module Main
where

import System.IO
import Control.Exception
import qualified Crypto.Hash.SHA512 as S(hashlazy)
import qualified Crypto.Hash.Whirlpool as W(hashlazy)
import Data.ByteString.Builder(toLazyByteString, stringUtf8)
import qualified Data.ByteString.Lazy as L(ByteString, fromStrict, toStrict, concat, singleton)
import qualified Data.ByteString as B(ByteString, foldl, concat, hPut)

stateMod :: Integer
stateMod = 104723 :: Integer

rowsMod :: Int
rowsMod = 137 :: Int

colsMod :: Int
colsMod = 139 :: Int

seedSep :: String
seedSep = "::"

megabytes :: Int
megabytes = 16

{- 64 bytes per hash emitted, this much iterations per megabyte -}
iterations :: Int
iterations = 16384

main :: IO ()
main = do 
   _ <- putStrLn "Enter empty key to finish"
   keysStr <- readKeysTillEmpty []

   let keysBytes = map (L.toStrict . toLazyByteString . stringUtf8) keysStr 

   let seedPlainStr = foldl (\ acc pwd -> acc ++ seedSep ++ pwd) "" keysStr
   let seedPlainBytes = toLazyByteString $ stringUtf8 seedPlainStr
   let seed = B.concat [hash 0 seedPlainBytes, hash 1 seedPlainBytes]

   let entropyCol0 = scanl (stretchAcc keysBytes) seed [1..rowsMod-1]
   let entropyMtx = map (\ rowSeed -> scanl (stretchAcc keysBytes) rowSeed [2..colsMod]) entropyCol0

   withFile "stretch.out" WriteMode $ writeStep entropyMtx (seed, megabytes * iterations) 

writeStep :: [[B.ByteString]] -> (B.ByteString, Int) -> Handle -> IO ()
writeStep entropyMtx (state, idx) fileHandle = 
  if idx <= 0 
    then return ()
    else do
      let stateInt = asState state
      let increment = entropyMtx !! (stateInt `mod` rowsMod) !! (stateInt `mod` colsMod)
      let nextState = hash stateInt $ L.concat [L.fromStrict state, L.fromStrict increment]
      _ <- B.hPut fileHandle nextState
      writeStep entropyMtx (nextState , idx - 1) fileHandle 

stretchAcc :: [B.ByteString] -> B.ByteString -> Int -> B.ByteString
stretchAcc keys acc idx =
  hash (idx + asState acc) fullAcc 
    where 
      fullAcc = 
        L.concat[
          L.fromStrict acc, 
          L.singleton $ fromIntegral idx, 
          L.fromStrict $ keys !! (idx `mod` length keys)
        ]
      
asInteger :: B.ByteString -> Integer
asInteger =
  B.foldl (\ num word8 -> num * 256 + fromIntegral word8 ) 0

asState :: B.ByteString -> Int
asState str =
  fromIntegral $ asInteger str `mod` stateMod 

hash :: Int -> L.ByteString -> B.ByteString
hash algo =
  case algo `mod` 2 of
    0 -> hashWirp
    _ -> hashSha

hashWirp :: L.ByteString -> B.ByteString
hashWirp = W.hashlazy

hashSha :: L.ByteString -> B.ByteString
hashSha = S.hashlazy

readKeysTillEmpty :: [String] -> IO [String]
readKeysTillEmpty accum = do
  nextPwdMaybe <- readKeyNumbered $ length accum  
  maybe (return accum) (\ nextPwd -> readKeysTillEmpty $ accum ++ [nextPwd] ) nextPwdMaybe  

readKeyNumbered :: Int -> IO (Maybe String)
readKeyNumbered idx = 
  readKeyVerified $ "Key #" ++ show idx

readKeyVerified :: String -> IO (Maybe String)
readKeyVerified prompt = do
  putStr $ prompt ++ " (enter):"
  pass1 <- readKey
  if null pass1 
    then
      return Nothing
    else do
      putStr $ prompt ++ " (verify):"
      pass2 <- readKey
      if pass1 == pass2 
        then 
          return (Just pass1) 
        else do 
          putStrLn "Retrying: 'enter' and 'verify' did not match"
          readKeyVerified prompt
      

readKey :: IO String
readKey = do 
  hFlush stdout
  old <- hGetEcho stdin
  password <- bracket_ (hSetEcho stdin False) (hSetEcho stdin old) getLine
  putChar '\n'
  return password

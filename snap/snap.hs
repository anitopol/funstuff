{-
  compile:
    ghc snap.hs -threaded 

  used source samples: 
    http://www.haskell.org/haskellwiki/Gtk2Hs/Tutorials/ThreadedGUIs 
    http://stackoverflow.com/questions/11963561/screen-capture-in-haskell
    http://stackoverflow.com/questions/3124229/taking-a-screenshot-with-c-gtk
-}
import Control.Concurrent -- forkIO and forkOS
import Control.Concurrent.MVar
import Graphics.UI.Gtk
import Graphics.UI.Gtk.Gdk.Screen
import System.Exit
import System.Environment
import Data.Time.Clock
import Data.Time
import System.Locale (defaultTimeLocale)

main :: IO ()
main = do
  _ <- initGUI
  {- create an exit MVar: keep our application alive until we wish to quit -}
  exit <- newEmptyMVar 
  {- Now we can create a window with some buttons and some threads of our own.
     Watch out though, at least one button MUST be bound to filling the exit MVar, 
     or else our program will close immediately and complain that it blocked 
     indefinitely on an MVar operation. -}
  window <- windowNew
  myTopHBox <- hBoxNew False 0
  containerAdd window myTopHBox
  timeLabel <- labelNew Nothing
  boxPackStart myTopHBox timeLabel PackNatural 0
  exitButton <- buttonNewWithLabel "Exit"
  boxPackEnd myTopHBox exitButton PackNatural 0
  exitButton `on` buttonActivated $ putMVar exit ExitSuccess
  forkIO $ do
    let
      printTime = do
        currTime <- getZonedTime
        {- http://pleac.sourceforge.net/pleac_haskell/datesandtimes.html -}
        let currTimeStr = formatTime defaultTimeLocale "%F--%H_%M" currTime
        {- We are not in GTK's own thread: so send GTK a 
            drawing action of type IO a. -}
        let setTextAction = do {
          Just screen <- screenGetDefault;
          window <- screenGetRootWindow screen;
          size <- drawableGetSize window;
          origin <- drawWindowGetOrigin window;
          Just pxbuf <- pixbufGetFromDrawable window ((uncurry . uncurry Rectangle) origin size);
          _ <- pixbufSave pxbuf (currTimeStr++".png") "png" [];
          labelSetText timeLabel (show currTimeStr);
        }
        _ <- postGUIAsync setTextAction
        _ <- threadDelay 30000000
        printTime 
    printTime
  {- window set up, now pack it -}
  widgetShowAll window
  {- start GTK's event loop in it's own thread. must be forkOS and not forkIO as
     the GTK thread is a FFI thread and cannot use Haskell's green threads -}
  forkOS mainGUI
  {- when we want our program to exit, we fill this MVar with an exit signal -}
  signal <- takeMVar exit
  {- We rarely want to exit imediately. 
     Usually, we want to wait for some other thread to finish saving the file etc. 
     We should have appropriate waiting code here. -}
  {- close GTK: GTK is in another thread, so post the action async -}
  postGUIAsync mainQuit
  {- and then we can exit. -}
  exitWith signal


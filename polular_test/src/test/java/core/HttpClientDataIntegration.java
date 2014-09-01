package core;


public class HttpClientDataIntegration {


    private static String url = "http://anitopol.github.io/polular/data/index.csv.txt";

    public static void main(String[] args) {

        String[] linesSplited = HttpTransport.retrieve();

        Response.responseBodyDataParsing(linesSplited);

    }


}



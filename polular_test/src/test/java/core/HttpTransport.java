package core;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpStatus;
import ui_tests.TestData;

import java.io.IOException;

/**
 * Created by a on 01.09.14.
 */
public class HttpTransport {
    private static String url = TestData.IndexMenuUrl;
    public static String[] retrieve() {
        HttpClient client = new HttpClient();

        GetMethod method = new GetMethod(url);
        String[] linesSplited;

        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        try {
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }

            byte[] responseBody = method.getResponseBody();
            String lines = new String(responseBody, "UTF-8");
            linesSplited = lines.split("\\r?\\n");


        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            method.releaseConnection();
        }
        return linesSplited;
    }
}

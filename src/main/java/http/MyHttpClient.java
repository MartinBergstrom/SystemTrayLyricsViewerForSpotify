package http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.function.Consumer;


public class MyHttpClient {

    public String postRequest(String address,
                              String payload,
                              Consumer<HttpRequest> requestAdder) {
        HttpClient client = HttpClientBuilder.create().build();
        try {
            HttpResponse response;
            HttpPost postRequest = new HttpPost(address);
            StringEntity payLoad = new StringEntity(payload);
            postRequest.setEntity(payLoad);
            requestAdder.accept(postRequest);
            response = client.execute(postRequest);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200)
            {
                throw new RuntimeException("Failed with HTTP error code : " + statusCode);
            }
            HttpEntity httpEntity = response.getEntity();
            return EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            client.getConnectionManager().shutdown();
        }
        return null;
    }

    public String getRequest(String address, Consumer<HttpRequest> headerAdder) {
        HttpClient client = HttpClientBuilder.create().build();
        try {

            HttpGet getRequest = new HttpGet(address);
            headerAdder.accept(getRequest);
            HttpResponse response = client.execute(getRequest);
            HttpEntity httpEntity = response.getEntity();
            return EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            client.getConnectionManager().shutdown();
        }
        return null;
    }
}
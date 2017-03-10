package Helper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.KeyStore;

/**
 * Created by jason on 10/03/2017.
 */
public class UpdateChecker {

    public static boolean checkUpate() {

        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpUriRequest request =
                new HttpGet("https://github.com/Jasonbroker/emailcrawler/releases/latest");

        // 打印请求信息
        System.out.println(request.getRequestLine());
        try {
            // 发送请求，返回响应
            HttpResponse response = httpClient.execute(request, new ResponseHandler<HttpResponse>() {
                @Override
                public HttpResponse handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    // 打印响应信息
                    System.out.println(response.getStatusLine());
                    HttpEntity entity = response.getEntity();
                    System.out.println(entity.getContentType());
                    System.out.println(entity.getContentLength());
                    String responseString = EntityUtils.toString(entity);
                    System.out.println(responseString);
                    return response;
                }
            });



        } catch (ClientProtocolException e) {
            // 协议错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络异常
            e.printStackTrace();
        }


        return true;
    }

}

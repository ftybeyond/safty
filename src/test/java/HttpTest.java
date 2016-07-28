import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FTY on 2016/7/14.
 */
public class HttpTest {

    CloseableHttpClient client;
    @Before
    public void init(){
        client = HttpClients.createDefault();

    }

//    @Test
    public void test(){
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthClientRequest accessTokenRequest = null;
        try {
            accessTokenRequest = OAuthClientRequest
                    .tokenLocation("http://localhost:8088/rest/authorize")
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId("c1ebe466-1cdc-4bd3-ab69-77c3561b9dee")
//                    .setClientSecret("d8346ea2-6017-43ed-ad68-19c0f971738b")
//                    .setCode("123123")
                    .setRedirectURI("http://www.baidu.com")
                    .buildQueryMessage();
            OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.POST);

            String accessToken = oAuthResponse.getAccessToken();
            Long expiresIn = oAuthResponse.getExpiresIn();

            OAuthClientRequest userInfoRequest = new OAuthBearerClientRequest("www.hao")
                    .setAccessToken(accessToken).buildQueryMessage();

            OAuthResourceResponse resourceResponse = oAuthClient.resource(userInfoRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
            String username = resourceResponse.getBody();
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test1() throws IOException {
        client = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://localhost:8088/rest/authorize");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("client_id","c1ebe466-1cdc-4bd3-ab69-77c3561b9dee"));
        params.add(new BasicNameValuePair("redirect_uri","http://www.baidu.com"));
        params.add(new BasicNameValuePair("response_type","code"));

        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        HttpResponse response = client.execute(post);

        System.out.println(EntityUtils.toString(response.getEntity()));
    }


    @After
    public void destory(){
        HttpClientUtils.closeQuietly(client);
    }


}

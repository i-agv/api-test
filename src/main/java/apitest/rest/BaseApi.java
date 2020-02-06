package apitest.rest;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;


public class BaseApi {

    @Autowired
    RestTemplate restTemplate = new RestTemplate();

    protected <T> ResponseEntity<T> sendPostRequest(String url, Object request, Class<T> clazz, Map<String, ?> parameters) {
        return restTemplate.postForEntity(url, request, clazz, parameters);
    }

    protected <T> ResponseEntity<T> sendGetRequest(String url, Class<T> clazz, Map<String, ?> parameters) {
        return restTemplate.getForEntity(url, clazz, parameters);
    }

    protected <T> ResponseEntity<T> sendPutRequest(String url, Object request, Class<T> clazz, Map<String, ?> parameters) {
        return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), clazz, parameters);
    }

    protected <T> ResponseEntity<T> sendDeleteRequest(String url, Object request, Class<T> clazz, Map<String, ?> parameters) {
        return restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(request), clazz, parameters);
    }

    protected <T> ResponseEntity<T> sendPatchRequest(String url, Object request, Class<T> clazz, Map<String, ?> parameters) {
        return restTemplate.exchange(url, HttpMethod.PATCH, new HttpEntity<>(request), clazz, parameters);
    }

    public BaseApi() {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = null;
        try {
            sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
        }

        assert sslContext != null;
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        restTemplate = new RestTemplate(requestFactory);
    }
}

package apitest.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class BaseApi {

    @Autowired
    RestTemplate restTemplate;

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
}

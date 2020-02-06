package apitest.rest;

import apitest.model.UserPost;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class TestApi extends BaseApi {

//    private String baseUrl = "http://reqres.in/api/";

    public ResponseEntity<UserPost> postClient(UserPost userPost) {
        return sendPostRequest("http://reqres.in/api/users", userPost, UserPost.class, new HashMap<String, String>());
    }
}

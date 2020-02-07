package apitest.rest;

import apitest.model.Error;
import apitest.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class TestApi extends BaseApi {

    private String baseUrl = "https://reqres.in/api/";
    private String usersUrl = baseUrl + "/users";

    public ResponseEntity<User> postUser(User user) {
        return sendPostRequest(usersUrl, user, User.class, new HashMap<String, String>());
    }

    public ResponseEntity<User> putUser(String id, User user) {
        return sendPutRequest(usersUrl + "/{id}", user, User.class,
                new HashMap<String, String>() {{
                    put("id", id);
                }});
    }

    public ResponseEntity<User> patchUser(String id, User user) {
        return sendPatchRequest(usersUrl + "/{id}", user, User.class,
                new HashMap<String, String>() {{
                    put("id", id);
                }});
    }

    public ResponseEntity<Error> deleteUser(String id) {
        return sendDeleteRequest(usersUrl + "/{id}", null,
                Error.class,
                new HashMap<String, String>() {{
                    put("id", id);
                }});
    }

    public ResponseEntity<SingleUser> getUser(String id) {
        return sendGetRequest(usersUrl + "/{id}",
                SingleUser.class,
                new HashMap<String, String>() {{
                    put("id", id);
                }});
    }

    public ResponseEntity<Error> getUserError(String id) {
        return sendGetRequest(usersUrl + "/{id}",
                Error.class,
                new HashMap<String, String>() {{
                    put("id", id);
                }});
    }

    public ResponseEntity<Users> getUsers(String page) {
        return sendGetRequest(usersUrl + "?page={page}",
                Users.class,
                new HashMap<String, String>() {{
                    put("page", page);
                }});
    }

    public ResponseEntity<Token> postRegister(Login login) {
        return sendPostRequest(baseUrl + "register", login, Token.class, new HashMap<String, String>());
    }

    public ResponseEntity<Token> postLogin(Login login) {
        return sendPostRequest(baseUrl + "login", login, Token.class, new HashMap<String, String>());
    }

    public ResponseEntity<Error> postRegisterError(Login login) {
        return sendPostRequest(baseUrl + "register", login, Error.class, new HashMap<String, String>());
    }

    public ResponseEntity<Error> postLoginError(Login login) {
        return sendPostRequest(baseUrl + "login", login, Error.class, new HashMap<String, String>());
    }
}

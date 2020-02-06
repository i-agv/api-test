package apitest;

import apitest.model.Error;
import apitest.model.*;
import apitest.rest.TestApi;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestUser {

    @Autowired
    TestApi testApi = new TestApi();

    @ParameterizedTest
    @DisplayName("POST /users successful")
    @MethodSource("createAndUpdateUser")
    public void createUser(User user) {
        ResponseEntity<User> response = testApi.postUser(user);
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "POST /users didn't return 201");

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertThat("POST /users returned client not as expected",
                response.getBody(),
                sameBeanAs(user)
                        .ignoring("createdAt")
                        .ignoring("id"));

        assertDurationBetweenNowLessThanMinute(response.getBody().getCreatedAt());
    }

    private static Stream<Arguments> createAndUpdateUser() {
        List<Arguments> arguments = Arrays.asList(
                Arguments.of(User.builder().name("test").job("bank").build()),
                Arguments.of(User.builder().name("123").build()),
                Arguments.of(User.builder().job("432").build())
        );
        return arguments.stream();
    }

    @ParameterizedTest
    @DisplayName("PUT /users/{id} successful")
    @MethodSource("createAndUpdateUser")
    public void putUser(User user) {
        ResponseEntity<User> response = testApi.putUser("1", user);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "PUT /users/{id} didn't return 200");

        assertNotNull(response.getBody());
        assertThat("PUT /users/{id} returned client not as expected",
                response.getBody(), sameBeanAs(user).ignoring("updatedAt"));

        assertDurationBetweenNowLessThanMinute(response.getBody().getUpdatedAt());
    }

    @ParameterizedTest
    @DisplayName("PATCH /users/{id} successful")
    @MethodSource("createAndUpdateUser")
    public void patchUser(User user) {
        ResponseEntity<User> response = testApi.patchUser("1", user);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "PATCH /users/{id} didn't return 200");

        assertNotNull(response.getBody());
        assertThat("PATCH /users/{id} returned client not as expected",
                response.getBody(), sameBeanAs(user).ignoring("updatedAt"));

        assertDurationBetweenNowLessThanMinute(response.getBody().getUpdatedAt());
    }

    @Test
    @DisplayName("DELETE /users/{id} successful")
    public void deleteUser() {
        ResponseEntity<Error> response = testApi.deleteUser(String.valueOf(RandomUtils.nextInt(1, 30)));
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "DELETE /users/{id} didn't return 204");
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("GET /users/{id} successful")
    public void getUser() {
        SingleUser user = SingleUser.builder()
                .data(UserData.builder()
                        .id("1")
                        .first_name("George")
                        .last_name("Bluth")
                        .email("george.bluth@reqres.in")
                        .avatar("https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg")
                        .build())
                .build();

        ResponseEntity<SingleUser> response = testApi.getUser("1");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "GET /users/{id} didn't return 200");

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertThat("GET /users/{id} returned client not as expected",
                response.getBody(), sameBeanAs(user));
    }

    @Test
    @DisplayName("GET /users successful")
    public void getUsers() {
        Users users = Users.builder()
                .page(1)
                .per_page(6)
                .total(12)
                .total_pages(2)
                .data(Collections.singletonList(UserData.builder()
                        .id("1")
                        .first_name("George")
                        .last_name("Bluth")
                        .email("george.bluth@reqres.in")
                        .avatar("https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg")
                        .build()))
                .build();

        ResponseEntity<Users> response = testApi.getUsers("1");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "GET /users didn't return 200");

        assertNotNull(response.getBody());
        Users apiUsers = response.getBody();

        assertNotNull(apiUsers.getData());
        assertThat("GET /users returned response not as expected",
                apiUsers, sameBeanAs(users).ignoring("data"));

        assertEquals((int) apiUsers.getPer_page(), apiUsers.getData().size());
        assertThat(apiUsers.getData().stream()
                        .filter(userData -> userData.getId().contains("1"))
                        .collect(Collectors.toList()),
                sameBeanAs(users.getData()));
    }

    @Test
    @DisplayName("GET /users/{id} 404 Not Found")
    public void getUserNotFound() {
        ResponseEntity<Error> response = testApi.getUserError(String.valueOf(RandomUtils.nextInt(20, 50)));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "GET /users/{id} didn't return 404");
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("POST /register successful")
    public void postRegister() {
        Login login = Login.builder().email("eve.holt@reqres.in").password("pistol").build();

        ResponseEntity<Token> response = testApi.postRegister(login);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "POST /register didn't return 200");

        assertNotNull(response.getBody());
        Token apiToken = response.getBody();
        assertNotNull(apiToken.getId());
        assertNotNull(apiToken.getToken());
    }

    @Test
    @DisplayName("POST /login successful")
    public void postLogin() {
        Login login = Login.builder().email("eve.holt@reqres.in").password("cityslicka").build();

        ResponseEntity<Token> response = testApi.postLogin(login);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "POST /login didn't return 200");

        assertNotNull(response.getBody());
        Token apiToken = response.getBody();
        assertNull(apiToken.getId());
        assertNotNull(apiToken.getToken());
    }

    @ParameterizedTest
    @DisplayName("POST /register unsuccessful")
    @MethodSource
    public void postRegisterError(Login login) {
        ResponseEntity<Error> response = testApi.postRegisterError(login);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "POST /register didn't return 400");

        assertNotNull(response.getBody());

        if (login.getEmail() == null || login.getEmail().isEmpty()) {
            assertEquals("Missing email or username", response.getBody().getError());
        } else if (login.getPassword() == null || login.getPassword().isEmpty()) {
            assertEquals("Missing password", response.getBody().getError());
        } else assertEquals("Note: Only defined users succeed registration", response.getBody().getError());
    }

    @ParameterizedTest
    @DisplayName("POST /login unsuccessful")
    @MethodSource("postRegisterError")
    public void postLoginError(Login login) {
        ResponseEntity<Error> response = testApi.postLoginError(login);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "POST /register didn't return 400");

        assertNotNull(response.getBody());

        if (login.getEmail() == null || login.getEmail().isEmpty()) {
            assertEquals("Missing email or username", response.getBody().getError());
        } else if (login.getPassword() == null || login.getPassword().isEmpty()) {
            assertEquals("Missing password", response.getBody().getError());
        } else assertEquals("Note: Only defined users succeed registration", response.getBody().getError());
    }

    private static Stream<Arguments> postRegisterError() {
        List<Arguments> arguments = Arrays.asList(
                Arguments.of(Login.builder().email("test").password("qwe").build()),
                Arguments.of(Login.builder().email("sydney@fife").build()),
                Arguments.of(Login.builder().email("test@mail.ru").password("").build())
        );
        return arguments.stream();
    }

    private void assertDurationBetweenNowLessThanMinute(Instant time) {
        assertTrue(Duration.between(time, Instant.now()).toMinutes() < 1,
                "Difference between date and now more than a minute");
    }
}

package apitest;

import apitest.model.UserPost;
import apitest.rest.TestApi;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.Instant;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestPost {

    @Autowired
    TestApi testApi;

    @Test
    public void createUser() {
        UserPost user = UserPost.builder().name("test").job("bank").build();

        ResponseEntity<UserPost> response = testApi.postClient(user);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED, "Код ответа не 201");

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertThat("POST /client returned client not as expected",
                response.getBody(),
                sameBeanAs(user)
                        .ignoring("createdAt")
                        .ignoring("id"));

        assertDurationBetweenNowLessThanMinute(response.getBody().getCreatedAt());

    }


    private void assertDurationBetweenNowLessThanMinute(Instant time) {
        assertTrue(Duration.between(time, Instant.now()).toMinutes() < 1,
                "Difference between date and now more than a minute");
    }
}

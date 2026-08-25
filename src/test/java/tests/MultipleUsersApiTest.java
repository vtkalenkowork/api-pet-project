package tests;

import client.UserClient;
import data.UserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class MultipleUsersApiTest {
    private UserClient userClient;
    private List<Long> userIds;

    @BeforeEach
    public void setup() {
        userClient = new UserClient();
        userIds = new ArrayList<>();
    }

    @Test
    public void shouldCreateSeveralUsers() {
        //not convenient for multiple users - just for example
        /*UserRequest user1 = new UserRequest("Slava", "slava@test.com");
        UserRequest user2 = new UserRequest("John", "john@test.com");
        UserRequest user3 = new UserRequest("Mike", "mike@test.com");

        userClient.createUser(user1);
        userClient.createUser(user2);
        userClient.createUser(user3);*/

        //Better approach
        List<UserRequest> users = List.of(
                new UserRequest("Slava", "slava@test.com"),
                new UserRequest("John", "john@test.com"),
                new UserRequest("Mike", "mike@test.com")
        );

        for (UserRequest user : users) {
            Long userId = userClient.createUser(user)
                    .jsonPath()
                    .getLong("id");

            userIds.add(userId);
        }
    }

    @AfterEach
    public void tearDown() {
        for (Long userId : userIds) {
            userClient.deleteUser(userId);
        }
    }
}

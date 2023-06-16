package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserDbStorageTest {

    @Autowired
    private final UserDbStorage userDbStorage;

    private User start() {
        User user = new User();
        user.setEmail("aa@mail.ru");
        user.setLogin("aleshka34");
        user.setName("Алексей Алексеевич");
        user.setBirthday(LocalDate.of(1990, 01, 10));
        return user;
    }

    private User createTestUser() {
        User user = new User();
        user.setEmail("a@.ru");
        user.setLogin("alex68");
        user.setName("Alex");
        user.setBirthday(LocalDate.of(1990, 1, 10));
        return user;
    }

    private User createTestUser2() {
        User user = new User();
        user.setEmail("i@.ru");
        user.setLogin("ivan68");
        user.setName("Ivan");
        user.setBirthday(LocalDate.of(1995, 4, 6));
        return user;
    }

    @Test
    void addAndGetTest() {
        User testUser = start();
        userDbStorage.createUser(testUser);
        assertEquals("aa@mail.ru", testUser.getEmail());
        assertEquals("aleshka34", testUser.getLogin());
        assertEquals("Алексей Алексеевич", testUser.getName());
        assertEquals(LocalDate.of(1990, 01, 10), testUser.getBirthday());
    }

    @Test
    void getUsersTest() {
        User testUser = start();
        userDbStorage.createUser(testUser);
        List<User> usersList = List.of(testUser);

        assertEquals(usersList.toString(), userDbStorage.getUsers().toString());
    }

    @Test
    void getUserByIdTest() {
        User testUser = start();
        userDbStorage.createUser(testUser);

        Optional<User> filmOptional = Optional.of(userDbStorage.getUserById(testUser.getId()));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    void updateUserTest() {
        User testUser = start();
        userDbStorage.createUser(testUser);
        testUser.setName("Алеша");
        User newUser = userDbStorage.updateUser(testUser);
        assertEquals(newUser, userDbStorage.getUserById(testUser.getId()));
    }

    @Test
    void addFriendsTest() {
        User user1 = userDbStorage.createUser(createTestUser());
        User user2 = userDbStorage.createUser(createTestUser2());

        userDbStorage.addFriend(user1, user2);
        userDbStorage.addFriend(user2, user1);

        List<User> friendsByUser1 = userDbStorage.getFriends(user1.getId());
        List<User> friendsByUser2 = userDbStorage.getFriends(user2.getId());

        assertTrue(friendsByUser1.contains(user2));
        assertTrue(friendsByUser2.contains(user1));

    }

    @Test
    void addCommonFriendsTest() {
        User user1 = userDbStorage.createUser(createTestUser());
        User user2 = userDbStorage.createUser(createTestUser2());
        User user3 = userDbStorage.createUser(start());

        userDbStorage.addFriend(user1, user3);
        userDbStorage.addFriend(user2, user3);

        List<User> commonFriends = userDbStorage.getCommonFriends(user1.getId(), user2.getId());

        assertTrue(commonFriends.contains(user3));

    }

}

package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    UserStorage userStorage;
    UserService userService;

    @BeforeEach
    protected void start() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
    }

//     @Test
//     void validateEmail() {
//         final UserController userController = new UserController(userStorage, userService);
//         final User user = new User();
//         user.setEmail("email.without.doggie.symbol.com");

//         assertThrows(RuntimeException.class, () -> userStorage.validate(user));
//     }

//     @Test
//     void validateLogin() {
//         final UserController userController = new UserController(userStorage, userService);
//         final User user = new User();
//         user.setLogin("");

//         assertThrows(RuntimeException.class, () -> userStorage.validate(user));
//     }

//     @Test
//     void validateName() {
//         final UserController userController = new UserController(userStorage, userService);
//         final User user = new User();
//         user.setLogin("login");
//         user.setName("");

//         assertThrows(NullPointerException.class, () -> userStorage.validate(user));
//     }

//     @Test
//     void validateBirthday() {
//         final UserController userController = new UserController(userStorage, userService);
//         final User user = new User();
//         user.setBirthday(LocalDate.MAX);

//         assertThrows(RuntimeException.class, () -> userStorage.validate(user));
//     }
}

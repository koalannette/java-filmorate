package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    @Test
    void validateEmail() {
        final UserController userController = new UserController();
        final User user = new User();
        user.setEmail("email.without.doggie.symbol.com");

        assertThrows(RuntimeException.class, () -> userController.validateDate(user) );
    }

    @Test
    void validateLogin() {
        final UserController userController = new UserController();
        final User user = new User();
        user.setLogin("");

        assertThrows(RuntimeException.class, () -> userController.validateDate(user) );
    }

    @Test
    void validateName() {
        final UserController userController = new UserController();
        final User user = new User();
        user.setLogin("login");
        user.setName("");
        userController.validateName(user);

        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void validateBirthday() {
        final UserController userController = new UserController();
        final User user = new User();
        user.setBirthday(LocalDate.MAX);

        assertThrows(RuntimeException.class, () -> userController.validateDate(user) );
    }
}

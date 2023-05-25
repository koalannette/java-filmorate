package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int userId = 0;

    private int getIdForFilm() {
        return ++userId;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        validate(user);
        user.setId(getIdForFilm());
        users.put(user.getId(), user);
        log.info("Поступил запрос на добавление пользователя. Пользователь " + user.getId() + " добавлен");

        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        validate(user);
        if (users.get(user.getId()) != null) {
            users.put(user.getId(), user);
            log.info("Поступил запрос на изменения пользователя. Пользователь " + user.getId() + " изменён.");
        } else {
            log.error("Поступил запрос на изменения пользователя. Пользователь " + user.getId() + " не найден.");
            throw new NotFoundException("User not found.");
        }
        return user;
    }

    @GetMapping("/users")
    public List<User> getFilms() {
        log.info("Запрос всех пользователей");
        return new ArrayList<>(users.values());
    }

    public void validate(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Почта некорректная.");
            throw new ValidationException("Почта некорректная.");
        }

        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Логин пользователя пустой.");
            throw new ValidationException("Логин пользователя пустой.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имя пользователя пустое. Был использован логин");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

}


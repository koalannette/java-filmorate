package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
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
    public User createUser (@RequestBody User user) {
        validateEmail(user);
        validateName(user);
        validateLogin(user);
        validateDate(user);

        user.setId(getIdForFilm());
        users.put(user.getId(), user);
        log.info("Поступил запрос на добавление пользователя. Пользователь добавлен");

        return user;
    }

    @PutMapping("/users")
    public User updateUser (@RequestBody User user) {
        validateEmail(user);
        validateName(user);
        validateLogin(user);
        validateDate(user);

        if (users.get(user.getId()) != null) {
            users.put(user.getId(), user);
            log.info("Поступил запрос на изменения пользователя. Пользователь изменён.");
        } else {
            log.error("Поступил запрос на изменения пользователя. Пользователь не найден.");
            throw new NotFoundException("User not found.");
        }
        return user;
    }

    @GetMapping("/users")
    public List<User> getFilms() {
        log.info("Запрос всех пользователей");
        return new ArrayList<>(users.values());
    }

    public void validateEmail(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Почта некорректная.");
            throw new ValidationException("Почта некорректная.");
        }
    }

    public void validateLogin(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Логин пользователя пустой.");
            throw new ValidationException("Логин пользователя пустой.");
        }
    }

    public void validateName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имя пользователя пустое. Был использован логин");
        }
        }

    public void validateDate(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

    }


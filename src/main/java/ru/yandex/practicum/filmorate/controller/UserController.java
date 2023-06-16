package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public User createUser(@Validated @RequestBody User user) {
        log.info("Поступил запрос на добавление пользователя.");
        return userService.createUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@Validated @RequestBody User user) {
        log.info("Поступил запрос на изменения пользователя.");
        return userService.updateUser(user);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("Запрос всех пользователей");
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable long id) {
        log.info("Поступил запрос на получение пользователя");
        return userService.getUserById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Поступил запрос на добавление в друзья.");
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Поступил запрос на удаление из друзей.");
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        log.info("Поступил запрос на получение друзей.");
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Поступил запрос на получение общих друзей.");
        return userService.getCommonFriends(id, otherId);
    }

}


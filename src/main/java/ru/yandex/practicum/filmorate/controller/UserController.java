package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        log.info("Поступил запрос на добавление пользователя.");
        return userStorage.createUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        log.info("Поступил запрос на изменения пользователя.");
        return userStorage.updateUser(user);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("Запрос всех пользователей");
        return userStorage.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable String id) {
        log.info("Поступил запрос на получение пользователя");
        return userStorage.getUserById(Integer.parseInt(id));
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable String id, @PathVariable String friendId) {
        log.info("Поступил запрос на добавление в друзья.");
        return userService.addFriend(Integer.parseInt(id), Integer.parseInt(friendId));
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable String id, @PathVariable String friendId) {
        log.info("Поступил запрос на удаление из друзей.");
        userService.deleteFriend(Integer.parseInt(id), Integer.parseInt(friendId));
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable String id) {
        log.info("Поступил запрос на получение друзей.");
        return userService.getFriends(Integer.parseInt(id));
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable String id, @PathVariable String otherId) {
        log.info("Поступил запрос на получение общих друзей.");
        return userService.getCommonFriends(Integer.parseInt(id), Integer.parseInt(otherId));
    }

}


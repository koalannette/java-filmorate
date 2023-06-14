package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    User getUserById(long id);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    List<User> getFriends(long id);

    List<User> getCommonFriends(long userId, long friendId);

//    void validate(User user);
}

package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    User getUserById(long id);

//    User addFriend(int userId, int friendId);
//
//    User deleteFriend(int userId, int friendId);
//
//    List<User> getFriendsByUserId(int id);
//
//    List<User> getCommonFriends(int userId, int friendId);
//
//    void validate(User user);
}

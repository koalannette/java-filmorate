//package ru.yandex.practicum.filmorate.storage.user;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Component
//public class InMemoryUserStorage implements UserStorage {
//
//    private final HashMap<Integer, User> users = new HashMap<>();
//    private int userId = 0;
//
//    private int getIdForFilm() {
//        return ++userId;
//    }
//
//    @Override
//    public User createUser(User user) {
//        validate(user);
//        user.setId(getIdForFilm());
//        users.put(user.getId(), user);
//        log.info("Пользователь " + user.getId() + " добавлен");
//        return user;
//    }
//
//    @Override
//    public User updateUser(User user) {
//        validate(user);
//        if (users.get(user.getId()) != null) {
//            users.put(user.getId(), user);
//            log.info("Пользователь " + user.getId() + " изменён.");
//        } else {
//            log.error("Пользователь " + user.getId() + " не найден.");
//            throw new NotFoundException("User not found.");
//        }
//        return user;
//    }
//
//    @Override
//    public List<User> getUsers() {
//        log.info("Данные пользователей отправлены.");
//        return new ArrayList<>(users.values());
//    }
//
//    @Override
//    public User getUserById(int id) {
//        if (users.containsKey(id)) {
//            return users.get(id);
//        } else throw new NotFoundException("User not found.");
//    }
//
//    @Override
//    public User addFriend(int userId, int friendId) {
//        if (friendId > 0) {
//            getUserById(userId).getFriends().add(friendId);
//            getUserById(friendId).getFriends().add(userId);
//            return getUserById(userId);
//        }
//        throw new NotFoundException("Friend not found.");
//    }
//
//    @Override
//    public User deleteFriend(int userId, int friendId) {
//        getUserById(userId).getFriends().remove(friendId);
//        getUserById(friendId).getFriends().remove(userId);
//        return getUserById(userId);
//    }
//
//    @Override
//    public List<User> getFriendsByUserId(int id) {
//        return getUserById(id).getFriends()
//                .stream()
//                .map(this::getUserById)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<User> getCommonFriends(int userId, int friendId) {
//        User user = users.get(userId);
//        User friend = users.get(friendId);
//        return user.getFriends().stream().filter(u -> friend.getFriends().contains(u)).map(u -> users.get(u)).collect(Collectors.toList());
//    }
//
//    public void validate(User user) {
//        if (user.getName() == null || user.getName().isBlank()) {
//            user.setName(user.getLogin());
//            log.debug("Имя пользователя пустое. Был использован логин");
//        }
//    }
//}

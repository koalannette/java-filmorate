package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {

        if (doesTheUserEmailNotExist(user.getEmail())) {

            String userName = user.getName();
            if (userName == null || userName.isBlank()) {
                user.setName(user.getLogin());
            }

            String sqlQuery = "insert into users (email, login, name, birthday)" +
                    "values (?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
                stmt.setString(1, user.getEmail());
                stmt.setString(2, user.getLogin());
                stmt.setString(3, user.getName());
                stmt.setDate(4, Date.valueOf(user.getBirthday()));
                return stmt;
            }, keyHolder);

            long userId = keyHolder.getKey().longValue();
            user.setId(userId);

            return user;

        } else {
            throw new ValidationException(String.format("Пользователь с почтой %s уже существует!", user.getEmail()));
        }
    }

    @Override
    public List<User> getUsers() {
        String sqlQuery = "select id, email, login, name, birthday from users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User getUserById(long id) {
        String sqlQuery = "select id, email, login, name, birthday " +
                "from users where id = ?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        } catch (RuntimeException e) {
            throw new NotFoundException(String.format("Пользователя с ID %d не существует!", id));
        }
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update users set " +
                "email = ?, login = ?, name = ?, birthday = ?" +
                "where id = ?";

        if (doesTheUserExist(user.getId())) {
            jdbcTemplate.update(sqlQuery,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            return user;

        } else {
            throw new NotFoundException(String.format("Пользователя с ID %d не существует!", user.getId()));
        }
    }

    @Override
    public void addFriend(User user, User friend) {

        List<User> friendsForUser = getFriends(user.getId());

        if (!friendsForUser.contains(friend)) {


            String sqlQuery = "insert into friends (user_id, friend_id)" +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery, user.getId(), friend.getId());
            log.info(String.format("Друг %s был добавлен к пользователю %s в друзья!", friend.getLogin(), user.getLogin()));


        } else {
            throw new ValidationException(String.format("Друг %s уже есть в друзьях у пользователя %s!", friend.getLogin(), user.getLogin()));
        }
    }

    @Override
    public void deleteFriend(User user, User friend) {
        String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";

        jdbcTemplate.update(sqlQuery, user.getId(), friend.getId());
    }

    @Override
    public List<User> getFriends(long userId) {
        String sqlQuery = "select * " +
                "from users " +
                "where id in " +
                "(select friend_id " +
                "from friends " +
                "where user_id = ?)";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        String sqlQuery = "select * " +
                "from users as u " +
                "join friends as f on u.id = f.friend_id " +
                "where f.user_id = ? and friend_id in " +
                "(select friend_id " +
                "from friends " +
                "where user_id = ?)";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId, otherId);
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());

        return user;
    }

    private boolean doesTheUserExist(long id) {
        String sqlQuery = "select count(id) from users where id = ?";
        Long countId = jdbcTemplate.queryForObject(sqlQuery, Long.class, id);
        return countId != null && countId > 0;
    }

    private boolean doesTheUserEmailNotExist(String email) {
        String sqlQuery = "select count(id) from users where email = ?";
        Long countId = jdbcTemplate.queryForObject(sqlQuery, Long.class, email);
        return countId != null && countId <= 0;
    }

    private void updateFriendshipStatus() {

    }
}

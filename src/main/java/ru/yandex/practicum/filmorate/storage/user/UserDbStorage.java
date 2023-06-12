package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "insert into users (email, login, name, birthday)" +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        long userId = keyHolder.getKey().longValue();
        user.setId(userId);

        return user;
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
            jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(),
                    user.getBirthday(), user.getId());

            return user;
        } else {
            throw new NotFoundException(String.format("Пользователя с ID %d не существует!", user.getId()));
        }
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setLogin(rs.getString("email"));
        user.setName(rs.getString("login"));
        user.setEmail(rs.getString("name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());

        return user;
    }

    private boolean doesTheUserExist(long id) {
        String sqlQuery = "select count(id) from users where id = ?";
        long countId = jdbcTemplate.queryForObject(sqlQuery, Long.class, id);

        if (countId > 0) {
            return true;
        } else {
            return false;
        }
    }

}

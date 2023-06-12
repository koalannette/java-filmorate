package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query("select * from mpa", this::mapRowToMpa);
    }

    @Override
    public Mpa getMpaById(int id) {
        String sqlQuery = "select * from mpa where id = ?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
        } catch (RuntimeException e) {
            throw new NotFoundException(String.format("Рейтинга с ID %d не существует!", id));
        }
    }

    private Mpa mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("id"));
        mpa.setType(rs.getString("name"));
        mpa.setDescription(rs.getString("description"));
        return mpa;
    }
}

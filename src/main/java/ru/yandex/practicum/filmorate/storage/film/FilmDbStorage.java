package ru.yandex.practicum.filmorate.storage.film;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

@Repository
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "insert into films (name, description, release_date, duration, mpa_id)" +
                "values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);

        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return film;
        }
        genreStorage.addGenre(film, film.getGenres());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update films set " +
                "name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "where id = ?";

        if (filmExist(film.getId())) {
            jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getMpa().getId(), film.getId());

            for (Genre genre : film.getGenres()) {
                String sqlQuery1 = "insert into films_genres(film_id, genre_id) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlQuery1, film.getId(), genre.getId());
            }
            return film;
        } else {
            throw new NotFoundException(String.format("Фильма с ID %d не существует!", film.getId()));
        }
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "select * from films";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film getFilmById(long id) {
        String sqlQuery = "select * from films where id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (RuntimeException e) {
            throw new NotFoundException(String.format("Фильма с ID %d не существует!", id));
        }
    }

    private boolean filmExist(long id) {
        String sqlQuery = "select count(id) from films where id = ?";
        long countId = jdbcTemplate.queryForObject(sqlQuery, Long.class, id);

        if (countId > 0) {
            return true;
        } else {
            return false;
        }
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(mpaStorage.getMpaById(rs.getInt("mpa_id")));
        film.setGenres(new HashSet<>(genreStorage.getGenresByFilmId(rs.getLong("id"))));
        return film;
    }

}

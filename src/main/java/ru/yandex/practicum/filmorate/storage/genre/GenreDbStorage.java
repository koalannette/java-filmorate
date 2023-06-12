package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenres() {
        String sqlQuery = "select * from film_genre";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }


    @Override
    public void addGenre(Film film, Set<Genre> genres) {
        if (film!=null) {
            String sqlQuery = "insert into film_genre (film_id, genre_id) values (?, ?)";
            genres.forEach(genre -> jdbcTemplate.update(sqlQuery, film.getId(), genre.getId()));
            film.setGenres(new HashSet<>(getGenresByFilmId(film.getId())));
        }
    }

    @Override
    public Genre getGenreById(int genreId) {
        String sqlQuery = "select * from film_genre where genre_id = ?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, genreId);
        } catch (RuntimeException e) {
            throw new NotFoundException(String.format("Жанра с ID %d не существует!", genreId));
        }
    }

    @Override
    public List<Genre> getGenresByFilmId(long filmId) {
        String sqlQuery = "select *" + " from film_genre as fg" +
                " join genre as g on g.genre_id = fg.genre_id" +
                " where fg.film_id = ?" +
                " order by genre_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId);
    }


    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("id"));
        genre.setName(rs.getString("name"));
        return genre;
    }

}

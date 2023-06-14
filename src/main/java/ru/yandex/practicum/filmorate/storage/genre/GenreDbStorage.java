package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void addGenre(Film film, Set<Genre> genres) {
        if (film != null) {
            String sqlQuery = "insert into film_genre (film_id, genre_id) values (?, ?)";
            genres.forEach(genre -> jdbcTemplate.update(sqlQuery, film.getId(), genre.getId()));
            film.setGenres(new HashSet<>(getGenresByFilmId(film.getId())));
        }
    }

    @Override
    public List<Genre> getGenres() {
        String sqlQuery = "select * from genre";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Genre getGenreById(int genreId) {
        String sqlQuery = "select * from genre where id = ?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, genreId);
        } catch (RuntimeException e) {
            throw new NotFoundException(String.format("Жанра с ID %d не существует!", genreId));
        }
    }

    @Override
    public List<Genre> getGenresByFilmId(long filmId) {
        String sqlQuery = "select *" +
                " from film_genre as fg" +
                " join genre as g on g.id = fg.genre_id" +
                " where fg.film_id = ?" +
                " order by genre_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId);
    }

    @Override
    public void updateGenre(Film film, Set<Genre> genres) {
        String sqlQuery = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());

        String sqlQuery1 = "insert into film_genre (film_id, genre_id) values (?, ?)";
        List<Genre> genreList = new ArrayList<>(genres);
        jdbcTemplate.batchUpdate(sqlQuery1, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, film.getId());
                ps.setInt(2, genreList.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genreList.size();
            }
        });
    }

    @Override
    public void putGenresForFilm(List<Film> films) {
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));

        String sqlQuery = String.format("select fg.film_id, g.id, g.name, " +
                "from genre as g, film_genre as fg " +
                "where fg.genre_id = g.id " +
                "and fg.film_id in (%s)", inSql);

        Map<Long, Film> filmMap = films.stream().collect(Collectors.toMap(Film::getId, identity()));

        jdbcTemplate.query(sqlQuery, (rs) -> {
            Film film = filmMap.get(rs.getLong("film_id"));
            Genre genre = new Genre(rs.getInt("id"), rs.getString("name"));
            film.getGenres().add(genre);
        }, films.stream().map(Film::getId).toArray());
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("id"));
        genre.setName(rs.getString("name"));
        return genre;
    }

}

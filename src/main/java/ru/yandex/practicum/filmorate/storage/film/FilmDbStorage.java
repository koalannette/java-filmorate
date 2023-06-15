package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

@Repository
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

//            for (Genre genre : film.getGenres()) {
//                String sqlQuery1 = "insert into film_genre(film_id, genre_id) " +
//                        "values (?, ?)";
//                jdbcTemplate.update(sqlQuery1, film.getId(), genre.getId());
//            }

            genreStorage.updateGenre(film, film.getGenres());
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

    @Override
    public void like(Film film, User user) {
        String sqlQuery = "insert into likes (film_id, user_id)" +
                " values (?, ?)";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
    }

    @Override
    public void deleteLike(Film film, User user) {
        String sqlQuery = "delete from likes where film_id = ? and user_id = ? ";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
    }

    @Override
    public List<Film> getBestFilms(int count) {
        String sqlQuery = "select f.id, f.name, f.description, f.release_date, f.duration, " +
                "rm.mpa_id, r.name as mpa_name, " +
                "from films as f " +
                "left join rating as r on f.mpa_id = r.mpa_id " +
                "left join likes as l on f.film_id = l.film_id " +
                "group by f.id " +
                "order by count(l.user_id) desc " +
                "limit ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
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

        String likesSql = "select * from likes where film_id = ?";
        List<Integer> usersCollection = jdbcTemplate.query(likesSql, (rs1, rowNum1) -> makeFilmsLike(rs1), film.getId());

        for (Integer like : usersCollection) {
            film.getLikes().add(like);
        }

        return film;
    }

    private Integer makeFilmsLike(ResultSet rs) throws SQLException {
        Integer userId = rs.getInt("user_id");
        return userId;
    }

}

package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public List<Film> getFilms() {
        List<Film> filmList = filmStorage.getFilms();
        genreStorage.putGenresForFilm(filmList);
        return filmStorage.getFilms();
    }

    public Film updateFilm(Film newFilm) {
        return filmStorage.updateFilm(newFilm);
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.getFilmById(id);
        genreStorage.putGenresForFilm(List.of(film));
        return filmStorage.getFilmById(id);
    }

    public void like(long filmId, long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        filmStorage.like(film, user);
    }

    public void deleteLike(long filmId, long userId) {
        Set<Integer> likes = filmStorage.getFilmById(filmId).getLikes();
        if (likes.contains(userId)) {
            likes.remove(userId);
        }
        log.info("У фильма " + filmId + " нет лайка от пользователя " + userId);
    }

    public List<Film> getBestFilms(int quantity) {
        return filmStorage.getFilms().stream().sorted((film1, film2) ->
                        film2.getLikes().size() - film1.getLikes().size())
                .limit(quantity).collect(Collectors.toList());
    }


}

package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film updateFilm(Film newFilm) {
        return filmStorage.updateFilm(newFilm);
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public void like(int filmId, int userId) {
        filmStorage.getFilmById(filmId).getLikes().add(userId);
    }

    public void deleteLike(int filmId, int userId) {
        Set<Integer> likes = filmStorage.getFilmById(filmId).getLikes();
        if (likes.contains(userId)) {
            likes.remove(userId);
        }
        log.info("У фильма " + filmId + " нет лайка от пользователя " + userId);
    }

    public List<Film> showPopularFilms(int quantity) {
        return filmStorage.getFilms().stream().sorted((film1, film2) ->
                        film2.getLikes().size() - film1.getLikes().size())
                .limit(quantity).collect(Collectors.toList());
    }


}

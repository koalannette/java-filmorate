package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getFilms();

    Film getFilmById(long id);

    void like(Film film, User user);

    List<User> getLikesByFilmId(long filmId);

    void deleteLike(Film film, User user);

    List<Film> getBestFilms(int count);

}

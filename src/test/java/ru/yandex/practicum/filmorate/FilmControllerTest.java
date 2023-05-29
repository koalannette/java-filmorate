package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {

    FilmStorage filmStorage;
    FilmService filmService;

    @BeforeEach
    protected void start() {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage);
    }

    @Test
    void validateNameTest() {
        final FilmController filmController = new FilmController(filmStorage, filmService);
        final Film film = new Film();
        film.setName("");

        assertThrows(RuntimeException.class, () -> filmStorage.validate(film));
    }

    @Test
    void validateDescriptionTest() {
        final FilmController filmController = new FilmController(filmStorage, filmService);
        final Film film = new Film();
        film.setDescription("Описание для фильма длиной больше 200 символов. Описание для фильма длиной больше 200 символов. " +
                "Описание для фильма длиной больше 200 символов. Описание для фильма длиной больше 200 символов. Описание для фильма длиной больше 200 символов. " +
                "Описание для фильма длиной больше 200 символов. Описание для фильма длиной больше 200 символов. Описание для фильма длиной больше 200 символов. " +
                "Описание для фильма длиной больше 200 символов. Описание для фильма длиной больше 200 символов.");

        assertThrows(RuntimeException.class, () -> filmStorage.validate(film));
    }

    @Test
    void validateDateTest() {
        final FilmController filmController = new FilmController(filmStorage, filmService);
        final Film film = new Film();
        film.setReleaseDate(LocalDate.MIN);
        assertThrows(RuntimeException.class, () -> filmStorage.validate(film));
    }

    @Test
    void validateDurationTest() {
        final FilmController filmController = new FilmController(filmStorage, filmService);
        final Film film = new Film();
        film.setDuration(-100);
        assertThrows(RuntimeException.class, () -> filmStorage.validate(film));
    }

}

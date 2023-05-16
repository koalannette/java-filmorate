package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {


    @Test
    void validateNameTest() {
        final FilmController filmController = new FilmController();
        final Film film = new Film();
        film.setName("");

        assertThrows(RuntimeException.class, () -> filmController.validateName(film));
    }

    @Test
    void validateDescriptionTest() {
        final FilmController filmController = new FilmController();
        final Film film = new Film();
        film.setDescription("Описание для фильма длиной больше 200 символов. Описание для фильма длиной больше 200 символов. " +
                "Описание для фильма длиной больше 200 символов. Описание для фильма длиной больше 200 символов. Описание для фильма длиной больше 200 символов. " +
                "Описание для фильма длиной больше 200 символов. Описание для фильма длиной больше 200 символов. Описание для фильма длиной больше 200 символов. " +
                "Описание для фильма длиной больше 200 символов. Описание для фильма длиной больше 200 символов.");

        assertThrows(RuntimeException.class, () -> filmController.validateDescription(film));
    }

    @Test
    void validateDateTest() {
        final FilmController filmController = new FilmController();
        final Film film = new Film();
        film.setReleaseDate(LocalDate.MIN);
        assertThrows(RuntimeException.class, () -> filmController.validateDescription(film));
    }

    @Test
    void validateDurationTest() {
        final FilmController filmController = new FilmController();
        final Film film = new Film();
        film.setDuration(-100);
        assertThrows(RuntimeException.class, () -> filmController.validateDescription(film));
    }

}

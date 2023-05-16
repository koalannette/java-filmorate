package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int filmId = 0;
    private final LocalDate minDate = LocalDate.of(1895, 12, 28);

    private int getIdForFilm() {
        return ++filmId;
    }

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) {
        validateName(film);
        validateDescription(film);
        validateDate(film);
        validateDuration(film);

        film.setId(getIdForFilm());
        films.put(film.getId(), film);
        log.info("Поступил запрос на добавление фильма. Фильм добавлен");
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        validateName(film);
        validateDescription(film);
        validateDate(film);
        validateDuration(film);

        if (films.get(film.getId()) != null) {
            films.put(film.getId(), film);
            log.info("Поступил запрос на изменения фильма. Фильм изменён.");
        } else {
            log.error("Поступил запрос на изменения фильма. Фильм не найден.");
            throw new NotFoundException("Film not found.");
        }
        return film;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Запрос всех фильмов");
        return new ArrayList<>(films.values());
    }

    public void validateName(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Название фильма не может быть пустым.");
            throw new ValidationException("Название фильма не может быть пустым.");
        }

    }

    public void validateDescription(Film film) {
        if (film.getDescription().length() > 200) {
            log.warn("Описание фильма не может быть больше 200 символов.");
            throw new ValidationException("Описание фильма не может быть больше 200 символов.");
        }
    }

    public void validateDate(Film film) {
        if (film.getReleaseDate().isBefore(minDate)) {
            log.warn("Дата релиза не может быть раньше 28.12.1895\nТекущая дата релиза: " + film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895\nТекущая дата релиза: " + film.getReleaseDate());
        }
    }

    public void validateDuration(Film film) {
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма не может быть отрицательной.");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной.");
        }

    }

}

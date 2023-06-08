package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int filmId = 0;
    private final LocalDate minDate = LocalDate.of(1895, 12, 28);

    private int getIdForFilm() {
        return ++filmId;
    }

    @Override
    public Film createFilm(Film film) {
        validate(film);
        film.setId(getIdForFilm());
        films.put(film.getId(), film);
        log.info("Фильм " + film.getId() + " добавлен");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validate(film);
        if (films.get(film.getId()) != null) {
            films.put(film.getId(), film);
            log.info("Фильм " + film.getId() + " изменён.");
        } else {
            log.error("Фильм " + film.getId() + " не найден.");
            throw new NotFoundException("Film not found.");
        }
        return film;
    }

    @Override
    public List<Film> getFilms() {
        log.info("Данные фильмов отправлены.");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else throw new NotFoundException("Film not found.");
    }

    @Override
    public void validate(Film film) {
        if (film.getDescription().length() > 200) {
            log.warn("Описание фильма не может быть больше 200 символов.");
            throw new ValidationException("Описание фильма не может быть больше 200 символов.");
        }

        if (film.getReleaseDate().isBefore(minDate)) {
            log.warn("Дата релиза не может быть раньше 28.12.1895\nТекущая дата релиза: " + film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895\nТекущая дата релиза: " + film.getReleaseDate());
        }

        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма не может быть отрицательной.");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной.");
        }

    }

}

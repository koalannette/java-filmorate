package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Slf4j
@RestController
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film createFilm(@Validated @RequestBody Film film) {
        log.info("Поступил запрос на добавление фильма.");
        return filmService.createFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@Validated @RequestBody Film film) {
        log.info("Поступил запрос на изменения фильма.");
        return filmService.updateFilm(film);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Запрос всех фильмов.");
        return filmService.getFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable Integer id) {
        log.info("Поступил запрос на получение фильма");
        return filmService.getFilmById(id);
    }

    @PutMapping("/films/{id}/like/{filmId}")
    public void like(@PathVariable Integer id, @PathVariable Integer filmId) {
        log.info("Поступил запрос на добавление лайка фильму.");
        filmService.like(id, filmId);
    }

    @DeleteMapping("/films/{id}/like/{filmId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer filmId) {
        log.info("Поступил запрос на удаление лайка у фильма.");
        filmService.deleteLike(filmId, id);
    }

    @GetMapping("/films/popular")
    public List<Film> getBestFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Поступил запрос на получение популярных фильмов.");
        return filmService.showPopularFilms(count);
    }
}



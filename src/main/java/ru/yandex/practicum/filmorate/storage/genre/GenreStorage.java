package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {

    List<Genre> getGenres();

    Genre getGenreById(int genreId);

    List<Genre> getGenresByFilmId(long filmId);

    void addGenre(Film film, Set<Genre> genres);

}

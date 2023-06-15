package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmDbStorageTest {

    @Autowired
    private final FilmDbStorage filmDbStorage;
    private final UserStorage userStorage;

    private Film start() {
        Film film = new Film();
        film.setName("Film 1");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.of(2023, 01, 10));
        film.setDuration(100);
        film.setMpa(new Mpa(1, "G", "у фильма нет возрастных ограничений"));
        return film;
    }

    private User createTestUser() {
        User user = new User();
        user.setEmail("a@.ru");
        user.setLogin("alex68");
        user.setName("Alex");
        user.setBirthday(LocalDate.of(1990, 1, 10));
        return user;
    }

    private User createTestUser2() {
        User user = new User();
        user.setEmail("i@.ru");
        user.setLogin("ivan68");
        user.setName("Ivan");
        user.setBirthday(LocalDate.of(1995, 4, 6));
        return user;
    }

    @Test
    void addAndGetTest() {
        Film testFilm = start();
        filmDbStorage.createFilm(testFilm);
        assertEquals(1, testFilm.getId());
        assertEquals("Film 1", testFilm.getName());
        assertEquals("Film Description", testFilm.getDescription());
        assertEquals(LocalDate.of(2023, 01, 10), testFilm.getReleaseDate());
    }

    @Test
    void getFilmsTest() {
        Film testFilm = start();
        filmDbStorage.createFilm(testFilm);
        List<Film> filmsList = List.of(testFilm);

        assertEquals(filmsList.toString(), filmDbStorage.getFilms().toString());
    }

    @Test
    void findFilmByIdTest() {
        Film testFilm = start();
        filmDbStorage.createFilm(testFilm);

        Optional<Film> filmOptional = Optional.of(filmDbStorage.getFilmById(testFilm.getId()));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    void updateFilmTest() {
        Film testFilm = start();
        filmDbStorage.createFilm(testFilm);
        testFilm.setName("Super New Film");
        Film newFilm = filmDbStorage.updateFilm(testFilm);
        assertEquals(newFilm, filmDbStorage.getFilmById(testFilm.getId()));
    }


}


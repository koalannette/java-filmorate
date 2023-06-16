package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@NoArgsConstructor
public class Film {

    private long id;
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;
    @NotBlank
    @Length(max = 200, message = "Описание к фильму не может превышать 200 символов!")
    private String description;
    private LocalDate releaseDate;
    @NonNull
    @Positive
    private int duration;
    private Mpa mpa;
    private Set<Genre> genres = new TreeSet<>(Comparator.comparingLong(Genre::getId));
    private Set<Integer> likes = new HashSet<>();

    @JsonSetter
    public void setGenres(Set<Genre> genres) {
        this.genres.clear();
        this.genres.addAll(genres);
    }
}

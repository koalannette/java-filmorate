package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Film {

    private long id;
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;
    @NotBlank
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    private int duration;
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();
    @NonNull
    private Set<Integer> likes = new HashSet<>();
}

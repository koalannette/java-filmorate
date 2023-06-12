package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class Mpa {

    private int id;
    @NotBlank
    private String type;
    @NotBlank
    private String description;
}

package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mpa {

    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;

}

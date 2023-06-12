package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class User {

    private long id;
    @Email(message = "Почта некорректная.")
    @NotBlank(message = "Почта не может быть null.")
    private String email;
    @NotBlank(message = "Логин не может быть null.")
    private String login;
    private String name;
    @NonNull
    @Past
    private LocalDate birthday;
    @NonNull
    private Set<Integer> friends = new HashSet<>();

}

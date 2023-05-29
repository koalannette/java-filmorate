package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private int id;
    @Email
    @NonNull
    private String email;
    @NotBlank
    @NonNull
    private String login;
    @NonNull
    private String name;
    @NonNull
    @Past
    private LocalDate birthday;
    @NonNull
    private Set<Integer> friends = new HashSet<>();

}

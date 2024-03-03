package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Set<Integer> friends;
    @Min(value = 1, message = "Невозможно найти пользователем с указанным ID")
    private Integer id;
    @Email(message = "Некорректно введён email пользователя")
    private String email;
    @NotEmpty(message = "Некорректно введён логин")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}

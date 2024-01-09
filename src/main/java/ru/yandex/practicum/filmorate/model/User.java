package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
public class User {
Integer id;
    @Email(message = "Некорректно введён email пользователя")
String email;
    @NotEmpty(message = "Некорректно введён логин")
String login;
String name; //не смог подобрать нужную аннотацию
    @Past
LocalDate birthday;

}

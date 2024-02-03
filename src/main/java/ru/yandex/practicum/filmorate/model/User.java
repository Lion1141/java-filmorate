package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Set<Integer> friends = new HashSet<>();
    private Integer id;
    @Email(message = "Некорректно введён email пользователя")
    private String email;
    @NotEmpty(message = "Некорректно введён логин")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;

    public void addFriend(User user) {
        this.friends.add(user.id);
    }

}

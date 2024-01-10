package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@RestController
@Slf4j
public class UserController {
    protected final HashMap<Integer, User> users = new HashMap<>();
    protected Integer id = 1;

    @GetMapping("/users")
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(id++);
        int userId = user.getId();
        users.put(userId, user);
        log.info("Пользователь успешно добавлен: " + user.getName());
        return user;
    }

    @PutMapping("/users")
    public User updateUsers(@Valid @RequestBody User updatedUser) {
        if (!users.containsKey(updatedUser.getId())) {
            throw new RuntimeException("Неизвестный фильм");
        }
            if (updatedUser.getName().isBlank() || updatedUser.getName() == null) {
                updatedUser.setName(updatedUser.getLogin());
            }
        if (!users.containsKey(updatedUser.getId())) {
            throw new RuntimeException("Не удалось обновить пользователя");
        }
        updatedUser.setName(updatedUser.getName());
        updatedUser.setEmail(updatedUser.getEmail());
        updatedUser.setBirthday(updatedUser.getBirthday());
        users.put(updatedUser.getId(), updatedUser);
        log.info("Пользователь успешно обновлён: " + updatedUser.getName());
        return updatedUser;
    }
}
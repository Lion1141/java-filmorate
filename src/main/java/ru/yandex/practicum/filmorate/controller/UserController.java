package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
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
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        try {
            if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                throw new ValidationException("Некорректно введён email пользователя");
            } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
                throw new ValidationException("Некорректно введён логин");
            } else if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Дата рождения не может быть в будущем");
            }
                user.setId(id++);
                int userId = user.getId();
                users.put(userId, user);
            log.info("Пользователь успешно добавлен: " + user.getName());
            return user;
        } catch (Exception e) {
            log.error("Ошибка при добавлении пользователя: " + e.getMessage());
            throw e;
        }
    }

    @PutMapping("/users")
    public User updateUsers(@Valid @RequestBody User updatedUser) throws ValidationException {
        try {
            if (updatedUser.getName().isBlank() || updatedUser.getName() == null) {
                updatedUser.setName(updatedUser.getLogin());
            } else {
                if(!users.containsKey(updatedUser.getId())) {
                    throw new ValidationException("не удалось обновить пользователя");
                }
                updatedUser.setName(updatedUser.getName());
                updatedUser.setEmail(updatedUser.getEmail());
                updatedUser.setBirthday(updatedUser.getBirthday());
                users.put(updatedUser.getId(), updatedUser);
                log.info("Пользователь успешно обновлён: " + updatedUser.getName());
                return updatedUser;
            }
            throw new ValidationException("Не удалось обновить пользователя");
        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя: " + e.getMessage());
            throw e;
        }
    }
}
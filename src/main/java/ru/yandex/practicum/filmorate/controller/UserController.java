package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController("")
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        log.info("Поступил запрос на получение списка пользователей.");
        return userStorage.getUsers();
    }

    @PostMapping
    public Optional<User> createUser(@Valid @RequestBody User user) {
        log.info("Поступил запрос на создание пользователя.");
        return userStorage.createUser(user);
    }

    @PutMapping
    public User updateUsers(@Valid @RequestBody User updatedUser) {
        log.info("Поступил запрос на обновление пользователя.");
        return userStorage.updateUser(updatedUser);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@Valid @PathVariable Integer userId) {
        userStorage.deleteUser(userId);
    }

    @GetMapping("{id}")
    public Optional<User> findById(@Valid @PathVariable Integer id) {
        log.info("Поступил запрос на получение пользователя по id.");
        Optional<User> user = userStorage.findById(id);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Невозможно найти пользователя с указанным ID");
        }
        return user;
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@Valid @PathVariable Integer id, @Valid @PathVariable Integer friendId) {
        log.info("Поступил запрос на добавление в список друзей.");
        if (id < 1 || friendId < 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Невозможно найти пользователя с указанным ID");
        }
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Поступил запрос на удаление из списка друзей.");
        userService.deleteFriend(id, friendId);
        userService.deleteFriend(friendId, id);
    }

    @GetMapping("{id}/friends")
    public Collection<User> getFriends(@PathVariable Integer id) {
        log.info("Поступил запрос на получение списка друзей.");
        return userService.getUserFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCrossFriend(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Поступил запрос на получение списка общих друзей.");
        return userService.getUserCrossFriends(id, otherId);
    }
}
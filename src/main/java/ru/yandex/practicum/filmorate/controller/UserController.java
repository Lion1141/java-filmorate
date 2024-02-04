package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController("")
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUsers(@Valid @RequestBody User updatedUser) {
        return userService.updateUser(updatedUser);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@Valid @PathVariable Integer userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("{id}")
    public Optional<User> findById(@Valid @PathVariable Integer id) {
        var user = userService.findById(id);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Невозможно найти пользователя с указанным ID");
        }
        return user;
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@Valid @PathVariable Integer id, @Valid @PathVariable Integer friendId) {
        if (friendId < 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Невозможно найти пользователя с указанным ID");
        }
        userService.addFriend(id, friendId);
        userService.addFriend(friendId, id);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.removeFriend(id, friendId);
        userService.removeFriend(friendId, id);
    }

    @GetMapping("{id}/friends")
    public Collection<User> getFriends(@PathVariable Integer id) {
        return userService
                .getFriends(id)
                .stream()
                .sorted(Comparator.comparing(user -> user.getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<User> getCrossFriend(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.getCrossFriends(id, otherId);
    }
}
package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storages.UserStorage;
import ru.yandex.practicum.filmorate.storages.memory.InMemoryUserStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController controller;
    UserStorage userStorage;
    UserService userService;
    User testUser;

    @BeforeEach
    protected void init() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        controller = new UserController(userStorage, userService);

        testUser = User.builder()
                .name("John")
                .email("john@mail.ru")
                .login("login")
                .birthday(LocalDate.of(1987, 4, 14))
                .build();

    }

    @Test
    public void createUser_NameIsBlank_NameIsLoginTest() {
        testUser.setName("");
        controller.createUser(testUser);
        assertEquals("login", controller.getUsers().get(0).getName());
    }

    @Test
    void createUser_BirthdayInFuture_badRequestTest() {
        testUser.setBirthday(LocalDate.parse("2024-10-12"));
        try {
            controller.createUser(testUser);
        } catch (ValidationException e) {
            assertEquals("Неверно указана дата рождения", e.getMessage());
        }
    }
}
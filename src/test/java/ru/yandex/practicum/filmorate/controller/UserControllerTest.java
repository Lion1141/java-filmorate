package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storages.user.UserDbStorage;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest extends AbstractControllerTest {
    UserController controller;
    @Autowired
    UserDbStorage userStorage;
    UserService userService;
    User testUser;

    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @BeforeEach
    protected void init() {
        userService = new UserService(userStorage);
        controller = new UserController(userService);

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
        var violations = validator.validate(testUser);
        assertEquals(1, violations.size());
    }
}
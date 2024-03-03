package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest

public class AbstractControllerTest {
    @Autowired
    protected FilmService filmService;

    @Autowired
    protected UserService userService;

}
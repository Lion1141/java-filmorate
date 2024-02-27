package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storages.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storages.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storages.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storages.user.UserDbStorage;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest extends AbstractControllerTest {

    @Autowired
    private FilmController controller;
    @Autowired
    private FilmDbStorage filmStorage;
    @Autowired
    private MpaDbStorage mpaDbStorage;
    @Autowired
    private GenreDbStorage genreDbStorage;
    @Autowired
    private UserDbStorage userStorage;
    @Autowired
    private FilmService filmService;
    private Film testFilm;
    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @BeforeEach
    protected void init() {
        filmService = new FilmService(filmStorage, mpaDbStorage, genreDbStorage, userStorage);
        controller = new FilmController(filmService);
        testFilm = Film.builder()
                .id(1)
                .name("Тестовый фильм")
                .description("Тестовое описание тестового фильма")
                .releaseDate(LocalDate.of(1999, 12, 27))
                .duration(87)
                .build();

        testFilm.setGenres(new HashSet<>());
        testFilm.setLikes(new HashSet<>());
        testFilm.setMpa(Mpa.builder()
                .id(1)
                .name("NC-17")
                .build());
    }

    @Test
    void createNewCorrectFilm_isOkTest() {
        Optional<Film> film = controller.addFilm(testFilm);
        assertEquals(film, filmService.findById(2));
    }

    @Test
    void createFilm_NameIsBlank_badRequestTest() {
        testFilm.setName("");
        try {
            controller.addFilm(testFilm);
        } catch (ValidationException e) {
            assertEquals("Некорректно указано название фильма.", e.getMessage());
        }
    }


    @Test
    void createFilm_IncorrectDescription_badRequestTest() {
        testFilm.setDescription("Размер описания значительно превышает двести символов, а может и не превышает " +
                "(надо посчитать). Нет, к сожалению размер описания фильма сейчас не превышает двести символов," +
                "но вот сейчас однозначно стал превышать двести символов!");

        var violations = validator.validate(testFilm);
        assertEquals(1, violations.size());
    }

    @Test
    void createFilm_RealiseDateInFuture_badRequestTest() {
        testFilm.setReleaseDate(LocalDate.of(2033, 4, 14));
        try {
            controller.addFilm(testFilm);
        } catch (ValidationException e) {
            assertEquals("Некорректно указана дата релиза.", e.getMessage());
        }
    }

    @Test
    void createFilm_RealiseDateBeforeFirstFilmDate_badRequestTest() {
        testFilm.setReleaseDate(LocalDate.of(1833, 4, 14));
        try {
            controller.addFilm(testFilm);
        } catch (ValidationException e) {
            assertEquals("Некорректно указана дата релиза.", e.getMessage());
        }
    }

}
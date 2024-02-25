package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storages.FilmStorage;
import ru.yandex.practicum.filmorate.storages.memory.InMemoryFilmStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest extends AbstractControllerTest {

    @Autowired
    private FilmController controller;
    private FilmStorage filmStorage;

    private FilmService filmService;
    private Film testFilm;

    @BeforeEach
    protected void init() {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage);
        controller = new FilmController(filmStorage, filmService);
        testFilm = Film.builder()
                .id(1)
                .name("Тестовый фильм")
                .description("Тестовое описание тестового фильма")
                .releaseDate(LocalDate.of(1999, 12, 27))
                .duration(87)
                .build();
    }

    @Test
    void createNewCorrectFilm_isOkTest() {
        Optional<Film> film = controller.addFilm(testFilm);
        assertEquals(film, filmStorage.findById(1));
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
        try {
            controller.addFilm(testFilm);
        } catch (ValidationException e) {
            assertEquals("Превышено количество символов в описании фильма.", e.getMessage());
        }
    }

    @Test
    void createFilm_RealiseDateInFuture_badRequestTest(){
        testFilm.setReleaseDate(LocalDate.of(2033, 4, 14));
        try {
            controller.addFilm(testFilm);
        } catch (ValidationException e) {
            assertEquals("Некорректно указана дата релиза.", e.getMessage());
        }
    }

    @Test
    void createFilm_RealiseDateBeforeFirstFilmDate_badRequestTest(){
        testFilm.setReleaseDate(LocalDate.of(1833, 4, 14));
        try {
            controller.addFilm(testFilm);
        } catch (ValidationException e) {
            assertEquals("Некорректно указана дата релиза.", e.getMessage());
        }
    }

}
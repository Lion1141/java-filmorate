package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class FilmControllerTest extends AbstractControllerTest{
    @Autowired
    private FilmController filmController;

    @Test
    public void testCreateFilm() {
        Film film = Film.builder()
                .id(1)
                .name("Inception")
                .description("A heist movie that takes place within the architecture of the mind")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .duration(148L)
                .build();
        Film addedFilm = filmController.addFilm(film);
        assertEquals(film, addedFilm);
    }

    @Test
    public void testUpdateFilm() {
        Film existingFilm = Film.builder()
                .id(2)
                .name("Inception")
                .description("A heist movie that takes place within the architecture of the mind")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .duration(148L)
                .build();
        filmController.addFilm(existingFilm);

        Film updatedFilm = Film.builder()
                .id(existingFilm.getId())
                .name("Inception: Special Edition")
                .description("A heist movie that takes place within the architecture of the mind, now with extra scenes")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .duration(148L)
                .build();
        Film newFilm = filmController.updateFilm(updatedFilm);
        assertEquals(updatedFilm, newFilm);
    }
        @Test
        public void testDeleteFilm() {
            FilmService filmService = mock(FilmService.class);
            FilmController filmController = new FilmController(filmService);
            filmController.deleteFilm(1);
            verify(filmService).deleteFilm(1);
        }

    @Test
    public void testGetFilmWithValidId() {
        Film existingFilm3 = Film.builder()
                .id(3)
                .name("Название фильма")
                .description("Описание фильма")
                .releaseDate(LocalDate.of(2005, 7, 16))
                .duration(148L)
                .build();
        filmController.addFilm(existingFilm3);
        FilmService filmService = mock(FilmService.class);
        FilmController filmController = new FilmController(filmService);
        when(filmService.findById(1)).thenReturn(Optional.of(existingFilm3));
        Optional<Film> result = filmController.getFilm(1);
        verify(filmService).findById(1);
        assertTrue(result.isPresent());
        assertEquals("Название фильма", result.get().getName());
        assertEquals("Описание фильма", result.get().getDescription());
    }

    @Test
    public void testGetFilmWithInvalidId() {
        FilmService filmService = mock(FilmService.class);
        FilmController filmController = new FilmController(filmService);
        when(filmService.findById(10)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> {
            filmController.getFilm(10);
        });
        verify(filmService).findById(10);
    }

    @Test
    public void testGetPopular() {
        // Создание заглушки для filmService
        FilmService filmService = mock(FilmService.class);
        FilmController filmController = new FilmController(filmService);
        Film film1 = Film.builder()
                .id(1)
                .name("Inception")
                .description("A heist movie that takes place within the architecture of the mind")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .duration(148L)
                .build();
        filmController.addFilm(film1);
        Film film2 = Film.builder()
                .id(2)
                .name("Inception")
                .description("A heist movie that takes place within the architecture of the mind")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .duration(148L)
                .build();
        filmController.addFilm(film2);
        Film film3 = Film.builder()
                .id(3)
                .name("Название фильма")
                .description("Описание фильма")
                .releaseDate(LocalDate.of(2005, 7, 16))
                .duration(148L)
                .build();
        filmController.addFilm(film3);
        Film film4 = Film.builder()
                .id(4)
                .name("Фильм 4")
                .description("Описание фильма 4")
                .releaseDate(LocalDate.of(2004, 7, 16))
                .duration(148L)
                .build();
        filmController.addFilm(film4);
        Film film5 = Film.builder()
                .id(5)
                .name("Фильм 5")
                .description("Описание фильма 5")
                .releaseDate(LocalDate.of(2003, 7, 16))
                .duration(148L)
                .build();
        filmController.addFilm(film5);

        // Установка поведения заглушки для метода getMostPopular
        when(filmService.getMostPopular(5)).thenReturn(Arrays.asList(
                film1,
                film2,
                film3,
                film4,
                film5
        ));
        Collection<Film> result = filmController.getPopular(5);
        verify(filmService).getMostPopular(5);
        assertEquals(5, result.size());
    }
    @Test
    public void testAddLike() {
        FilmService filmService = mock(FilmService.class);
        FilmController filmController = new FilmController(filmService);
        filmController.addLike(1, 123);
        verify(filmService).addLike(1, 123);
    }

    @Test
    public void testRemoveLike() {
        FilmService filmService = mock(FilmService.class);
        FilmController filmController = new FilmController(filmService);
        filmController.removeLike(1, 123);
        verify(filmService).removeLike(1, 123);
    }

    @Test
    public void testGetFilms() {
        FilmService filmService = mock(FilmService.class);
        FilmController filmController = new FilmController(filmService);
        Film film1 = Film.builder()
                .id(1)
                .name("Inception")
                .description("A heist movie that takes place within the architecture of the mind")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .duration(148L)
                .build();
        filmController.addFilm(film1);
        Film film2 = Film.builder()
                .id(2)
                .name("Inception")
                .description("A heist movie that takes place within the architecture of the mind")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .duration(148L)
                .build();
        filmController.addFilm(film2);
        Film film3 = Film.builder()
                .id(3)
                .name("Название фильма")
                .description("Описание фильма")
                .releaseDate(LocalDate.of(2005, 7, 16))
                .duration(148L)
                .build();
        filmController.addFilm(film3);
        when(filmService.getFilms()).thenReturn(Arrays.asList(
                film1,
                film2,
                film3
        ));
        Collection<Film> result = filmController.getFilms();
        verify(filmService).getFilms();
        assertEquals(3, result.size());
    }
}



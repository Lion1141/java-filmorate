package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest extends FilmController {
    @Test
    public void testCreateFilm() {
        FilmController filmController = new FilmController();
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
        FilmController filmController = new FilmController();
        Film existingFilm = Film.builder()
                .id(1)
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
}


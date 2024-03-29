package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Поступил запрос на получение списка всех фильмов.");
        return filmService.getFilms();
    }

    @PostMapping()
    public Optional<Film> addFilm(@Valid @RequestBody Film film) {
        log.info("Поступил запрос на добавление фильма.");
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film updatedFilm) {
        log.info("Поступил запрос на изменения фильма.");
        return filmService.updateFilm(updatedFilm);
    }

    @DeleteMapping("{filmId}")
    public void deleteFilm(@Valid @PathVariable Integer filmId) {
        log.info("Поступил запрос на удаление фильма.");
        filmService.deleteFilm(filmId);
    }

    @GetMapping("{id}")
    public Optional<Film> getFilm(@Valid @PathVariable Integer id) {
        log.info("Получен GET-запрос на получение фильма");
        Optional<Film> film = filmService.findById(id);

        if (film.isEmpty()) {
            throw new NotFoundException("Невозможно найти фильм с указанным ID");
        }
        return film;
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@Valid @PathVariable Integer id, @Valid @PathVariable Integer userId) {
        log.info("Поступил запрос на присвоение лайка фильму.");
        filmService.like(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLike(@Valid @PathVariable Integer id, @Valid @PathVariable Integer userId) {
        log.info("Поступил запрос на удаление лайка у фильма.");
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@Valid @RequestParam(defaultValue = "10", name = "count") Integer count) {
        log.info("Поступил запрос на получение списка популярных фильмов.");
        return filmService.getMostPopular(count);
    }
}
package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@Slf4j
public class FilmController {

    protected final HashMap<Integer, Film> films = new HashMap<>();
    protected Integer id = 1;
    LocalDate cutoffDate = LocalDate.of(1895, 12, 28);

    @GetMapping("/films")
    public Collection<Film> getUsers() {
        return films.values();
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        try {
            if (film.getName().isBlank()) {
                throw new ValidationException("Название фильма не должно быть пустым");
            } else if (film.getDescription().length() > 200) {
                throw new ValidationException("Описание фильма должно быть меньше 200 знаков");
            } else if (film.getReleaseDate().isBefore(cutoffDate)) {
                throw new ValidationException("Некорректно введена дата релиза фильма");
            } else if (film.getDuration() < 0) {
                throw new ValidationException("Продолжительность фильма не может быть отрицательной");
            } else {
                film.setId(id++);
                int filmId = film.getId();
                films.put(filmId, film);
            }
            log.info("Фильм успешно добавлен: " + film.getName());
            return film;
        } catch (Exception e) {
            log.error("Ошибка при добавлении фильма: " + e.getMessage());
            throw e;
        }
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film updatedFilm) throws ValidationException {
        try {
            if (updatedFilm.getName().isBlank()) {
                throw new ValidationException("Название фильма не должно быть пустым");
            } else if (updatedFilm.getDescription().length() > 200) {
                throw new ValidationException("Описание фильма должно быть меньше 200 знаков");
            } else if (updatedFilm.getReleaseDate().isBefore(cutoffDate)) {
                throw new ValidationException("Некорректно введена дата релиза фильма");
            } else if (updatedFilm.getDuration() < 0) {
                throw new ValidationException("Продолжительность фильма не может быть отрицательной");
            } else if (!films.containsKey(updatedFilm.getId())) {
                throw new ValidationException("Ошибка добавления фильма");
            }
            updatedFilm.setName(updatedFilm.getName());
            updatedFilm.setDescription(updatedFilm.getDescription());
            updatedFilm.setReleaseDate(updatedFilm.getReleaseDate());
            updatedFilm.setDuration(updatedFilm.getDuration());
            films.put(updatedFilm.getId(), updatedFilm);
            log.info("Фильм успешно обновлен: " + updatedFilm.getName());
            return updatedFilm;
        } catch (Exception e) {
            log.error("Ошибка при обновлении фильма: " + e.getMessage());
            throw e;
        }
    }
}

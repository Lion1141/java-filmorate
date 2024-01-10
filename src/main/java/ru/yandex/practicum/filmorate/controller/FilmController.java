package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@RestController
@Slf4j
public class FilmController {
    protected final HashMap<Integer, Film> films = new HashMap<>();
    protected Integer id = 1;

    @GetMapping("/films")
    public Collection<Film> getFilm() {
        return films.values();
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(id++);
        int filmId = film.getId();
        films.put(filmId, film);
        log.info("Фильм успешно добавлен: " + film.getName());
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film updatedFilm) {
        if(!films.containsKey(updatedFilm.getId())) {
            throw new RuntimeException("Неизвестный фильм");
        }
        updatedFilm.setName(updatedFilm.getName());
        updatedFilm.setDescription(updatedFilm.getDescription());
        updatedFilm.setReleaseDate(updatedFilm.getReleaseDate());
        updatedFilm.setDuration(updatedFilm.getDuration());
        films.put(updatedFilm.getId(), updatedFilm);
        log.info("Фильм успешно обновлен: " + updatedFilm.getName());
        return updatedFilm;
    }
}

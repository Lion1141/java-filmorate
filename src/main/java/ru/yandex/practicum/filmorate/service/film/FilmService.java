package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.FilmStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public Optional<Film> like(int filmId, int userId) {
        return filmStorage.like(filmId, userId);
    }

    public Optional<Film> deleteLike(int filmId, int userId) {
        return filmStorage.deleteLike(filmId, userId);
    }
}
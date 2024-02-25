package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> addFilm(Film film);

    Collection<Film> getFilms();

    Film updateFilm(Film updatedFilm) throws RuntimeException;

    void deleteFilm(Integer filmId);

    Optional<Film> like(int filmId, int userId);

    Optional<Film> deleteLike(int filmId, int userId);

    Collection<Film> getMostPopular(Integer count);

    Optional<Film> findById(Integer filmId);
}

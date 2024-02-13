package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film addFilm(Film film);

    Collection<Film> getFilms();

    Film updateFilm(Film updatedFilm) throws RuntimeException;

    void deleteFilm(Integer filmId);

    List<Film> getMostPopular(Integer count);

    Optional<Film> findById(Integer filmId);
}

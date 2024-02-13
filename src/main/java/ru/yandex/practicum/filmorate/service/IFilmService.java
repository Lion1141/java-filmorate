package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IFilmService {

    Film addFilm(Film film);

    Collection<Film> getFilms();

    Film updateFilm(Film updatedFilm) throws RuntimeException;

    Optional<Film> findById(Integer filmId);

    void deleteFilm(Integer filmId);

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    List<Film> getMostPopular(Integer count);
}

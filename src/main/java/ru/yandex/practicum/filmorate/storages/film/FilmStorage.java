package ru.yandex.practicum.filmorate.storages.film;

import ru.yandex.practicum.filmorate.dao.FilmDao;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Optional<FilmDao> addFilm(FilmDao film);

    Collection<FilmDao> getFilms();

    FilmDao updateFilm(FilmDao updatedFilm) throws RuntimeException;

    void deleteFilm(Integer filmId);

    Optional<FilmDao> like(int filmId, int userId);

    Optional<FilmDao> deleteLike(int filmId, int userId);

    Collection<FilmDao> getMostPopular(Integer count);

    Optional<FilmDao> findById(Integer filmId);
}

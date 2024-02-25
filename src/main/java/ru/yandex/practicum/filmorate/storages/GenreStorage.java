package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreStorage {

    Collection<Genre> getAll(); //получение списка жанров

    Collection<Genre> getGenreForCurrentFilm(int filmId); //получение списка жанров определённого фильма

    void addGenresForCurrentFilm(Film film); //присвоение жанра фильму

    void updateGenresForCurrentFilm(Film film); //изменение жанра фильма

    Optional<Genre> getGenreForId(int genreId); //получение жанра по id
}

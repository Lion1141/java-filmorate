package ru.yandex.practicum.filmorate.storages.mpa;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

public interface MpaStorage {
    Collection<Mpa> getAll(); //получение списка рейтингов MPA

    Optional<Mpa> getMpaForId(int mpaId); //получение рейтинга MPA по ID

    void addMpaToFilm(Film film);   //присвоение рейтингов в фильме
}

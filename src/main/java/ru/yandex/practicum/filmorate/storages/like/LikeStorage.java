package ru.yandex.practicum.filmorate.storages.like;

import java.util.Collection;

public interface LikeStorage {
    Collection<Integer> getLikesForCurrentFilm(int id); //получение лайков по у фильма по id фильма
}

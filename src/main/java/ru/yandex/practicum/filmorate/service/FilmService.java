package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService implements IFilmService {
    @Autowired
    private final FilmStorage storage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage storage, UserService userService) {
        this.storage = storage;
        this.userService = userService;
    }

    @Override
    public Film addFilm(Film film) {
        Film newFilm = storage.addFilm(film);
        log.info("Фильм {} успешно добавлен.", film.getName());
        return newFilm;
    }

    @Override
    public Collection<Film> getFilms() {
        return storage.getFilms();
    }

    @Override
    public Film updateFilm(Film updatedFilm) throws RuntimeException {
        log.info("Обновлён фильм: {}", updatedFilm);
        return storage.updateFilm(updatedFilm);
    }

    @Override
    public Optional<Film> findById(Integer filmId) {
        return storage.findById(filmId);
    }

    @Override
    public void deleteFilm(Integer filmId) {
        storage.deleteFilm(filmId);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        Film film = storage.findById(filmId).get();
        User user = userService.findById(userId).get();

        film.addLike(userId);
        storage.updateFilm(film);
        log.info("User: {} was like film: {}", user, film);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        if (filmId < 1 || userId < 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти пользователя или фильм");
        }
        Film film = storage.findById(filmId).get();
        User user = userService.findById(userId).get();
        film.removeLike(userId);
        storage.updateFilm(film);
        log.info("User: {} was like film: {}", user, film);
    }

    @Override
    public List<Film> getMostPopular(Integer count) {
        return storage.getMostPopular(count);
    }
}
package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.film.FilmStorage;
import ru.yandex.practicum.filmorate.storages.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storages.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storages.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final UserStorage userStorage;

    public Optional<Film> createFilm(Film film) {
        FilmDao filmDao = map(film);
        Optional<FilmDao> result = filmStorage.addFilm(filmDao);
        film.setId(result.get().getId());
        mpaDbStorage.addMpaToFilm(film);
        genreDbStorage.addGenreNameToFilm(film);
        genreDbStorage.addGenresForCurrentFilm(film);
        if(result.isEmpty())
            return Optional.empty();
        return Optional.of(map(result.get()));
    }

    public Optional<Film> like(int filmId, int userId) {
        if (userStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден.");
        }

        var result = filmStorage.like(filmId, userId);

        if(result.isEmpty())
            return Optional.empty();

        return Optional.of(map(result.get()));
    }

    public Collection<Film> getFilms(){
        return filmStorage
                .getFilms()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public Optional<Film> deleteLike(int filmId, int userId) {
        if (userStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден.");
        }

        var result = filmStorage.deleteLike(filmId, userId);

        if(result.isEmpty())
            return Optional.empty();

        return Optional.of(map(result.get()));
    }

    public Film updateFilm(Film updatedFilm){
        filmStorage.updateFilm(map(updatedFilm));
        mpaDbStorage.addMpaToFilm(updatedFilm);
        genreDbStorage.updateGenresForCurrentFilm(updatedFilm);
        genreDbStorage.addGenreNameToFilm(updatedFilm);
        updatedFilm.setGenres(genreDbStorage.getGenreForCurrentFilm(updatedFilm.getId()));

        return updatedFilm;
    }

    public List<Film> getMostPopular(Integer count){
        return filmStorage
                .getMostPopular(count)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public void deleteFilm(Integer filmId){
        filmStorage.deleteFilm(filmId);
    }

    public Optional<Film> findById(Integer filmId){
        var result = filmStorage.findById(filmId);

        if(result.isEmpty())
            return Optional.empty();

        return Optional.of(map(result.get()));
    }

    private Film map(FilmDao filmDao) {
        var mpa = mpaDbStorage.getMpaForId(filmDao.mpa_id);
        var genres = genreDbStorage.getGenreForCurrentFilm(filmDao.id);
        Film film = Film.builder()
                .id(filmDao.id)
                .name(filmDao.name)
                .description(filmDao.description)
                .releaseDate(filmDao.releaseDate)
                .duration(filmDao.duration)
                .mpa(mpa.get())
                .genres(genres)
                .build();
        validate(film);
        return film;
    }

    public static FilmDao map(Film filmDao){
        FilmDao film = FilmDao.builder()
                .id(filmDao.getId())
                .name(filmDao.getName())
                .description(filmDao.getDescription())
                .releaseDate(filmDao.getReleaseDate())
                .duration(filmDao.getDuration())
                .mpa_id(filmDao.getMpa().getId())
                .build();

        return film;
    }

    public void validate(Film film) throws ValidationException {

    }

}
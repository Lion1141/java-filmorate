package ru.yandex.practicum.filmorate.storages.memory;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Data
@Component
public class InMemoryFilmStorage implements FilmStorage {

    protected final HashMap<Integer, Film> films = new HashMap<>();
    protected Integer id = 1;

    @Override
    public Film addFilm(Film film) {
        if (film.getId() == null) {
            film.setId(id++);
        }
        if (!films.containsKey(film.getId())) {
            int filmId = film.getId();
            films.put(filmId, film);
            return film;
        } else {
            throw new RuntimeException(String.format("Фильм с id {} уже существует", film.getId()));
        }
    }

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film updateFilm(Film updatedFilm) throws RuntimeException {
        if (!films.containsKey(updatedFilm.getId())) {
            log.debug("Ошибка обновления фильма с ID: {}", updatedFilm.getId());
            throw new RuntimeException("Неизвестный фильм");
        }
        updatedFilm.setName(updatedFilm.getName());
        updatedFilm.setDescription(updatedFilm.getDescription());
        updatedFilm.setReleaseDate(updatedFilm.getReleaseDate());
        updatedFilm.setDuration(updatedFilm.getDuration());
        films.put(updatedFilm.getId(), updatedFilm);
        return updatedFilm;
    }

    @Override
    public void deleteFilm(Integer filmId) {
        if (films.containsKey(filmId)) {
            films.remove(filmId);
        } else {
            log.debug("Ошибка удаления фильма с ID: {}", filmId);
            throw new RuntimeException("Неизвестный фильм");
        }
    }

    @Override
    public List<Film> getMostPopular(Integer count) {
        return films.values().stream().sorted(Comparator.comparingInt(f -> -f.getLikes().size()))
                .limit(count).collect(Collectors.toList());
    }

    @Override
    public Optional<Film> findById(Integer filmId) {
        if (films.containsKey(filmId)) {
            return Optional.of(films.get(filmId));
        } else {
            return Optional.empty();
        }
    }
}

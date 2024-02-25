package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storages.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreDbStorage;

    public Optional<Genre> getGenre(int genreId) {
        return genreDbStorage.getGenreForId(genreId);
    }

    public Collection<Genre> findAll() {
        return genreDbStorage.getAll();
    }
}
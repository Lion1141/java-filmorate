package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storages.mpa.MpaStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaDbStorage;

    public Collection<Mpa> findAll() {
        return mpaDbStorage.getAll();
    }

    public Optional<Mpa> getMpaRating(int ratingMpaId) {
        return mpaDbStorage.getMpaForId(ratingMpaId);
    }
}

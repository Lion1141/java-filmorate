package ru.yandex.practicum.filmorate.storages.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.FilmStorage;
import ru.yandex.practicum.filmorate.storages.LikeStorage;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Primary
@Slf4j
@RequiredArgsConstructor
@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;
    private final MpaDbStorage mpaDbStorage;
    private final LikeStorage likeDbStorage;
    private final GenreDbStorage genreDbStorage;

    @Override
    public Optional<Film> addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(toMap(film)).intValue());
        mpaDbStorage.addMpaToFilm(film);
        genreDbStorage.addGenreNameToFilm(film);
        genreDbStorage.addGenresForCurrentFilm(film);
        log.info("Фильм добавлен.");
        return Optional.of(film);
    }

    @Override
    public Collection<Film> getFilms() {
        List<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT film_id, name, description, release_date," +
                " duration, mpa_id FROM FILMS");
        while (filmRows.next()) {
            Film film = Film.builder()
                    .id(filmRows.getInt("film_id"))
                    .name(filmRows.getString("name"))
                    .description(filmRows.getString("description"))
                    .releaseDate(Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate())
                    .duration(filmRows.getInt("duration"))
                    .mpa(mpaDbStorage.getMpaForId(filmRows.getInt("mpa_id")).orElse(null))
                    .build();
            film.setGenres(genreDbStorage.getGenreForCurrentFilm(film.getId()));
            film.setLikes(likeDbStorage.getLikesForCurrentFilm(film.getId()));

            films.add(film);
        }
        return films;
    }

    @Override
    public Film updateFilm(Film updatedFilm) throws RuntimeException {
        String sqlQuery = "UPDATE FILMS SET " +
                "name=?, description=?, release_date=?, duration=?, mpa_id=? WHERE film_id=?";
        int rowsCount = jdbcTemplate.update(sqlQuery, updatedFilm.getName(), updatedFilm.getDescription(),
                updatedFilm.getReleaseDate(), updatedFilm.getDuration(), updatedFilm.getMpa().getId(), updatedFilm.getId());
        mpaDbStorage.addMpaToFilm(updatedFilm);
        genreDbStorage.updateGenresForCurrentFilm(updatedFilm);
        genreDbStorage.addGenreNameToFilm(updatedFilm);
        updatedFilm.setGenres(genreDbStorage.getGenreForCurrentFilm(updatedFilm.getId()));
        if (rowsCount > 0) {
            return updatedFilm;
        }
        throw new NotFoundException("Фильм не найден.");
    }

    @Override
    public void deleteFilm(Integer filmId) {
        Optional<Film> film = findById(filmId);
        if (film.isPresent()) {
            String sqlQuery =
                    "DELETE " +
                            "FROM films " +
                            "WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery, filmId);
            log.info("Фильм удалён");
        } else {
            throw new NotFoundException("Фильм не найден.");
        }
    }

    @Override
    public Optional<Film> like(int filmId, int userId) {
        Optional<Film> film = findById(filmId);
        String sqlQuery = "INSERT INTO LIKES (film_id, user_id) VALUES(?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return film;
    }

    @Override
    public Optional<Film> deleteLike(int filmId, int userId) {
        if (userStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден.");
        }
        Optional<Film> film = findById(filmId);
        String sqlQuery = "DELETE FROM LIKES WHERE likes.film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return film;
    }

        @Override
        public List<Film> getMostPopular(Integer count) {
            String sqlQuery = "SELECT FILMS.*, COUNT(l.film_id) as count FROM FILMS\n" +
                    "LEFT JOIN LIKES l ON films.film_id=l.film_id\n" +
                    "GROUP BY FILMS.FILM_ID\n" +
                    "ORDER BY count DESC\n" +
                    "LIMIT ?";
            return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
        }

    @Override
    public Optional<Film> findById(Integer filmId) {
        String sqlQuery = "SELECT film_id, name, description, release_date, duration, mpa_id " +
                "FROM FILMS WHERE film_id=?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId));
        } catch (RuntimeException e) {
            throw new NotFoundException("Фильм не найден.");
        }
    }

    private Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("mpa_id", film.getMpa().getId());
        return values;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpaDbStorage.getMpaForId(resultSet.getInt("mpa_id")).orElse(null))
                .build();
        film.setLikes(likeDbStorage.getLikesForCurrentFilm(film.getId()));
        film.setGenres(genreDbStorage.getGenreForCurrentFilm(film.getId()));
        return film;
    }
}

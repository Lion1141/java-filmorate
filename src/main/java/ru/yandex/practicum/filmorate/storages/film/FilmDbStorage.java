package ru.yandex.practicum.filmorate.storages.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Primary
@Slf4j
@RequiredArgsConstructor
@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<FilmDao> addFilm(FilmDao film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(toMap(film)).intValue());
        log.info("Фильм добавлен.");
        return Optional.of(film);
    }

    @Override
    public Collection<FilmDao> getFilms() {
        var sql = "SELECT film_id, name, description, release_date, duration, mpa_id FROM FILMS";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public FilmDao updateFilm(FilmDao updatedFilm) throws RuntimeException {
        String sqlQuery = "UPDATE FILMS SET " +
                "name=?, description=?, release_date=?, duration=?, mpa_id=? WHERE film_id=?";
        int rowsCount = jdbcTemplate.update(sqlQuery, updatedFilm.getName(), updatedFilm.getDescription(),
                updatedFilm.getReleaseDate(), updatedFilm.getDuration(), updatedFilm.getMpaId(), updatedFilm.getId());
        if (rowsCount > 0) {
            return updatedFilm;
        }
        throw new NotFoundException("Фильм не найден.");
    }

    @Override
    public void deleteFilm(Integer filmId) {
        Optional<FilmDao> film = findById(filmId);
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
    public Optional<FilmDao> like(int filmId, int userId) {
        Optional<FilmDao> film = findById(filmId);
        String sqlQuery = "INSERT INTO LIKES (film_id, user_id) VALUES(?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return film;
    }

    @Override
    public Optional<FilmDao> deleteLike(int filmId, int userId) {
        Optional<FilmDao> film = findById(filmId);
        String sqlQuery = "DELETE FROM LIKES WHERE likes.film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return film;
    }

    @Override
    public List<FilmDao> getMostPopular(Integer count) {
        String sqlQuery = "SELECT FILMS.*, COUNT(l.film_id) as count FROM FILMS\n" +
                "LEFT JOIN LIKES l ON films.film_id=l.film_id\n" +
                "GROUP BY FILMS.FILM_ID\n" +
                "ORDER BY count DESC\n" +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    @Override
    public Optional<FilmDao> findById(Integer filmId) {
        String sqlQuery = "SELECT film_id, name, description, release_date, duration, mpa_id " +
                "FROM FILMS WHERE film_id=?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId));
        } catch (RuntimeException e) {
            throw new NotFoundException("Фильм не найден.");
        }
    }

    private Map<String, Object> toMap(FilmDao film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("mpa_id", film.getMpaId());
        return values;
    }

    private FilmDao mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return FilmDao.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getLong("duration"))
                .mpaId(resultSet.getInt("mpa_id"))
                .build();
    }
}

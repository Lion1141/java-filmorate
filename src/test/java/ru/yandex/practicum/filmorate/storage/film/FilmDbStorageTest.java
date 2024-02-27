package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.storages.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storages.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storages.user.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDbStorageTest {

    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final LikeDbStorage likeDbStorage;
    FilmDao film;
    FilmDao film2;
    UserDao user;
    UserDao user2;


    @BeforeEach
    void setUp() {
        film = FilmDao.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(1999, 8, 17))
                .duration(136L)
                .mpaId(1)
                .build();

        film2 = FilmDao.builder()
                .name("name2")
                .description("desc")
                .releaseDate(LocalDate.of(1999, 8, 17))
                .duration(136L)
                .mpaId(1)
                .build();

        user = UserDao.builder()
                .email("mail@mail.mail")
                .login("login")
                .birthday(LocalDate.of(1999, 8, 17))
                .build();
        user.setFriends(new HashSet<>());

        user2 = UserDao.builder()
                .email("gmail@gmail.gmail")
                .login("nelogin")
                .birthday(LocalDate.of(2001, 6, 19))
                .build();
        user2.setFriends(new HashSet<>());
    }

    @Test
    void addFilmTest() {
        filmDbStorage.addFilm(film);
        assertEquals(Optional.of(film), filmDbStorage.findById(film.getId()));
    }

    @Test
    void updateFilmTest() {
        filmDbStorage.addFilm(film);
        assertEquals(Optional.of(film), filmDbStorage.findById(film.getId()));

        String updatedName = "updateName";
        film.setName(updatedName);
        FilmDao updatedFilm = filmDbStorage.updateFilm(film);
        assertEquals(updatedName, updatedFilm.getName());

        Optional<FilmDao> retrievedFilm = filmDbStorage.findById(film.getId());
        assertEquals(updatedName, retrievedFilm.get().getName());
    }

    @Test
    void likeAndDeleteLikeTest() {
        filmDbStorage.addFilm(film);
        userDbStorage.createUser(user);
        userDbStorage.createUser(user2);
        filmDbStorage.like(1, 1);
        filmDbStorage.like(1, 2);
        assertEquals(2, likeDbStorage.getLikesForCurrentFilm(film.getId()).size());

        filmDbStorage.deleteLike(1, 1);
        assertEquals(1, likeDbStorage.getLikesForCurrentFilm(film.getId()).size());
    }

    @Test
    void getRatingTest() {
        filmDbStorage.addFilm(film);
        userDbStorage.createUser(user);
        userDbStorage.createUser(user2);
        filmDbStorage.like(1, 1);
        filmDbStorage.like(1, 2);
        assertEquals(1, filmDbStorage.getMostPopular(1).get(0).getId());
    }
}
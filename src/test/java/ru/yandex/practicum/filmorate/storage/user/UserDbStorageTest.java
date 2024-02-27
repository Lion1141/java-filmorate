package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.storages.user.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;
    UserDao user;
    UserDao friend;
    UserDao mutualFriend;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM friends");
        user = UserDao.builder()
                .email("mail@mail.mail")
                .name("login")
                .login("login")
                .birthday(LocalDate.of(1999, 8, 17))
                .build();
        user.setFriends(new HashSet<>());

        friend = UserDao.builder()
                .email("gmail@gmail.gmail")
                .login("nelogin")
                .name("nelogin")
                .birthday(LocalDate.of(2001, 6, 19))
                .build();
        friend.setFriends(new HashSet<>());

        mutualFriend = UserDao.builder()
                .email("mutual@mutual.mutual")
                .login("mutual")
                .name("mutual")
                .birthday(LocalDate.of(2001, 1, 11))
                .build();
        mutualFriend.setFriends(new HashSet<>());
    }

    @Test
    void shouldCreateAndUpdateAndGetUser() {
        userDbStorage.createUser(user);
        assertEquals(Optional.of(user), userDbStorage.findById(user.getId()));
        assertEquals(user.getLogin(), userDbStorage.findById(user.getId()).get().getName());

        user.setEmail("lol@lol.lol");
        userDbStorage.updateUser(user);
        assertEquals(Optional.of(user), userDbStorage.findById(user.getId()));

        assertEquals(1, userDbStorage.getUsers().size());
        assertEquals(Optional.of(user), userDbStorage.findById(user.getId()));
    }


    @Test
    void shouldAddAndDeleteFriends() {
        userDbStorage.createUser(user);
        userDbStorage.createUser(friend);
        userDbStorage.addFriend(user.getId(), friend.getId());
        assertEquals(1, userDbStorage.getUserFriends(user.getId()).size());
        assertEquals(0, userDbStorage.getUserFriends(friend.getId()).size());

        userDbStorage.deleteFriend(user.getId(), friend.getId());
        assertEquals(0, userDbStorage.getUserFriends(user.getId()).size());
        assertEquals(0, userDbStorage.getUserFriends(friend.getId()).size());
    }


    @Test
    void shouldGetMutualFriends() {
        userDbStorage.createUser(user);
        userDbStorage.createUser(friend);
        userDbStorage.createUser(mutualFriend);
        userDbStorage.addFriend(user.getId(), mutualFriend.getId());
        userDbStorage.addFriend(friend.getId(), mutualFriend.getId());
        assertSame(userDbStorage.getUserCrossFriends(user.getId(), friend.getId()).get(0).getId(), mutualFriend.getId());
    }

}
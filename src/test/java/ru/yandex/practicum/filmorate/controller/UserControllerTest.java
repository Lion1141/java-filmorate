package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserControllerTest extends AbstractControllerTest {

    @Autowired
    UserController userController;

    @Autowired
    private FilmController filmController;

    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }
    @AfterEach
    void cleanData() {
        filmService.getFilms().forEach(film -> film.getLikes().clear());
        filmService.getFilms().clear();
        userService = null;
    }

    @Test
    public void testAddUser() {
        User user = User.builder()
                .id(1)
                .email("john.doe@example.com")
                .login("johndoe123")
                .name("John Doe")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        User addedUser = userController.createUser(user);
        assertEquals(user, addedUser);
    }

    @Test
    public void testUpdateUser() {
        User existingUser = User.builder()
                .id(1)
                .email("existing.user@example.com")
                .login("existinguser")
                .name("Existing User")
                .birthday(LocalDate.of(1980, 5, 15))
                .build();
        userController.createUser(existingUser);

        User updatedUser = User.builder()
                .id(existingUser.getId())
                .email("updated.user@example.com")
                .login("updateduser")
                .name("Updated User")
                .birthday(LocalDate.of(1995, 3, 20))
                .build();
        User returnedUser = userController.updateUsers(updatedUser);
        assertEquals(updatedUser, returnedUser);
    }
    @Test
    void shouldRemoveUserByIdCorrectly() {
        User existingUser1 = User.builder()
                .id(1)
                .email("existing.user1@example.com")
                .login("existinguser1")
                .name("Existing User1")
                .birthday(LocalDate.of(1980, 5, 15))
                .build();
        userController.createUser(existingUser1);
        User existingUser2 = User.builder()
                .id(2)
                .email("existing.user2@example.com")
                .login("existinguser2")
                .name("Existing User2")
                .birthday(LocalDate.of(1995, 3, 20))
                .build();
        userController.createUser(existingUser2);
        assertEquals(2, userService.getUsers().size());
        userController.deleteUser(1);
        assertEquals(1, userService.getUsers().size());
    }

    @Test
    public void testAddUserWithInvalidEmail() {
        User user = User.builder()
                .id(1)
                .email("invalidemail")
                .login("johndoe123")
                .name("John Doe")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        var violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    public void testAddFriendWithValidIds() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        userController.addFriend(1, 2);
        verify(userService).addFriend(1, 2);
        verify(userService).addFriend(2, 1);
    }

    @Test
    public void testAddFriendWithInvalidIds() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        assertThrows(ResponseStatusException.class, () -> {
            userController.addFriend(0, 2);
        });
        verify(userService, never()).addFriend(0, 2);
    }
    @Test
    public void testGetFriends() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        User existingUser1 = User.builder()
                .id(2)
                .email("existing.user2@example.com")
                .login("existinguser2")
                .name("Existing User2")
                .birthday(LocalDate.of(1980, 5, 15))
                .build();
        userController.createUser(existingUser1);
        User existingUser2 = User.builder()
                .id(3)
                .email("existing.user3@example.com")
                .login("existinguser3")
                .name("Existing User3")
                .birthday(LocalDate.of(1995, 3, 20))
                .build();
        userController.createUser(existingUser2);
        when(userService.getFriends(1)).thenReturn(Arrays.asList(
                existingUser1,
                existingUser2
        ));
        Collection<User> result = userController.getFriends(1);
        verify(userService).getFriends(1);
        assertEquals(2, result.size());
        assertEquals(2, result.stream().map(User::getId).collect(Collectors.toList()).get(0));
        assertEquals(3, result.stream().map(User::getId).collect(Collectors.toList()).get(1));
    }
        @Test
        public void testRemoveFriend() {
            UserService userService = mock(UserService.class);
            UserController userController = new UserController(userService);
            userController.removeFriend(1, 2);
            verify(userService).removeFriend(1, 2);
        }
    @Test
    public void testGetCrossFriend() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        User existingUser1 = User.builder()
                .id(1)
                .email("existing.user1@example.com")
                .login("existinguser1")
                .name("Existing User1")
                .birthday(LocalDate.of(1980, 5, 15))
                .build();
        userController.createUser(existingUser1);
        User existingUser2 = User.builder()
                .id(2)
                .email("existing.user2@example.com")
                .login("existinguser2")
                .name("Existing User2")
                .birthday(LocalDate.of(1995, 3, 20))
                .build();
        userController.createUser(existingUser2);
        when(userService.getCrossFriends(1, 2)).thenReturn(Arrays.asList(
                existingUser1,
                existingUser2
        ));
        Collection<User> result = userController.getCrossFriend(1, 2);
        verify(userService).getCrossFriends(1, 2);
        assertEquals(2, result.size());
    }
    }


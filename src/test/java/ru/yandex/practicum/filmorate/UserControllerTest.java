package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest extends UserController {

    @Test
    public void testAddUser() throws ValidationException {
        UserController userController = new UserController();
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
    public void testUpdateUser() throws ValidationException {
        UserController userController = new UserController();
        User existingUser = User.builder()
                .id(1)
                .email("existing.user@example.com")
                .login("existinguser")
                .name("Existing User")
                .birthday(LocalDate.of(1980, 5, 15))
                .build();
        userController.createUser(existingUser);

        User updatedUser = User.builder()
                .id(1)
                .email("updated.user@example.com")
                .login("updateduser")
                .name("Updated User")
                .birthday(LocalDate.of(1995, 3, 20))
                .build();
        User returnedUser = userController.updateUsers(updatedUser);
        assertEquals(updatedUser, returnedUser);
    }

    @Test
    public void testAddUserWithInvalidEmail() {
        UserController userController = new UserController();
        User user = User.builder()
                .id(1)
                .email("invalidemail")
                .login("johndoe123")
                .name("John Doe")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

}

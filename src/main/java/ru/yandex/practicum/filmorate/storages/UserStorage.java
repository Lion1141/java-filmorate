package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {
    User createUser(User user);
    Collection<User> getUsers();
    User updateUser(User updatedUser);

    Optional<User> findById(Integer userId) throws RuntimeException;

    void deleteUser(Integer userId);
    Set<User> getUserFriends(Integer userId);
    Collection<User> getUserCrossFriends(Integer userId, Integer otherUserId);

}

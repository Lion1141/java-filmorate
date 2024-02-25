package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> createUser(User user);

    List<User> getUsers();

    User updateUser(User updatedUser);

    Optional<User> findById(Integer userId) throws RuntimeException;

    void deleteUser(Integer userId);

    Optional<User> addFriend(Integer userId, Integer friendId);

    Optional<User> deleteFriend(Integer userId, Integer friendId);

    Collection<User> getUserFriends(Integer userId);

    List<User> getUserCrossFriends(Integer userId, Integer otherUserId);
}

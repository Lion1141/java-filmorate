package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface IUserService {
    User createUser(User user);
    Collection<User> getUsers();

    Optional<User> findById(Integer userId);

    User updateUser(User updatedUser) throws RuntimeException;
    void deleteUser(Integer userId);
    void addFriend(Integer id, Integer friendId);
    void removeFriend(Integer id, Integer userId);
    Collection<User> getFriends(Integer userId);
    Collection<User> getCrossFriends(Integer userId, Integer otherUserId);
}

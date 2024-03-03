package ru.yandex.practicum.filmorate.storages.user;

import ru.yandex.practicum.filmorate.dao.UserDao;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<UserDao> createUser(UserDao userDao);

    List<UserDao> getUsers();

    UserDao updateUser(UserDao updatedUser);

    Optional<UserDao> findById(Integer userId) throws RuntimeException;

    void deleteUser(Integer userId);

    Optional<UserDao> addFriend(Integer userId, Integer friendId);

    Optional<UserDao> deleteFriend(Integer userId, Integer friendId);

    Collection<UserDao> getUserFriends(Integer userId);

    List<UserDao> getUserCrossFriends(Integer userId, Integer otherUserId);
}

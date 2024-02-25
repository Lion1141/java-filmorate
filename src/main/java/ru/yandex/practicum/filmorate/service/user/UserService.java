package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Optional<User> addFriend(int userId, int friendId) {
        userStorage.addFriend(userId, friendId);
        return userStorage.findById(userId);
    }

    public Optional<User> deleteFriend(int userId, int friendId) {
        userStorage.deleteFriend(userId, friendId);
        return userStorage.findById(userId);
    }

    public Collection<User> getUserFriends(Integer userId) {
        return userStorage.getUserFriends(userId);
    }

    public List<User> getUserCrossFriends(int userId, int otherId) {
        return userStorage.getUserCrossFriends(userId, otherId);
    }
}
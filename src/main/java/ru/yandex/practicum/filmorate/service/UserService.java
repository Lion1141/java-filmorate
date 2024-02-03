package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService implements IUserService {
    @Autowired
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public User createUser(User user) {
        User newUser = storage.createUser(user);
        log.info("Пользователь {} успешно добавлен.", user.getName());
        return newUser;
    }

    @Override
    public Collection<User> getUsers() {
        return storage.getUsers();
    }

    @Override
    public Optional<User> findById(Integer userId) {
        return storage.findById(userId);
    }

    @Override
    public User updateUser(User updatedUser) throws RuntimeException {
        log.info("Обновлён фильм: {}", updatedUser);
        return storage.updateUser(updatedUser);
    }

    @Override
    public void deleteUser(Integer userId) {
        storage.deleteUser(userId);
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        User friend = storage.findById(friendId).get();
        User user = storage.findById(id).get();
        if(user.getFriends().contains(friend.getId())){
            return;
        }
        user.addFriend(friend);
    }

    @Override
    public void removeFriend(Integer id, Integer userId) {
        User user = storage.findById(id).get();
        user.setFriends(user.getFriends().stream().filter(user1 -> !Objects.equals(user1, userId))
                .collect(Collectors.toSet()));
    }

    @Override
    public Collection<User> getFriends(Integer userId) {
        return storage.getUserFriends(userId);
    }

    @Override
    public Collection<User> getCrossFriends(Integer userId, Integer otherUserId) {
        return storage.getUserCrossFriends(userId, otherUserId);
    }
}
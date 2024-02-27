package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public List<User> getUsers() {
        var users = userStorage.getUsers();
        return users.stream().map(this::toMap).collect(Collectors.toList());
    }

    public Optional<User> updateUser(User updatedUser){
        UserDao dao = userStorage.updateUser(fromUserToDao(updatedUser));
        return Optional.of(toMap(dao));
    }

    public Optional<User> createUser(User user){
        validationUser(user);
        UserDao userDao = fromUserToDao(user);
        Optional<UserDao> result = userStorage.createUser(userDao);
        if(result.isEmpty())
            return Optional.empty();
        return Optional.of(toMap(result.get()));
    }

    public void deleteUser(Integer userId){
        userStorage.deleteUser(userId);
    }

    public Optional<User> findById(Integer userId){
        var dao = userStorage.findById(userId);
        if(dao.isEmpty())
            return Optional.empty();
        return Optional.of(toMap(dao.get()));
    }


    public Optional<User> addFriend(int userId, int friendId) {
        userStorage.addFriend(userId, friendId);
        var user = userStorage.findById(userId);
        if(user.isEmpty())
            return Optional.empty();

        return Optional.of(toMap(user.get()));
    }

    public Optional<User> deleteFriend(int userId, int friendId) {
        userStorage.deleteFriend(userId, friendId);
        var user = userStorage.findById(userId);
        if(user.isEmpty())
            return Optional.empty();

        return Optional.of(toMap(user.get()));
    }

    public Collection<User> getUserFriends(Integer userId) {
        return userStorage.getUserFriends(userId)
                .stream()
                .map(this::toMap)
                .collect(Collectors.toList());
    }

    public List<User> getUserCrossFriends(int userId, int otherId) {
        return userStorage
                .getUserCrossFriends(userId, otherId)
                .stream()
                .map(this::toMap)
                .collect(Collectors.toList());
    }

    private UserDao fromUserToDao(User user) {
        UserDao userDao = new UserDao(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getFriends());
        return userDao;
    }

    private User toMap(UserDao userDao){
        User user = User.builder()
                .id(userDao.id)
                .name(userDao.name)
                .login(userDao.login)
                .email(userDao.email)
                .birthday(userDao.birthday)
                .friends(userDao.friends)
                .build();

        return user;
    }

    private void validationUser(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
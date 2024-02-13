package ru.yandex.practicum.filmorate.storages.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    protected final HashMap<Integer, User> users = new HashMap<>();
    private static Integer id = 1;

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getId() == null) {
            user.setId(id++);
        } else {
            user.setId(user.getId());
        }
        int userId = user.getId();
        users.put(userId, user);

        return user;
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User updateUser(User updatedUser) {
        if (!users.containsKey(updatedUser.getId())) {
            throw new RuntimeException("Неизвестный пользователь");
        }
        if (updatedUser.getName().isBlank() || updatedUser.getName() == null) {
            updatedUser.setName(updatedUser.getLogin());
        }
        if (!users.containsKey(updatedUser.getId())) {
            throw new RuntimeException("Не удалось обновить пользователя");
        } else {
            users.put(updatedUser.getId(), updatedUser);
            log.info("Пользователь успешно обновлён: " + updatedUser.getName());
            return updatedUser;
        }
    }

    @Override
    public Optional<User> findById(Integer userId) throws RuntimeException {
        User user = users.get(userId);
        if (user != null) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
        } else {
            log.debug("Ошибка удаления пользователя с ID: {}", userId);
            throw new RuntimeException("Неизвестный пользователь");
        }
    }

    @Override
    public Set<User> getUserFriends(Integer userId) {
        return users.get(userId)
                .getFriends()
                .stream().map(user -> findById(user).get())
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public Collection<User> getUserCrossFriends(Integer id, Integer otherId) {
        var userFriends = getUserFriends(id);
        var otherUserFriends = getUserFriends(otherId);

        return userFriends.stream().filter(otherUserFriends::contains).collect(Collectors.toCollection(HashSet::new));
    }
}

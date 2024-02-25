package ru.yandex.practicum.filmorate.storages.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Primary
@Slf4j
@Component
@Qualifier
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> createUser(User user) {
        validationUser(user);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(toMap(user)).intValue());
        log.info("Пользователь добавлен в базу данных");
        return Optional.of(user);
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM users");
        while (rowSet.next()) {
            User user = User.builder()
                    .id(rowSet.getInt("user_id"))
                    .name(rowSet.getString("name"))
                    .login(rowSet.getString("login"))
                    .email(rowSet.getString("email"))
                    .birthday(Objects.requireNonNull(rowSet.getDate("birthday")).toLocalDate())
                    .build();
            users.add(user);
        }
        return users;
    }

    @Override
    public User updateUser(User updatedUser) {
        validationUser(updatedUser);
        String sqlQuery = "UPDATE users SET " +
                "email=?, login=?, name=?, birthday=? WHERE user_id=?";
        int rowsCount = jdbcTemplate.update(sqlQuery, updatedUser.getEmail(), updatedUser.getLogin(),
                updatedUser.getName(), updatedUser.getBirthday(), updatedUser.getId());
        if (rowsCount > 0) {
            log.info("Пользователь изменён");
            return updatedUser;
        }
        throw new NotFoundException("Пользователь не найден.");
    }

    @Override
    public Optional<User> findById(Integer userId) throws RuntimeException {
        String sqlQuery = "SELECT user_id, email, login, name, birthday FROM users WHERE user_id=?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId));
        } catch (RuntimeException e) {
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        Optional<User> user = findById(userId);
        if (user.isPresent()) {
            String sqlQuery =
                    "DELETE " +
                            "FROM users " +
                            "WHERE user_id = ?";
            jdbcTemplate.update(sqlQuery, userId);
            log.info("Пользователь удалён");
        } else {
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    @Override
    public Optional<User> addFriend(Integer userId, Integer friendId) {
            Optional<User> user = findById(userId);
            try {
                findById(friendId);
            } catch (RuntimeException e) {
                throw new NotFoundException("Пользователь не найден.");
            }
            String sqlQuery = "INSERT INTO friends (user_id, friend_id) VALUES(?, ?)";
            jdbcTemplate.update(sqlQuery, userId, friendId);
            return user;
        }

    @Override
    public Optional<User> deleteFriend(Integer userId, Integer friendId) {
        Optional<User> user = findById(userId);
        String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return user;
    }

    @Override
    public Collection<User> getUserFriends(Integer userId) {
        final String sql = "SELECT * From USERS where USER_ID IN (SELECT FRIEND_ID FROM FRIENDS where USER_ID = ?)";
        Collection<User> friends = new HashSet<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, userId);
        while (rs.next()) {
            User user = User.builder()
                    .id(rs.getInt("user_id"))
                    .email(rs.getString("email"))
                    .login(rs.getString("login"))
                    .name(rs.getString("name"))
                    .birthday(rs.getDate("birthday").toLocalDate())
                    .build();
            friends.add(user);
        }
        return friends.stream().sorted(Comparator.comparing(User::getId)).collect(Collectors.toList());
    }

    @Override
    public List<User> getUserCrossFriends(Integer userId, Integer otherUserId) {
        String sqlQuery = "SELECT user_id, email, login, name, birthday FROM users WHERE user_id IN(" +
                "SELECT friend_id FROM friends WHERE user_id = ?) " +
                "AND user_id IN(SELECT friend_id FROM friends WHERE user_id = ?)";
        return new ArrayList<>(jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId, otherUserId));
    }

    private void validationUser(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private Map<String, Object> toMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        return values;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
        user.setFriends(getUserFriends(user.getId()).stream().map(User::getId).collect(Collectors.toSet()));
        return user;
    }
}

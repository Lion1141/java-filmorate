package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validator.IsAfter;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    private Integer rate;
    final private Set<Integer> likes = new HashSet<>();
    private Integer id;
    @NotEmpty(message = "Название фильма не должно быть пустым")
    private String name;
    @Size(max = 200)
    private String description;
    @IsAfter(current = "1895-12-28", message = "Ошибка. В эту дату фильм не мог выйти")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive
    private long duration;

    public void addLike(Integer filmId) {
        likes.add(filmId);
    }

    public void removeLike(Integer userId) {
        likes.remove(userId);
    }
}

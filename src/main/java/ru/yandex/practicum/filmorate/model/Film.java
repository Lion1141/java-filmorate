package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validator.IsAfter;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Collection;

@Data
@Builder
public class Film {
    @NotNull
    private Mpa mpa;
    private Collection<Genre> genres;
    private Collection<Integer> likes;
    @Min(value = 1, message = "Невозможно найти фильм с указанным ID")
    protected Integer id;
    @NotEmpty(message = "Название фильма не должно быть пустым")
    private String name;
    @Size(max = 200)
    private String description;
    @IsAfter(current = "1895-12-28", message = "Ошибка. В эту дату фильм не мог выйти")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive
    private long duration;
}

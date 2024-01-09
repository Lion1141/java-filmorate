package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film {
        Integer id;
    @NotBlank(message = "Название фильма не должно быть пустым")
    String name;
    @Size(max = 200)
    String description;
    LocalDate releaseDate; //не смог подобрать нужную аннотацию
    @Positive
    Long duration;


}

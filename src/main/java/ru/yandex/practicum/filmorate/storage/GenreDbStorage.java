package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class GenreDbStorage extends BaseRepository<Genre> implements GenreStorage {


    public GenreDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Genre> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    private static final String FIND_ALL_GENRES_QUERY = """
            SELECT *
            FROM genres;
            """;
    private static final String FIND_BY_ID_QUERY = """
            SELECT *
            FROM genres
            WHERE id = ?
            ORDER BY id;
            """;

    private static final String FIND_BY_FILM_ID_QUERY = """
            SELECT g.id AS id,
                   g.name AS name
            FROM film_genre AS fg
            JOIN genres AS g ON fg.genre_id = g.id
            WHERE fg.film_id = ?;
            """;

    @Override
    public Optional<Genre> findById(long id) {
        log.info("making a request to find genre with id: {}", id);
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Genre> findAll() {
        log.info("making a request to find all genres");
        return findMany(FIND_ALL_GENRES_QUERY);
    }

    @Override
    public List<Genre> findFilmGenres(long filmId) {
        log.info("making a request to find genre with filmId: {}", filmId);
        return findMany(FIND_BY_FILM_ID_QUERY, filmId);
    }
}

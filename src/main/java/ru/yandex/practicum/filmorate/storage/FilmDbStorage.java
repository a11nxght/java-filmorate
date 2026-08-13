package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Repository("FilmDbStorage")
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {

    public FilmDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Film> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    private static final String INSERT_QUERY = """
            INSERT INTO films (name, description, release_date, duration)
            VALUES (?, ?, ?, ?);
            """;

    private static final String DELETE_QUERY = """
                    DELETE
                    FROM films
                    WHERE id=?;
            """;

    private static final String UPDATE_QUERY = """
            UPDATE films
            SET name = ?,
                description = ?,
                release_date = ?,
                duration = ?
            WHERE id=?;
            """;

    private static final String FIND_BY_ID_QUERY = """
            SELECT *
            FROM films
            WHERE id=?;
            """;

    private static final String FIND_ALL_QUERY = """
            SELECT *
            FROM films
            ORDER BY id;
    """;

    private static final String FIND_POPULAR_QUERY = """
            SELECT *
            FROM films AS f
            JOIN
                (SELECT film_id,
                        COUNT(*) AS likes_count
                FROM likes
                GROUP BY film_id
                ORDER BY likes_count DESC) AS l ON f.id = l.film_id
            LIMIT ?;
    """;

    @Override
    public Film save(Film film) {
        long id = save(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration());
        film.setId(id);
        return film;
    }

    @Override
    public void delete(long id) {
        delete(DELETE_QUERY, id);
    }

    @Override
    public Film update(Film film) {
        update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());
        return film;
    }

    @Override
    public Optional<Film> findById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public List<Film> findPopular(int count) {
        return findMany(FIND_POPULAR_QUERY, count);
    }
}

package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository("FilmDbStorage")
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {

    public FilmDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Film> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    private static final String INSERT_QUERY = """
            INSERT INTO films (name, description, release_date, duration, mpa_id)
            VALUES (?, ?, ?, ?, ?);
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
            SELECT f.id AS id,
                   f.name AS name,
                   f.description AS description,
                   f.release_date AS release_date,
                   f.duration AS duration,
                   f.mpa_id AS mpa_id,
                   m.name AS mpa_name
            FROM films AS f
            JOIN mpa AS m ON f.mpa_id = m.id
            WHERE f.id=?;
            """;

    private static final String FIND_ALL_QUERY = """
            SELECT f.id AS id,
                   f.name AS name,
                   f.description AS description,
                   f.release_date AS release_date,
                   f.duration AS duration,
                   f.mpa_id AS mpa_id,
                   m.name AS mpa_name
            FROM films AS f
            JOIN mpa AS m ON f.mpa_id = m.id
            ORDER BY id;
            """;

    private static final String FIND_POPULAR_QUERY = """
            SELECT f.id AS id,
                   f.name AS name,
                   f.description AS description,
                   f.release_date AS release_date,
                   f.duration AS duration,
                   f.mpa_id AS mpa_id,
                   m.name AS mpa_name
            FROM films AS f
            JOIN mpa AS m ON f.mpa_id = m.id
            JOIN
              (SELECT film_id,
                      COUNT(*) AS likes_count
               FROM likes
               GROUP BY film_id
               ORDER BY likes_count DESC) AS l ON f.id = l.film_id
            LIMIT ?;
            """;

    private static final String INSERT_GENRE_QUERY = """
                INSERT INTO film_genre (film_id, genre_id)
                VALUES (?, ?);
    """;

    private static final String FIND_ALL_GENRES_QUERY = """
                SELECT g.id AS id,
                               g.name AS name
                        FROM film_genre AS fg
                        JOIN genres AS g ON fg.genre_id = g.id
                        WHERE fg.film_id = ?;
    """;

    @Override
    public Film save(Film film) {
        long id = save(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null);
        film.setId(id);
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(INSERT_GENRE_QUERY, film.getId(), genre.getId());
            }
        }
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

    @Override
    public void setFilmGenres(Film film) {
        List<Genre> genres = jdbcTemplate.query(FIND_ALL_GENRES_QUERY, (rs, rowNum) ->
                        Genre.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .build(),
                film.getId());
        genres.forEach(genre -> film.getGenres().add(genre));
    }
}

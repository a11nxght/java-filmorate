package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Slf4j
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
                   m.name AS mpa_name,
                   l.likes_count AS likes
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            LEFT JOIN
              (SELECT film_id,
                      COUNT(*) AS likes_count
               FROM likes
               GROUP BY film_id
               ORDER BY likes_count DESC) AS l ON f.id = l.film_id
            WHERE f.id=?;
            """;

    private static final String FIND_ALL_QUERY = """
            SELECT f.id AS id,
                   f.name AS name,
                   f.description AS description,
                   f.release_date AS release_date,
                   f.duration AS duration,
                   f.mpa_id AS mpa_id,
                   m.name AS mpa_name,
                   l.likes_count AS likes
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            LEFT JOIN
              (SELECT film_id,
                      COUNT(*) AS likes_count
               FROM likes
               GROUP BY film_id
               ORDER BY likes_count DESC) AS l ON f.id = l.film_id
            ORDER BY id;
            """;

    private static final String FIND_POPULAR_QUERY = """
            SELECT f.id AS id,
                   f.name AS name,
                   f.description AS description,
                   f.release_date AS release_date,
                   f.duration AS duration,
                   f.mpa_id AS mpa_id,
                   m.name AS mpa_name,
                   l.likes_count AS likes
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            LEFT JOIN
              (SELECT film_id,
                      COUNT(*) AS likes_count
               FROM likes
               GROUP BY film_id
               ORDER BY likes_count DESC) AS l ON f.id = l.film_id
            ORDER BY likes_count DESC
            LIMIT ?;
            """;

    private static final String FIND_POPULAR_WITH_GENRE_AND_YEAR_QUERY = """
            SELECT f.id AS id,
                   f.name AS name,
                   f.description AS description,
                   f.release_date AS release_date,
                   f.duration AS duration,
                   f.mpa_id AS mpa_id,
                   m.name AS mpa_name,
                   l.likes_count AS likes
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            LEFT JOIN
              (SELECT film_id,
                      COUNT(*) AS likes_count
               FROM likes
               GROUP BY film_id
               ORDER BY likes_count DESC) AS l ON f.id = l.film_id
            JOIN film_genre AS fg ON f.id = fg.film_id AND fg.genre_id = ?
            WHERE EXTRACT(YEAR FROM release_date) = ?
            ORDER BY likes_count DESC
            LIMIT ?;
            """;

    private static final String FIND_POPULAR_WITH_GENRE_QUERY = """
            SELECT f.id AS id,
                   f.name AS name,
                   f.description AS description,
                   f.release_date AS release_date,
                   f.duration AS duration,
                   f.mpa_id AS mpa_id,
                   m.name AS mpa_name,
                   l.likes_count AS likes
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            LEFT JOIN
              (SELECT film_id,
                      COUNT(*) AS likes_count
               FROM likes
               GROUP BY film_id
               ORDER BY likes_count DESC) AS l ON f.id = l.film_id
            JOIN film_genre AS fg ON f.id = fg.film_id AND fg.genre_id = ?
            ORDER BY likes_count DESC
            LIMIT ?;
            """;

    private static final String FIND_POPULAR_WITH_YEAR_QUERY = """
            SELECT f.id AS id,
                   f.name AS name,
                   f.description AS description,
                   f.release_date AS release_date,
                   f.duration AS duration,
                   f.mpa_id AS mpa_id,
                   m.name AS mpa_name,
                   l.likes_count AS likes
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            LEFT JOIN
              (SELECT film_id,
                      COUNT(*) AS likes_count
               FROM likes
               GROUP BY film_id
               ORDER BY likes_count DESC) AS l ON f.id = l.film_id
            WHERE EXTRACT(YEAR FROM release_date) = ?
            ORDER BY likes_count DESC
            LIMIT ?;
            """;
    private static final String INSERT_GENRE_QUERY = """
                        INSERT INTO film_genre (film_id, genre_id)
                        VALUES (?, ?);
            """;
    private static final String INSERT_DIRECTOR_QUERY = """
            INSERT INTO film_director (film_id, director_id)
            VALUES (?, ?);
            """;

    private static final String DELETE_DIRECTOR_QUERY = """
                            DELETE FROM film_director
                            WHERE film_id = ?;
            """;


    private static final String FIND_COMMON_QUERY = """
            SELECT f.id AS id,
                   f.name AS name,
                   f.description AS description,
                   f.release_date AS release_date,
                   f.duration AS duration,
                   f.mpa_id AS mpa_id,
                   m.name AS mpa_name,
                   l.likes_count AS likes
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            LEFT JOIN
              (SELECT film_id,
                      COUNT(*) AS likes_count
               FROM likes
               GROUP BY film_id
               ORDER BY likes_count DESC) AS l ON f.id = l.film_id
            JOIN likes AS l1 ON f.id = l1.film_id AND l1.user_id = ?
            JOIN likes AS l2 ON f.id = l2.film_id AND l2.user_id = ?
            ORDER BY likes_count DESC;
            """;

    private static final String FIND_DIRECTOR_FILM_QUERY = """
            SELECT f.id AS id,
                   f.name AS name,
                   f.description AS description,
                   f.release_date AS release_date,
                   f.duration AS duration,
                   f.mpa_id AS mpa_id,
                   m.name AS mpa_name,
                   l.likes_count AS likes
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            LEFT JOIN
              (SELECT film_id,
                      COUNT(*) AS likes_count
               FROM likes
               GROUP BY film_id
               ORDER BY likes_count DESC) AS l ON f.id = l.film_id
            JOIN film_director AS fd ON f.id = fd.film_id
            WHERE fd.director_id = ?
            ORDER BY id;
            """;

    private static final String SEARCH_QUERY = """
                    SELECT f.id AS id,
                           f.name AS name,
                           f.description AS description,
                           f.release_date AS release_date,
                           f.duration AS duration,
                           f.mpa_id AS mpa_id,
                           m.name AS mpa_name,
                           l.likes_count AS likes
                    FROM films AS f
                    LEFT JOIN mpa AS m ON f.mpa_id = m.id
                    LEFT JOIN
                      (SELECT film_id,
                              COUNT(*) AS likes_count
                       FROM likes
                       GROUP BY film_id
                       ORDER BY likes_count DESC) AS l ON f.id = l.film_id
                    LEFT JOIN film_director AS fd ON f.id = fd.film_id
                    LEFT JOIN directors AS d ON fd.director_id = d.id
                    WHERE 1=1
            """;

    private static final String FIND_RECOMMENDATIONS_QUERY = """
                   SELECT f.id AS id,
                          f.name AS name,
                          f.description AS description,
                          f.release_date AS release_date,
                          f.duration AS duration,
                          f.mpa_id AS mpa_id,
                          m.name AS mpa_name,
                          l.likes_count AS likes
                   FROM films AS f
                   LEFT JOIN mpa AS m ON f.mpa_id = m.id
                   LEFT JOIN
                     (SELECT film_id,
                             COUNT(*) AS likes_count
                      FROM likes
                      GROUP BY film_id
                      ORDER BY likes_count DESC) AS l ON f.id = l.film_id
                   JOIN likes AS l1 ON f.id = l1.film_id
                   AND l1.user_id =
                     (SELECT user_id
                      FROM likes
                      WHERE film_id IN
                          (SELECT film_id
                           FROM likes
                           WHERE user_id = ?)
                        AND user_id <> ?
                      GROUP BY user_id
                      ORDER BY COUNT(*) DESC
                      LIMIT 1)
                   AND l1.film_id NOT IN
                     (SELECT film_id
                      FROM likes
                      WHERE user_id = ?)
                   ORDER BY likes_count DESC;
    """;

    private static final String SEARCH_ORDER_BY_LIKES_SUBQUERY = """
                    ORDER BY likes_count DESC;
            """;

    @Override
    public Film save(Film film) {
        log.info("Making a request to save a film");
        long id = save(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null);
        film.setId(id);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                update(INSERT_GENRE_QUERY, film.getId(), genre.getId());
            }
        }
        if (film.getDirectors() != null && !film.getDirectors().isEmpty()) {
            for (Director director : film.getDirectors()) {
                update(INSERT_DIRECTOR_QUERY, film.getId(), director.getId());
            }
        }
        return film;
    }

    @Override
    public void delete(long id) {
        log.info("Making a request to delete a film");
        delete(DELETE_QUERY, id);
    }

    @Override
    public Film update(Film film) {
        log.info("Making a request to update a film");
        update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());
        if (film.getDirectors() != null && !film.getDirectors().isEmpty()) {
            delete(DELETE_DIRECTOR_QUERY, film.getId());
            for (Director director : film.getDirectors()) {
                update(INSERT_DIRECTOR_QUERY, film.getId(), director.getId());
            }
        }
        return film;
    }

    @Override
    public Optional<Film> findById(long id) {
        log.info("Making a request to find a film by id: {}", id);
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Film> findAll() {
        log.info("Making a request to find all films");
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public List<Film> findPopular(int count) {
        log.info("Making a request to find all popular films");
        return findMany(FIND_POPULAR_QUERY, count);
    }

    @Override
    public List<Film> findPopularWithGenreAndYear(int count, long genreId, int year) {
        log.info("Making a request to find all popular films with genreId: {} and year: {}", genreId, year);
        return findMany(FIND_POPULAR_WITH_GENRE_AND_YEAR_QUERY, genreId, year, count);
    }

    @Override
    public List<Film> findPopularWithGenre(int count, long genreId) {
        log.info("Making a request to find all popular films with genreId: {}", genreId);
        return findMany(FIND_POPULAR_WITH_GENRE_QUERY, genreId, count);
    }

    @Override
    public List<Film> findPopularWithYear(int count, int year) {
        log.info("Making a request to find all popular films with year: {}", year);
        return findMany(FIND_POPULAR_WITH_YEAR_QUERY, year, count);
    }

    @Override
    public List<Film> findCommon(long userId, long friendId) {
        log.info("Making a request to get common films");
        return findMany(FIND_COMMON_QUERY, userId, friendId);
    }

    @Override
    public List<Film> findByDirector(long directorId) {
        log.info("Making a request to get films with director id: {}", directorId);
        return findMany(FIND_DIRECTOR_FILM_QUERY, directorId);
    }

    @Override
    public List<Film> searchFilms(String query, List<String> by) {
        String queryForDb = SEARCH_QUERY;
        if (by != null && !by.isEmpty()) {
            if (by.contains("director") && by.contains("title")) {
                queryForDb += "AND d.name LIKE" + " '%" + query + "%' \n";
                queryForDb += "OR f.name LIKE" + " '%" + query + "%' \n";
            } else if (by.contains("director")) {
                queryForDb += "AND d.name LIKE" + " '%" + query + "%' \n";
            } else if (by.contains("title")) {
                queryForDb += "AND f.name LIKE" + " '%" + query + "%' \n";
            }
        }
        queryForDb += SEARCH_ORDER_BY_LIKES_SUBQUERY;
        log.info("Making a request to search films with query: {}", query);
        return findMany(queryForDb);
    }

    @Override
    public List<Film> findRecommendations(long userId) {
        log.info("Making a request to find recommendations for user: {}", userId);
        return findMany(FIND_RECOMMENDATIONS_QUERY, userId, userId, userId);
    }
}

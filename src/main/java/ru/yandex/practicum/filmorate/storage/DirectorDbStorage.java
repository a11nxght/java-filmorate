package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class DirectorDbStorage extends BaseRepository<Director> implements DirectorStorage {

    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, (rs, rowNum) -> Director.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build());
    }

    private static final String FIND_BY_ID_QUERY = """
            SELECT *
            FROM directors
            WHERE id = ?
            """;

    private static final String FIND_ALL_QUERY = """
                    SELECT *
                    FROM directors
            """;

    private static final String INSERT_QUERY = """
                    INSERT INTO directors (name)
                    VALUES (?)
            """;

    private static final String UPDATE_QUERY = """
                    UPDATE directors
                    SET name = ?
                    WHERE id = ?
            """;

    private static final String DELETE_QUERY = """
                    DELETE FROM directors
                    WHERE id = ?
            """;

    private static final String FIND_BY_FILM_ID_QUERY = """
                    SELECT *
                    FROM directors AS d
                    JOIN film_director AS fd ON d.id = fd.director_id
                    WHERE fd.film_id = ?
    """;

    @Override
    public Optional<Director> findById(long id) {
        log.info("Making a request to find director by id {}", id);
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Director> findAll() {
        log.info("Making a request to find all directors");
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public long save(Director director) {
        log.info("Making a request to save a director {}", director);
        return save(INSERT_QUERY, director.getName());
    }

    @Override
    public void update(Director director) {
        log.info("Making a request to update a director {}", director);
        update(UPDATE_QUERY, director.getName(), director.getId());
    }

    @Override
    public void delete(long id) {
        log.info("Making a request to delete a director {}", id);
        delete(DELETE_QUERY, id);
    }

    @Override
    public List<Director> findFilmDirectors(long id) {
        log.info("Making a request to find directors with film id {}", id);
        return findMany(FIND_BY_FILM_ID_QUERY, id);
    }
}

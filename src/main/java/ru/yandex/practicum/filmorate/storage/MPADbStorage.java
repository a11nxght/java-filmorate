package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class MPADbStorage extends BaseRepository<MPA> implements MPAStorage {

    MPADbStorage(JdbcTemplate jdbcTemplate, RowMapper<MPA> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    private static final String FIND_BY_ID_QUERY = """
            SELECT *
            FROM mpa
            WHERE id = ?;
            """;

    private static final String FIND_ALL_QUERY = """
            SELECT *
            FROM mpa;
    """;

    @Override
    public Optional<MPA> findById(long id) {
        log.info("making a request to find mpa with id: {}", id);
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<MPA> findAll() {
        log.info("making a request to find all MPA");
        return findMany(FIND_ALL_QUERY);
    }
}

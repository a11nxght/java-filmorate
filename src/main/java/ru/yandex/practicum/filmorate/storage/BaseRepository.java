package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseRepository<T> {
    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<T> rowMapper;

    protected Optional<T> findOne(String query, Object... args) {
        try {
            T result = jdbcTemplate.queryForObject(query, rowMapper, args);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    protected List<T> findMany(String query, Object... args) {
        return jdbcTemplate.query(query, rowMapper, args);
    }

    protected long save(String query, Object... args) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            return preparedStatement;
        },  keyHolder);
        Number id = keyHolder.getKey();
        if (id != null) {
            return id.longValue();
        } else {
            throw new InternalServerException("Could not save record");
        }
    }

    protected void update(String query, Object... args) {
        int  updateCount = jdbcTemplate.update(query, args);
        if (updateCount == 0) {
            throw new InternalServerException("Could not update record");
        }
    }

    protected boolean delete(String query, Object... args) {
        int  deleteCount = jdbcTemplate.update(query, args);
        return deleteCount > 0;
    }
}

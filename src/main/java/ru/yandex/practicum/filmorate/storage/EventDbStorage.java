package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.event_feed.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.Instant;
import java.util.List;

@Repository
public class EventDbStorage extends BaseRepository<Event> implements EventStorage {
    public EventDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Event> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    private static final String INSERT_QUERY = """
            INSERT
            INTO events (timestamp, user_id, event_type, operation, entity_id)
            VALUES (?, ?, ?, ?, ?);
            """;

    private static final String FIND_USER_EVENTS_QUERY = """
            SELECT *
            FROM events
            WHERE user_id = ?;
    """;

    @Override
    public void addEvent(Event event) {
        save(INSERT_QUERY,
                Instant.now(),
                event.getUserId(),
                event.getEventType().toString(),
                event.getOperation().toString(),
                event.getEntityId());
    }

    @Override
    public List<Event> findUserEvents(Long userId) {
        return findMany(FIND_USER_EVENTS_QUERY, userId);
    }
}

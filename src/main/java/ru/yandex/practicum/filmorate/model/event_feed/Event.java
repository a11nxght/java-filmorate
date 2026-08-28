package ru.yandex.practicum.filmorate.model.event_feed;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @JsonFormat(shape = JsonFormat.Shape.NUMBER,
                without = JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
    Instant timestamp;
    Long userId;
    EventType eventType;
    Operation operation;
    Long eventId;
    Long entityId;
}

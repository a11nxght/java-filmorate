package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.event_feed.Event;

import java.util.List;

public interface EventStorage {
    void addEvent(Event event);

    List<Event> findUserEvents(Long userId);
}

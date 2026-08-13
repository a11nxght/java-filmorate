package ru.yandex.practicum.filmorate.storage;

public interface FriendshipStorage {
    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);
}

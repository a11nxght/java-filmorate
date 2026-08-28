package ru.yandex.practicum.filmorate.storage;

public interface FriendshipStorage {
    boolean addFriend(long userId, long friendId);

    boolean removeFriend(long userId, long friendId);
}

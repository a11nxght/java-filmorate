package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friendship {
    Long UserID;
    Long FriendID;
    Boolean IsFriend;
}

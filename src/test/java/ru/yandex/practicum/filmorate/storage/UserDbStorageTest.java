package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class, FriendshipDbStorage.class})
class UserDbStorageTest {
    private final UserDbStorage userStorage;
    private final FriendshipDbStorage friendshipDbStorage;
    User newUser1;
    User newUser2;
    User newUser3;

    @BeforeEach
    void setUp() {
        newUser1 = User.builder()
                .email("qupa@ya.ru")
                .login("qupa")
                .name("qupa")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();
        newUser2 = User.builder()
                .email("wupa@ya.ru")
                .login("wupa")
                .name("wupa")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();
        newUser3 = User.builder()
                .email("eupa@ya.ru")
                .login("eupa")
                .name("eupa")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();
        newUser1 = userStorage.save(newUser1);
        newUser2 = userStorage.save(newUser2);
        newUser3 = userStorage.save(newUser3);
    }

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userStorage.findById(newUser1.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", newUser1.getId())
                );
    }

    @Test
    public void testDeleteUserById() {
        userStorage.delete(newUser1.getId());
        Optional<User> userOptional = userStorage.findById(newUser1.getId());

        assertThat(userOptional).isEmpty();
    }

    @Test
    public void testUpdateUser() {
        User updateUser = newUser1.toBuilder()
                .email("updtupa@ya.ru")
                .login("updtupa")
                .name("updtupa")
                .birthday(LocalDate.of(1981, 1, 1))
                .build();
        userStorage.update(updateUser);
        Optional<User> userOptional = userStorage.findById(updateUser.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user.getId()).isEqualTo(updateUser.getId());
                            assertThat(user.getName()).isEqualTo(updateUser.getName());
                            assertThat(user.getBirthday()).isEqualTo(updateUser.getBirthday());
                            assertThat(user.getEmail()).isEqualTo(updateUser.getEmail());
                        }
                );
    }

    @Test
    public void testFindAllUsers() {
        List<User> allUsers = userStorage.findAll();
        assertThat(allUsers).hasSize(3);
    }

    @Test
    public void testAddFriend() {
        friendshipDbStorage.addFriend(newUser1.getId(), newUser2.getId());
        List<User> friends = userStorage.findFriends(newUser1.getId());
        assertThat(friends).hasSize(1);
        assertThat(friends.getFirst().getId()).isEqualTo(newUser2.getId());
    }

    @Test
    public void testRemoveFriend() {
        friendshipDbStorage.addFriend(newUser1.getId(), newUser2.getId());
        friendshipDbStorage.removeFriend(newUser1.getId(), newUser2.getId());
        List<User> friends = userStorage.findFriends(newUser1.getId());
        assertThat(friends).isEmpty();
    }

    @Test
    public void testFindCommonFriends() {
        friendshipDbStorage.addFriend(newUser1.getId(), newUser2.getId());
        friendshipDbStorage.addFriend(newUser3.getId(), newUser2.getId());
        friendshipDbStorage.addFriend(newUser1.getId(), newUser3.getId());
        List<User> commonFriends = userStorage.findCommonFriends(newUser1.getId(), newUser3.getId());
        assertThat(commonFriends).hasSize(1);
        assertThat(commonFriends.getFirst().getId()).isEqualTo(newUser2.getId());
    }
}

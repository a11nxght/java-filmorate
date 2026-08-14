package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class,
        FilmRowMapper.class,
        LikesDbStorage.class,
        UserDbStorage.class,
        UserRowMapper.class})
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final LikesStorage likesStorage;
    private final UserStorage userStorage;

    Film film1;
    Film film2;
    Film film3;

    @BeforeEach
    void setUp() {
        film1 = Film.builder()
                .name("shrek")
                .description("perfect cartoon")
                .releaseDate(LocalDate.of(2004, 1, 1))
                .duration(120)
                .build();
        film2 = Film.builder()
                .name("terminator")
                .description("good film")
                .releaseDate(LocalDate.of(1998, 2, 2))
                .duration(99)
                .build();
        film3 = Film.builder()
                .name("interstellar")
                .description("amazing film")
                .releaseDate(LocalDate.of(2015, 5, 5))
                .duration(187)
                .build();
        filmDbStorage.save(film1);
        filmDbStorage.save(film2);
        filmDbStorage.save(film3);
    }

    @Test
    public void testFindById() {
        Optional<Film> film = filmDbStorage.findById(film1.getId());
        assertThat(film).isPresent()
                .hasValueSatisfying(f -> {
            assertThat(f).hasFieldOrPropertyWithValue("id", film1.getId());
        });
    }

    @Test
    public void testDeleteById() {
        filmDbStorage.delete(film1.getId());
        Optional<Film> film = filmDbStorage.findById(film1.getId());
        assertThat(film).isNotPresent();
    }

    @Test
    public void testUpdate() {
        long id = film1.getId();
        film1 = film1.toBuilder().id(id)
                .name("shrek2")
                .description("very perfect cartoon")
                .releaseDate(LocalDate.of(2007, 2, 2))
                .duration(123)
                .build();
        filmDbStorage.update(film1);
        Optional<Film> film = filmDbStorage.findById(film1.getId());
        assertThat(film).isPresent().hasValueSatisfying(f -> {
            assertThat(f.getName()).isEqualTo("shrek2");
            assertThat(f.getDescription()).isEqualTo("very perfect cartoon");
            assertThat(f.getReleaseDate()).isEqualTo(LocalDate.of(2007, 2, 2));
            assertThat(f.getDuration()).isEqualTo(123);
        });
    }

    @Test
    public void testFindAll() {
        List<Film> films = filmDbStorage.findAll();
        assertThat(films.size()).isEqualTo(3);
    }

    @Test
    public void testFindPopularFilms() {
        User newUser1 = User.builder()
                .email("qupa@ya.ru")
                .login("qupa")
                .name("qupa")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();
        User newUser2 = User.builder()
                .email("wupa@ya.ru")
                .login("wupa")
                .name("wupa")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();
        User newUser3 = User.builder()
                .email("eupa@ya.ru")
                .login("eupa")
                .name("eupa")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();
        User newUser4 = User.builder()
                .email("rupa@ya.ru")
                .login("rupa")
                .name("rupa")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();
        User newUser5 = User.builder()
                .email("tupa@ya.ru")
                .login("tupa")
                .name("tupa")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();
        newUser1 = userStorage.save(newUser1);
        newUser2 = userStorage.save(newUser2);
        newUser3 = userStorage.save(newUser3);
        newUser4 = userStorage.save(newUser4);
        newUser5 = userStorage.save(newUser5);

        likesStorage.addLike(film1.getId(), newUser1.getId());
        likesStorage.addLike(film1.getId(), newUser2.getId());
        likesStorage.addLike(film1.getId(), newUser3.getId());
        likesStorage.addLike(film2.getId(), newUser4.getId());
        likesStorage.addLike(film2.getId(), newUser5.getId());
        List<Film> films = filmDbStorage.findPopular(2);
        assertThat(films.size()).isEqualTo(2);
        assertThat(films.getFirst().getId()).isEqualTo(film1.getId());
    }
}
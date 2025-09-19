package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {

    private FilmController filmController;
    private Film film;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        film = FilmControllerTest.Stub.getFilm();

    }

    @Test
    @DisplayName("Добавление фильма проверка Duration")
    public void test_CreateFilm_CheckDuration() throws ParseException {
        //given
        film = Stub.getFilm();

        //when
        Film createdFilm = filmController.create(film);

        //then
        assertEquals(1, createdFilm.getId(), "id объектов не совпадают");
        assertEquals("Film name", createdFilm.getName(), "id объектов не совпадают");
        assertEquals("Film description", createdFilm.getDescription(), "id объектов не совпадают");
        assertEquals(LocalDate.of(2000, 1, 1), createdFilm.getReleaseDate(), "id объектов не совпадают");
        assertEquals(90 * 60, createdFilm.getDuration().getSeconds(), "id объектов не совпадают");

    }

    @Test
    @DisplayName("Добавление фильма")
    public void test_CreateFilm() throws ParseException {
        //given
        film = Stub.getFilm();

        //when
        Film createdFilm = filmController.create(film);

        //then
        assertEquals(1, createdFilm.getId(), "id объектов не совпадают");
        assertEquals("Film name", createdFilm.getName(), "id объектов не совпадают");
        assertEquals("Film description", createdFilm.getDescription(), "id объектов не совпадают");
        assertEquals(LocalDate.of(2000, 1, 1), createdFilm.getReleaseDate(), "id объектов не совпадают");
        assertEquals(Duration.ofMinutes(90), createdFilm.getDuration(), "id объектов не совпадают");

    }

    @Test
    @DisplayName("Задано пустое название")
    public void test_EmptyName() throws ParseException {
        //given
        film = Stub.getFilm();
        film.setName("");

        //when
        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.create(film));

        //then
        assertEquals("Название не может быть пустым", exception.getMessage());

    }

    @Test
    @DisplayName("Максимальная длина описания — 200 символов.")
    public void test_DescriptionLength_MoreThan200() {
        //given
        film = Stub.getFilm();
        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.");

        //when
        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.create(film));

        //then
        assertEquals("Максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    @DisplayName("Дата релиза — не раньше 28 декабря 1895 года")
    public void test_WrongReleaseDate() {
        //given
        film = Stub.getFilm();
        film.setReleaseDate(FilmController.CINEMA_BIRTHDAY.minusDays(1));

        //when
        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.create(film));

        //then
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    @DisplayName("Продолжительность фильма должна быть положительным числом")
    public void test_PositiveDuration() {
        //given
        film = Stub.getFilm();
        film.setDuration(Duration.ofSeconds(0));

        //when
        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.create(film));

        //then
        assertEquals("Продолжительность фильма должна быть положительным числом", exception.getMessage());
    }

    static class Stub {
        public static Film getFilm() {
            Film film = new Film();
            film.setId(1);
            film.setName("Film name");
            film.setDescription("Film description");
            film.setReleaseDate(LocalDate.of(2000, 1, 1));
            film.setDuration(Duration.ofMinutes(90));

            return film;
        }
    }
}

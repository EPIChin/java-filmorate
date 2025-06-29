package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private User user;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeEach
    public void beforeEach() {
        user = new User(1, "name@ya.ru", "login", "name", LocalDate.of(2000, 01, 01));
    }

    @Test
    void emailValidationTest() {
        user.setEmail("");
        Set<ConstraintViolation<User>> validate = validator.validate(user);
        Set<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
        assertEquals(1, errorMessages.size());
        user.setEmail("mail");
        validate = validator.validate(user);
        errorMessages = validate.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
        assertEquals(1, errorMessages.size());
    }

    @Test
    void loginValidationTest() {
        user.setLogin("");
        Set<ConstraintViolation<User>> validate = validator.validate(user);
        Set<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
        assertTrue(errorMessages.contains("Логин не может быть пустым."));
        user.setLogin(" login ");
        validate = validator.validate(user);
        errorMessages = validate.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
        assertTrue(errorMessages.contains("Логин не может содержать пробелы."));
    }

    @Test
    void birthdayValidationTest() {
        user.setBirthday(LocalDate.of(2095, 12, 27));
        Set<ConstraintViolation<User>> validate = validator.validate(user);
        Set<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
        assertEquals(1, errorMessages.size());
    }
}
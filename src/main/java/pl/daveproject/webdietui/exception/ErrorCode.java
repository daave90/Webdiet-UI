package pl.daveproject.webdietui.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    EMPTY_USERNAME("Nazwa użytkownika nie może być pusta"),
    USERNAME_TOO_SHORT("Nazwa użytkownika musi mieć min 4 znaków"),
    EMPTY_FIRST_NAME("Imię nie może być puste"),
    FIRST_NAME_TOO_SHORT("Imię musi mieć min 2 znaki"),
    EMPTY_LAST_NAME("Nazwisko nie może być puste"),
    LAST_NAME_TOO_SHORT("Naziwsko musi mieć min 2 znaki"),
    EMPTY_PASSORD("Hasło nie może być puste"),
    PASSWORD_TOO_SHORT("Hasło musi mieć min 8 znaków"),
    PASSWORD_NO_UPPERCASE("Hasło musi mieć wielką literę"),
    PASSWORD_NO_LOWERCASE("Hasło musi mieć małą literę"),
    PASSWORD_NO_NUMBER("Hasło musi mieć cyfrę"),

    NO_AUTHENTICATED("Użytkownik niezalogowany"),
    LOGIN_FAILED("Błędny login lub hasło"),

    EMPTY_NAME("Nazwa nie może być pusta"),
    KCAL_LESS_THAN_0("Kcal nie może być mniejsze od 0"),

    EMPTY_TYPE("Typ nie może być pusty"),
    EMPTY_DESC("Opis nie może być pusty"),
    EMPTY_PRODUCTS("Przepis musi składać się z conajmniej 2 produktów"),

    DAYS_NUMBER_LESS_THAN_0("Liczba dni nie może być ujemna lub równa 0"),
    EMPTY_RECIPES("Lista zakupów musi składać się z conajmniej 1 przepisu");


    private String message;

    ErrorCode(String message) {
        this.message = message;
    }
}

package pl.daveproject.webdietui.validators;

import org.apache.commons.lang3.StringUtils;
import pl.daveproject.webdietui.exception.ErrorCode;
import pl.daveproject.webdietui.exception.WebdietRestException;
import pl.daveproject.webdietui.model.request.UserRequest;
import pl.daveproject.webdietui.model.request.WebdietRequest;

import java.util.function.IntPredicate;

public class UserRequestValidator implements FormValidator {

    @Override
    public void validate(WebdietRequest request) {
        UserRequest userRequest = (UserRequest) request;
        validateUsername(userRequest.getUsername());
        validateFirstName(userRequest.getFirstName());
        validateLastName(userRequest.getLastName());
        validatePassword(userRequest.getPassword());
    }

    private void validateUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new WebdietRestException(ErrorCode.EMPTY_USERNAME);
        }
        if (username.length() < 4) {
            throw new WebdietRestException(ErrorCode.USERNAME_TOO_SHORT);
        }
    }

    private void validateFirstName(String firstName) {
        if (StringUtils.isEmpty(firstName)) {
            throw new WebdietRestException(ErrorCode.EMPTY_FIRST_NAME);
        }
        if (firstName.length() < 2) {
            throw new WebdietRestException(ErrorCode.FIRST_NAME_TOO_SHORT);
        }
    }

    private void validateLastName(String lastName) {
        if (StringUtils.isEmpty(lastName)) {
            throw new WebdietRestException(ErrorCode.EMPTY_LAST_NAME);
        }
        if (lastName.length() < 2) {
            throw new WebdietRestException(ErrorCode.LAST_NAME_TOO_SHORT);
        }
    }

    private void validatePassword(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new WebdietRestException(ErrorCode.EMPTY_PASSORD);
        }
        if (password.length() < 8) {
            throw new WebdietRestException(ErrorCode.PASSWORD_TOO_SHORT);
        }
        if (checkPasswordPredicate(password, code -> !Character.isUpperCase(code))) {
            throw new WebdietRestException(ErrorCode.PASSWORD_NO_UPPERCASE);
        }
        if (checkPasswordPredicate(password, code -> !Character.isLowerCase(code))) {
            throw new WebdietRestException(ErrorCode.PASSWORD_NO_LOWERCASE);
        }
        if (checkPasswordPredicate(password, code -> !Character.isDigit(code))) {
            throw new WebdietRestException(ErrorCode.PASSWORD_NO_NUMBER);
        }
    }

    private boolean checkPasswordPredicate(String password, IntPredicate predicate) {
        return password.chars().map(ascii -> (char) ascii).allMatch(predicate);
    }
}

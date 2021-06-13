package pl.daveproject.webdietui.validators;

import pl.daveproject.webdietui.model.request.WebdietRequest;

public interface FormValidator {
    void validate(WebdietRequest request);
}

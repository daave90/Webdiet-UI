package pl.daveproject.webdietui.validators;

import org.apache.commons.lang3.StringUtils;
import pl.daveproject.webdietui.exception.ErrorCode;
import pl.daveproject.webdietui.exception.WebdietRestException;
import pl.daveproject.webdietui.model.product.Product;
import pl.daveproject.webdietui.model.request.WebdietRequest;

public class ProductRequestValidator implements FormValidator {

    @Override
    public void validate(WebdietRequest request) {
        Product product = (Product) request;
        validateName(product.getName());
        validateKcal(product.getKcal());
    }

    private void validateName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new WebdietRestException(ErrorCode.EMPTY_NAME);
        }
    }

    private void validateKcal(double kcal) {
        if (kcal < 0) {
            throw new WebdietRestException(ErrorCode.KCAL_LESS_THAN_0);
        }
    }
}

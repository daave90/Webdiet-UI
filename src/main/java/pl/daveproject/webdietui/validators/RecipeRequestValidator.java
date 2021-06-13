package pl.daveproject.webdietui.validators;

import org.apache.commons.lang3.StringUtils;
import pl.daveproject.webdietui.exception.ErrorCode;
import pl.daveproject.webdietui.exception.WebdietRestException;
import pl.daveproject.webdietui.model.enums.RecipeType;
import pl.daveproject.webdietui.model.product.GuidAndQuantity;
import pl.daveproject.webdietui.model.recipe.RecipeRequest;
import pl.daveproject.webdietui.model.request.WebdietRequest;

import java.util.List;

public class RecipeRequestValidator implements FormValidator {

    @Override
    public void validate(WebdietRequest request) {
        RecipeRequest recipeRequest = (RecipeRequest) request;
        validateName(recipeRequest.getName());
        validateType(recipeRequest.getType());
        validateDescription(recipeRequest.getDescription());
        validateProducts(recipeRequest.getProducts());
    }

    public void validateName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new WebdietRestException(ErrorCode.EMPTY_NAME);
        }
    }

    public void validateType(RecipeType type) {
        if (type == null) {
            throw new WebdietRestException(ErrorCode.EMPTY_TYPE);
        }
    }

    public void validateDescription(String description) {
        if (StringUtils.isEmpty(description)) {
            throw new WebdietRestException(ErrorCode.EMPTY_DESC);
        }
    }

    public void validateProducts(List<GuidAndQuantity> products) {
        if (products.isEmpty() || products.size() < 2) {
            throw new WebdietRestException(ErrorCode.EMPTY_PRODUCTS);
        }
    }
}

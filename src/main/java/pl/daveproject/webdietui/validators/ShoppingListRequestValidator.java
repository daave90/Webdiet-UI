package pl.daveproject.webdietui.validators;

import pl.daveproject.webdietui.exception.ErrorCode;
import pl.daveproject.webdietui.exception.WebdietRestException;
import pl.daveproject.webdietui.model.NameAndGuid;
import pl.daveproject.webdietui.model.request.WebdietRequest;
import pl.daveproject.webdietui.model.shoppinglist.ShoppingListRequest;

import java.util.List;

public class ShoppingListRequestValidator implements FormValidator {

    @Override
    public void validate(WebdietRequest request) {
        ShoppingListRequest shoppingList = (ShoppingListRequest) request;
        validateDays(shoppingList.getDaysNumber());
        validateRecipes(shoppingList.getRecipes());
    }

    private void validateDays(int days) {
        if (days <= 0) {
            throw new WebdietRestException(ErrorCode.KCAL_LESS_THAN_0);
        }
    }

    private void validateRecipes(List<NameAndGuid> recipes) {
        if (recipes.isEmpty()) {
            throw new WebdietRestException(ErrorCode.EMPTY_RECIPES);
        }
    }
}

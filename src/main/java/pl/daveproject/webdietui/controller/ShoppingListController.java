package pl.daveproject.webdietui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.daveproject.webdietui.exception.WebdietRestException;
import pl.daveproject.webdietui.model.shoppinglist.ShoppingList;
import pl.daveproject.webdietui.model.shoppinglist.ShoppingListRequest;
import pl.daveproject.webdietui.service.AuthService;
import pl.daveproject.webdietui.service.RecipeService;
import pl.daveproject.webdietui.service.ShoppingListService;
import pl.daveproject.webdietui.validators.FormValidator;
import pl.daveproject.webdietui.validators.ShoppingListRequestValidator;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ShoppingListController {

    private final AuthService authService;
    private final ShoppingListService shoppingListService;
    private final RecipeService recipeService;

    @GetMapping("/show-shopping-lists")
    public String getAllShoppingLists(Model model) {
        try {
            authService.checkIfAuthenticated();
            List<ShoppingList> shoppingLists = shoppingListService.getAllShoppingLists();
            model.addAttribute("shoppingLists", shoppingLists);
            return "shopping-lists";
        } catch (WebdietRestException exception) {
            return "redirect:/login";
        }
    }

    @GetMapping("/create-shopping-list")
    public String showCreateForm(Model model) {
        try {
            authService.checkIfAuthenticated();
            ShoppingListRequest request = new ShoppingListRequest();
            model.addAttribute("shoppingListRequest", request);
            model.addAttribute("allRecipes", shoppingListService.getRecipesForForm());
            model.addAttribute("submitText", "Dodaj listę zakupów");
            return "form-shopping-list";
        } catch (WebdietRestException exception) {
            return authService.handleNoAuthException(model, exception);
        }
    }

    @PostMapping("/create-shopping-list")
    public String createOrUpdateShoppingList(ShoppingListRequest request, @RequestParam("id") String guid, Model model) {
        try {
            FormValidator validator = new ShoppingListRequestValidator();
            validator.validate(request);
            shoppingListService.createOrUpdate(request, guid);
            return "redirect:/show-shopping-lists";
        } catch (WebdietRestException exception) {
            model.addAttribute("shoppingListRequest", request);
            model.addAttribute("allRecipes", shoppingListService.getRecipesForForm());
            model.addAttribute("submitText", "Dodaj listę zakupów");
        }
        return "redirect:/create-shopping-list";
    }

    @GetMapping("/delete-shopping-list")
    public String deleteShoppingList(Model model, @RequestParam("id") String guid) {
        try {
            authService.checkIfAuthenticated();
            shoppingListService.remove(guid);
            return "redirect:/show-shopping-lists";
        } catch (WebdietRestException exception) {
            return authService.handleNoAuthException(model, exception);
        }
    }

    @GetMapping("details-shopping-list")
    public String getShoppingListDetails(Model model, @RequestParam("id") String guid) {
        try {
            authService.checkIfAuthenticated();
            ShoppingList shoppingList = shoppingListService.getShoppingList(guid);
            model.addAttribute("shoppingList", shoppingList);
            model.addAttribute("products", recipeService.getProductsMap(shoppingList.getProductsAndWeight()));
            model.addAttribute("recipes", recipeService.getRecipesFromGuids(shoppingList.getRecipes()));
            return "details-shopping-list";
        } catch (WebdietRestException exception) {
            return authService.handleNoAuthException(model, exception);
        }
    }
}

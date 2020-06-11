package pl.daveproject.webdietui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.daveproject.webdietui.exception.WebdietRestException;
import pl.daveproject.webdietui.model.enums.RecipeType;
import pl.daveproject.webdietui.model.recipe.Recipe;
import pl.daveproject.webdietui.model.recipe.RecipeRequest;
import pl.daveproject.webdietui.service.AuthService;
import pl.daveproject.webdietui.service.RecipeService;
import pl.daveproject.webdietui.validators.FormValidator;
import pl.daveproject.webdietui.validators.RecipeRequestValidator;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RecipeController {

    private final AuthService authService;
    private final RecipeService recipeService;

    @GetMapping("/show-recipes")
    public String getAllRecipes(Model model) {
        try {
            authService.checkIfAuthenticated();
            List<Recipe> recipes = recipeService.getAllRecipes();
            model.addAttribute("recipes", recipes);
            return "recipes";
        } catch (WebdietRestException exception) {
            return "redirect:/login";
        }
    }

    @GetMapping("/create-recipe")
    public String showCreateForm(Model model) {
        try {
            authService.checkIfAuthenticated();
            RecipeRequest request = new RecipeRequest();
            model.addAttribute("recipeRequest", request);
            model.addAttribute("allTypes", RecipeType.values());
            model.addAttribute("allProducts", recipeService.getProductsForRecipeForm());
            model.addAttribute("submitText", "Dodaj przepis");
            return "form-recipe";
        } catch (WebdietRestException exception) {
            return authService.handleNoAuthException(model, exception);
        }
    }

    @PostMapping("/create-recipe")
    public String createOrUpdateRecipe(RecipeRequest request, @RequestParam("id") String guid, Model model) {
        try {
            FormValidator validator = new RecipeRequestValidator();
            validator.validate(request);
            recipeService.createOrUpdate(request, guid);
            return "redirect:/show-recipes";
        } catch (WebdietRestException exception) {
            model.addAttribute("recipeRequest", request);
            model.addAttribute("allTypes", RecipeType.values());
            model.addAttribute("allProducts", recipeService.getProductsForRecipeForm());
            model.addAttribute("submitText", "Dodaj przepis");
        }
        return "redirect:/create-recipe";
    }

    @GetMapping("/update-recipe")
    public String showUpdateForm(Model model, @RequestParam("id") String guid) {
        try {
            authService.checkIfAuthenticated();
            RecipeRequest request = recipeService.recipeToRequest(recipeService.getRecipe(guid));
            model.addAttribute("recipeRequest", request);
            model.addAttribute("allTypes", RecipeType.values());
            model.addAttribute("allProducts", recipeService.getProductsForRecipeForm());
            model.addAttribute("submitText", "Aktualizuj przepis");
            return "form-recipe";
        } catch (WebdietRestException exception) {
            return authService.handleNoAuthException(model, exception);
        }
    }

    @GetMapping("/delete-recipe")
    public String removeRecipe(Model model, @RequestParam("id") String guid) {
        try {
            authService.checkIfAuthenticated();
            recipeService.remove(guid);
            return "redirect:/show-recipes";
        } catch (WebdietRestException exception) {
            return authService.handleNoAuthException(model, exception);
        }
    }

    @GetMapping("/details-recipe")
    public String getRecipeDetails(Model model, @RequestParam("id") String guid) {
        try {
            authService.checkIfAuthenticated();
            Recipe recipe = recipeService.getRecipe(guid);
            model.addAttribute("recipe", recipe);
            model.addAttribute("products", recipeService.getProductsMap(recipe.getProducts()));
            return "details-recipe";
        } catch (WebdietRestException exception) {
            return authService.handleNoAuthException(model, exception);
        }
    }
}

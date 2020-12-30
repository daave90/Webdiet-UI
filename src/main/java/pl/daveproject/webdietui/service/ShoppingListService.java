package pl.daveproject.webdietui.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.daveproject.webdietui.exception.WebdietRestException;
import pl.daveproject.webdietui.model.NameAndGuid;
import pl.daveproject.webdietui.model.enums.RecipeType;
import pl.daveproject.webdietui.model.recipe.Recipe;
import pl.daveproject.webdietui.model.shoppinglist.ShoppingList;
import pl.daveproject.webdietui.model.shoppinglist.ShoppingListRequest;
import pl.daveproject.webdietui.util.AuthObject;
import pl.daveproject.webdietui.util.WebdietServerData;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ShoppingListService {

    private final WebdietServerData serverData;
    private final AuthObject authObject;
    private final RestTemplate restTemplate = new RestTemplate();
    private final RecipeService recipeService;

    public List<ShoppingList> getAllShoppingLists() {
        try {
            ResponseEntity<ShoppingList[]> response = restTemplate
                    .exchange(serverData.getShoppingListEndpoint(),
                            HttpMethod.GET,
                            getHttpEntity(new ShoppingList()),
                            ShoppingList[].class);
            return Arrays.asList(response.getBody());
        } catch (Exception exception) {
            throw new WebdietRestException(exception.getMessage());
        }
    }

    public ShoppingList getShoppingList(String guid) {
        try {
            ResponseEntity<ShoppingList> response = restTemplate
                    .exchange(serverData.getShoppingListEndpoint() + "/" + guid,
                            HttpMethod.GET,
                            getHttpEntity(new ShoppingList()),
                            ShoppingList.class);
            return response.getBody();
        } catch (Exception exception) {
            throw new WebdietRestException(exception.getMessage());
        }
    }

    private HttpEntity<ShoppingList> getHttpEntity(ShoppingList shoppingList) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authObject.getToken());
        return new HttpEntity<>(shoppingList, headers);
    }

    private HttpEntity<ShoppingListRequest> getHttpRequestEntity(ShoppingListRequest shoppingList) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authObject.getToken());
        return new HttpEntity<>(shoppingList, headers);
    }

    public List<Recipe> getRecipesForForm() {
        List<Recipe> recipes = new ArrayList<>();
        Recipe emptyRecipe = new Recipe();
        emptyRecipe.setName("Przepis");
        emptyRecipe.setType(RecipeType.EMPTY);
        recipes.add(emptyRecipe);
        recipes.addAll(recipeService.getAllRecipes());
        return recipes;
    }

    public void createOrUpdate(ShoppingListRequest request, String guid) {
        try {
            request = mapRecipes(request);
            if (StringUtils.isEmpty(guid)) {
                restTemplate.exchange(serverData.getShoppingListEndpoint(),
                        HttpMethod.POST,
                        getHttpRequestEntity(request),
                        ShoppingList.class);
            } else {
                restTemplate.exchange(serverData.getShoppingListEndpoint() + "/" + guid,
                        HttpMethod.PUT,
                        getHttpRequestEntity(request),
                        ShoppingList.class);
            }
        } catch (Exception exception) {
            throw new WebdietRestException(exception.getMessage());
        }
    }

    private ShoppingListRequest mapRecipes(ShoppingListRequest request) {
        ShoppingListRequest shoppingListRequest = new ShoppingListRequest();
        shoppingListRequest.getRecipes().clear();
        Set<NameAndGuid> recipes = new HashSet<>();
        for (NameAndGuid guid : request.getRecipes()) {
            if (StringUtils.isEmpty(guid.getGuid())) {
                break;
            }
            Recipe recipe = recipeService.getRecipe(guid.getGuid());
            NameAndGuid nameAndGuid = new NameAndGuid();
            nameAndGuid.setGuid(guid.getGuid());
            nameAndGuid.setName(recipe.getName());
            recipes.add(nameAndGuid);
        }
        shoppingListRequest.setGuid(request.getGuid());
        shoppingListRequest.setDaysNumber(request.getDaysNumber());
        shoppingListRequest.setRecipes(new ArrayList<>(recipes));
        return shoppingListRequest;
    }

    public void remove(String guid) {
        try {
            restTemplate.exchange(serverData.getShoppingListEndpoint() + "/" + guid,
                    HttpMethod.DELETE,
                    getHttpEntity(new ShoppingList()),
                    ShoppingList.class);
        } catch (Exception exception) {
            throw new WebdietRestException(exception.getMessage());
        }
    }
}

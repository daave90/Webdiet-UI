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
import pl.daveproject.webdietui.model.product.GuidAndQuantity;
import pl.daveproject.webdietui.model.product.Product;
import pl.daveproject.webdietui.model.product.ProductAndQuantity;
import pl.daveproject.webdietui.model.recipe.Recipe;
import pl.daveproject.webdietui.model.recipe.RecipeRequest;
import pl.daveproject.webdietui.util.AuthObject;
import pl.daveproject.webdietui.util.WebdietServerData;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final ProductService productService;
    private final WebdietServerData serverData;
    private final AuthObject authObject;
    private final RestTemplate restTemplate = new RestTemplate();

    public void createOrUpdate(RecipeRequest request, String guid) {
        Recipe recipe = requestToRecipeMap(request);
        try {
            if (StringUtils.isEmpty(guid)) {
                restTemplate.exchange(serverData.getRecipeEndpoint(),
                        HttpMethod.POST,
                        getHttpEntity(recipe),
                        Recipe.class);
            } else {
                restTemplate.exchange(serverData.getRecipeEndpoint() + "/" + guid,
                        HttpMethod.PUT,
                        getHttpEntity(recipe),
                        Recipe.class);
            }
        } catch (Exception exception) {
            throw new WebdietRestException(exception.getMessage());
        }
    }

    public List<Recipe> getAllRecipes() {
        try {
            ResponseEntity<Recipe[]> response = restTemplate
                    .exchange(serverData.getRecipeEndpoint(),
                            HttpMethod.GET,
                            getHttpEntity(new Recipe()),
                            Recipe[].class);
            List<Recipe> recipes = Arrays.asList(response.getBody());
            Collections.sort(recipes);
            return recipes;
        } catch (Exception exception) {
            throw new WebdietRestException(exception.getMessage());
        }
    }

    public List<Recipe> getRecipesFromGuids(List<String> guids) {
        return guids.stream()
                .map(guid -> getRecipe(guid))
                .collect(Collectors.toList());
    }

    public Recipe getRecipe(String guid) {
        try {
            ResponseEntity<Recipe> response = restTemplate
                    .exchange(serverData.getRecipeEndpoint() + "/" + guid,
                            HttpMethod.GET,
                            getHttpEntity(new Recipe()),
                            Recipe.class);
            return response.getBody();
        } catch (Exception exception) {
            throw new WebdietRestException(exception.getMessage());
        }
    }

    public void remove(String guid) {
        try {
            restTemplate.exchange(serverData.getRecipeEndpoint() + "/" + guid,
                    HttpMethod.DELETE,
                    getHttpEntity(new Recipe()),
                    Recipe.class);
        } catch (Exception exception) {
            throw new WebdietRestException(exception.getMessage());
        }
    }

    public List<Product> getProductsForRecipeForm() {
        List<Product> products = new ArrayList<>();
        Product emptyProduct = new Product();
        emptyProduct.setName("Produkt");
        products.add(emptyProduct);
        products.addAll(productService.getAllProducts());
        return products;
    }

    public RecipeRequest recipeToRequest(Recipe recipe) {
        RecipeRequest request = new RecipeRequest();
        request.setGuid(recipe.getGuid());
        request.setName(recipe.getName());
        request.setDescription(recipe.getDescription());
        request.setType(recipe.getType());
        request.setTotalKcal(recipe.getTotalKcal());

        List<GuidAndQuantity> products = new ArrayList<>();
        for (Map.Entry<String, Long> entry : recipe.getProducts().entrySet()) {
            GuidAndQuantity productEntry = new GuidAndQuantity();
            productEntry.setGuid(entry.getKey());
            productEntry.setQuantity(entry.getValue());
            products.add(productEntry);
        }

        if (products.size() < RecipeRequest.PRODUCT_ENTRY_SIZE) {
            while (products.size() != RecipeRequest.PRODUCT_ENTRY_SIZE) {
                products.add(new GuidAndQuantity());
            }
        }

        request.setProducts(products);
        return request;
    }

    private Recipe requestToRecipeMap(RecipeRequest request) {
        Recipe recipe = new Recipe();
        recipe.setName(request.getName());
        recipe.setDescription(request.getDescription());
        recipe.setType(request.getType());
        Map<String, Long> products = new HashMap<>();
        for (GuidAndQuantity entry : request.getProducts()) {
            if (StringUtils.isEmpty(entry.getGuid())) {
                break;
            }
            products.put(entry.getGuid(), entry.getQuantity());
        }
        recipe.setProducts(products);
        return recipe;
    }

    private HttpEntity<Recipe> getHttpEntity(Recipe recipe) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authObject.getToken());
        return new HttpEntity<>(recipe, headers);
    }

    public List<ProductAndQuantity> getProductsMap(Map<String, Long> guids) {
        List<ProductAndQuantity> products = new ArrayList<>();
        for (Map.Entry<String, Long> entry : guids.entrySet()) {
            ProductAndQuantity product = new ProductAndQuantity();
            product.setProduct(productService.getProduct(entry.getKey()));
            product.setQuantity(entry.getValue());
            products.add(product);
        }
        return products;
    }
}

package pl.daveproject.webdietui.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class WebdietServerData {

    @Value("${webdiet.host}")
    private String host;

    @Value("${webdiet.port}")
    private String port;

    @Value("${webdiet.ssl}")
    private boolean ssl;

    private final String loginEndpoint = "/login";
    private final String logoutEndpoint = "/logout";
    private final String userEndpoint = "/users";
    private final String productEndpoint = "/products";
    private final String recipeEndpoint = "/recipes";
    private final String shoppingListEndpoint = "/shopping-lists";

    private String getEndpointPrefix() {
        if(ssl){
             return "https://" + host + ":" + port;
        }
        return "http://" + host + ":" + port;
    }

    public String getUserEndpoint() {
        return getEndpointPrefix() + userEndpoint;
    }

    public String getLoginEndpoint() {
        return getEndpointPrefix() + loginEndpoint;
    }

    public String getLogoutEndpoint() {
        return getEndpointPrefix() + logoutEndpoint;
    }

    public String getProductEndpoint() {
        return getEndpointPrefix() + productEndpoint;
    }

    public String getRecipeEndpoint() {
        return getEndpointPrefix() + recipeEndpoint;
    }

    public String getShoppingListEndpoint() {
        return getEndpointPrefix() + shoppingListEndpoint;
    }
}

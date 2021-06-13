package pl.daveproject.webdietui.model.shoppinglist;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ShoppingList {
    private String guid;
    private int daysNumber;
    private Map<String, Long> productsAndWeight = new HashMap<>();
    private List<String> recipes = new ArrayList<>();
}

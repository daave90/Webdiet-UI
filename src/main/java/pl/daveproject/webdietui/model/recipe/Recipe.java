package pl.daveproject.webdietui.model.recipe;

import lombok.Data;
import pl.daveproject.webdietui.model.enums.RecipeType;

import java.util.HashMap;
import java.util.Map;

@Data
public class Recipe implements Comparable<Recipe> {
    private String guid;
    private String name;
    private double totalKcal;
    private RecipeType type;
    private String description;
    private Map<String, Long> products = new HashMap<>();

    @Override
    public int compareTo(Recipe o) {
        return this.getName().compareTo(o.getName());
    }
}

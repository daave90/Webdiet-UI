package pl.daveproject.webdietui.model.recipe;

import lombok.Data;
import pl.daveproject.webdietui.model.enums.RecipeType;
import pl.daveproject.webdietui.model.product.GuidAndQuantity;
import pl.daveproject.webdietui.model.request.WebdietRequest;

import java.util.ArrayList;
import java.util.List;

@Data
public class RecipeRequest implements WebdietRequest {

    public static final int PRODUCT_ENTRY_SIZE = 15;

    private String guid;
    private long ver;
    private String name;
    private double totalKcal;
    private RecipeType type;
    private String description;
    private List<GuidAndQuantity> products = new ArrayList<>();

    public RecipeRequest() {
        this.guid = "";
        for (int i = 0; i < PRODUCT_ENTRY_SIZE; i++) {
            products.add(new GuidAndQuantity());
        }
    }
}

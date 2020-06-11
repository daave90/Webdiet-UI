package pl.daveproject.webdietui.model.shoppinglist;

import lombok.Data;
import pl.daveproject.webdietui.model.NameAndGuid;
import pl.daveproject.webdietui.model.request.WebdietRequest;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShoppingListRequest implements WebdietRequest {

    public static final int RECIPE_ENTRY_SIZE = 8;

    private String guid;
    private int daysNumber;
    private List<NameAndGuid> recipes = new ArrayList<>();

    public ShoppingListRequest() {
        this.guid = "";
        for (int i = 0; i < RECIPE_ENTRY_SIZE; i++) {
            recipes.add(new NameAndGuid());
        }
    }
}

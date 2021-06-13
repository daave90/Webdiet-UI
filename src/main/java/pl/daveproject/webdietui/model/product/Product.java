package pl.daveproject.webdietui.model.product;

import lombok.Data;
import pl.daveproject.webdietui.model.request.WebdietRequest;

@Data
public class Product implements WebdietRequest, Comparable<Product> {
    private String guid = "";
    private String name = "";
    private double kcal;

    @Override
    public int compareTo(Product o) {
        return this.getName().compareTo(o.getName());
    }
}

package pl.daveproject.webdietui.model.product;

import lombok.Data;

@Data
public class ProductAndQuantity {
    private Product product;
    private long quantity;
}

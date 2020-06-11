package pl.daveproject.webdietui.model.enums;

import lombok.Getter;

@Getter
public enum RecipeType {
    SNIADANIE("Śniadanie"),
    DRUGIE_SNIADANIE("Drugie śniadanie"),
    OBIAD("Obiad"),
    PODWIECZOREK("Podwieczorek"),
    KOLACJA("Kolacja");

    private String value;

    RecipeType(String value) {
        this.value = value;
    }
}

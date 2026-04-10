package hello.itemservice.domain.item;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
// @ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000")
public class Item {

    private Long id;

    @NotNull
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1_000_000) // `@Range`는 hibernate 구현체에서만 동작함!
    private Integer price;

    @Max(9999)
    private Integer quantity;

    public Item() {}

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}

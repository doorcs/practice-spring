package hello.itemservice.domain.item;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
// @ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000")
public class Item {

    @NotNull(groups = UpdateCheck.class)
    private Long id;

    // 문자열에는 `@NotNull` 보다 `@NotBlank` 가 맞지 않나..?
    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;

    @Range(
            min = 1000,
            max = 1_000_000,
            groups = {SaveCheck.class, UpdateCheck.class}) // `@Range`는 hibernate 구현체에서만 동작함!
    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;

    @Max(value = 9999, groups = SaveCheck.class)
    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    private Integer quantity;

    public Item() {}

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}

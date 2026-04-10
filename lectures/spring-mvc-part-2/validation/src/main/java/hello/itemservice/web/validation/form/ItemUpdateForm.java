package hello.itemservice.web.validation.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class ItemUpdateForm {

    @NotNull
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    // @Max(9999)
    // 수정에서는 수량 제한을 두지 않기로 함
    @NotNull // 이건 남기는게 맞지 않나?
    private Integer quantity;
}

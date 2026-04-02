package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    // @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity, // int, Integer 둘 다 가능 (null 핸들링 방식에서 차이가 있다)
                            Model model) {

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);
        model.addAttribute("item", item);

        return "basic/item";
    }

    // @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {

        itemRepository.save(item);
        // model.addAttribute("item", item); `@ModelAttribute`에서 자동으로 `.addAttribute()`를 수행해주기 때문에 생략 가능!

        return "basic/item";
    }

    // @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {

        itemRepository.save(item);
        // `@ModelAttribute`에 괄호로 속성 이름을 지정해주지 않으면, `클래스명의 첫 글자만 소문자로 변경`해서 등록해준다
        // 여기서는 `Item` 클래스니까 `item` 으로 모델에 등록됨!

        return "basic/item";
    }

    // @PostMapping("/add")
    public String addItemV4(Item item) {

        itemRepository.save(item);
        // `@ModelAttribute` 어노테이션 자체를 생략할 수도 있지만, 권장하는 방식은 아님 (이렇게까지 생략하기보다는 명확하게 표현해주는게 낫다!)

        return "basic/item";
    }

    // @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item) {

        itemRepository.save(item);

        return "redirect:/basic/items" + item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        // 1. `{itemId} 처럼 `redirect URL`에 포함되는 속성은 `URL 인코딩`을 처리하고 치환해준다 (여기서는 {itemId}를 3으로 치환해줌)
        // 2. `status` 처럼 `redirect URL`에 포함되지 않는 속성은 리다이렉트 시 `쿼리 파라미터`로 같이 넘겨준다 (여기서는 ?status=true)
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}"; // redirect 안에서도 `@PathVariable`을 사용할 수 있다
    }

    // 테스트용 데이터 추가
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}

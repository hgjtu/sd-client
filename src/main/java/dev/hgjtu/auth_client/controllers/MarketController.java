package dev.hgjtu.auth_client.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/market")
public class MarketController {
    @GetMapping
    public String market() {
        return "market/market-dashboard";
    }

    @GetMapping("/category/{slug}")
    public String category(@PathVariable String slug,
                           @RequestParam(defaultValue = "buy") String mode,
                           Model model) {
//        // Получаем человекочитаемое имя категории (можно сделать маппинг в сервисе или enum)
//        String categoryName = itemService.getCategoryDisplayName(slug);
//
//        // Получаем список товаров для выбранной категории и режима
//        List<ItemMinResponse> items = itemService.getItemsByCategoryAndMode(slug, mode);

        // Передаём данные в шаблон
        model.addAttribute("categoryName", slug);
//        model.addAttribute("items", items);
        model.addAttribute("mode", mode);
        return "market/category";
    }
}

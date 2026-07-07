package uk.co.graham.shopping.list;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import uk.co.graham.shopping.list.item.ShoppingListItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ShoppingListController {

    private final ShoppingListItemService shoppingListItemService;
    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListItemService shoppingListItemService,
            ShoppingListService shoppingListService) {
        this.shoppingListItemService = shoppingListItemService;
        this.shoppingListService = shoppingListService;
    }

    @GetMapping("/")
    public String viewList(Model model) {
        ShoppingList shoppingList = shoppingListService.getCurrentShoppingList();

        int totalPriceInPence = shoppingListItemService.getTotalPriceInPence();
        Integer budgetInPence = shoppingList.getBudgetInPence();

        boolean budgetExceeded = budgetInPence != null && totalPriceInPence > budgetInPence;

        model.addAttribute("shoppingListName", shoppingList.getName());
        model.addAttribute("items", shoppingListItemService.getItems());
        model.addAttribute("totalPriceInPence", totalPriceInPence);
        model.addAttribute("budgetInPence", budgetInPence);
        model.addAttribute("budgetExceeded", budgetExceeded);

        return "shopping-list";
    }

    @PostMapping("/items")
    public String addItem(@RequestParam String name,
            @RequestParam String priceInPounds,
            RedirectAttributes redirectAttributes) {

        try {
            Integer priceInPence = parsePoundsToPence(priceInPounds, "Price");

            shoppingListItemService.addItem(name, priceInPence);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/items/{id}/delete")
    public String deleteItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            shoppingListItemService.removeItem(id);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/items/{id}/toggle")
    public String togglePurchased(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            shoppingListItemService.togglePurchased(id);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/items/{id}/move-up")
    public String moveItemUp(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            shoppingListItemService.moveItemUp(id);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/items/{id}/move-down")
    public String moveItemDown(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            shoppingListItemService.moveItemDown(id);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/budget")
    public String updateBudget(
            @RequestParam(required = false) String budgetInPounds,
            RedirectAttributes redirectAttributes) {
        try {
            Integer budgetInPence = parsePoundsToPence(budgetInPounds, "Budget");

            shoppingListService.updateBudget(budgetInPence);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/";
    }

    private Integer parsePoundsToPence(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            BigDecimal pounds = new BigDecimal(value.trim());

            if (pounds.scale() > 2) {
                throw new IllegalArgumentException(fieldName + " must not have more than 2 decimal places.");
            }

            if (pounds.signum() < 0) {
                throw new IllegalArgumentException(fieldName + " cannot be negative.");
            }

            return pounds
                    .movePointRight(2)
                    .intValueExact();

        } catch (NumberFormatException | ArithmeticException ex) {
            throw new IllegalArgumentException(fieldName + " must be a valid amount, for example 1.45.");
        }
    }
}
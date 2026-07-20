package uk.co.graham.shopping.list;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.graham.shopping.security.CurrentUserService;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final CurrentUserService currentUserService;

    public ShoppingListService(ShoppingListRepository shoppingListRepository, CurrentUserService currentUserService) {
        this.shoppingListRepository = shoppingListRepository;
        this.currentUserService = currentUserService;
    }

    /**
     * Retrieves the shopping list for the authenticated user.
     *
     * @return the current shopping list for the authenticated user
     */
    @Transactional(readOnly = true)
    public ShoppingList getCurrentShoppingList() {
        String username = currentUserService.getCurrentUsername();

        return shoppingListRepository
                .findFirstByAppUser_Username(username)
                .orElseThrow(() -> new IllegalStateException("Shopping list not found for user: " + username));
    }

    @Transactional
    public void updateBudget(Integer budgetInPence) {
        if (budgetInPence != null && budgetInPence < 0) {
            throw new IllegalArgumentException("Budget cannot be negative.");
        }

        ShoppingList shoppingList = getCurrentShoppingList();
        shoppingList.setBudgetInPence(budgetInPence);
    }
}
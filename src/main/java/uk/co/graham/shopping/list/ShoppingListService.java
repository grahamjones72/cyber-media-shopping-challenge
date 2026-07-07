package uk.co.graham.shopping.list;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.graham.shopping.config.DefaultShoppingListConfig;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListService(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    /**
     * Retrieves the default shopping list for the default, unauthenticated user.
     * This method is intended for use while user authentication is not yet
     * implemented.
     *
     * Later, this can be changed to retrieve the shopping list for the
     * currently authenticated user.
     *
     * @return the current shopping list
     */
    @Transactional(readOnly = true)
    public ShoppingList getCurrentShoppingList() {
        return shoppingListRepository
                .findByNameAndAppUserUsername(
                        DefaultShoppingListConfig.DEFAULT_SHOPPING_LIST,
                        DefaultShoppingListConfig.DEFAULT_USERNAME)
                .orElseThrow(() -> new IllegalStateException("Default shopping list not found"));
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
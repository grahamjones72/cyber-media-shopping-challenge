package uk.co.graham.shopping.list;

import org.springframework.stereotype.Service;

import uk.co.graham.shopping.config.DataSeeder;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListService(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    public ShoppingList getDefaultShoppingList() {
        return shoppingListRepository
            .findByNameAndAppUserUsername(DataSeeder.DEFAULT_SHOPPING_LIST, DataSeeder.DEFAULT_USERNAME)
            .orElseThrow(() -> new RuntimeException("Default shopping list not found"));
    }

}

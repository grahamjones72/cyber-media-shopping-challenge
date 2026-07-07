package uk.co.graham.shopping.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import uk.co.graham.shopping.list.ShoppingList;
import uk.co.graham.shopping.list.ShoppingListRepository;
import uk.co.graham.shopping.user.AppUser;
import uk.co.graham.shopping.user.AppUserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final ShoppingListRepository shoppingListRepository;

    public DataSeeder(
            AppUserRepository appUserRepository,
            ShoppingListRepository shoppingListRepository) {
        this.appUserRepository = appUserRepository;
        this.shoppingListRepository = shoppingListRepository;
    }

    @Override
    public void run(String... args) {
        AppUser user = appUserRepository.findByUsername(DefaultShoppingListConfig.DEFAULT_USERNAME)
                .orElseGet(() -> appUserRepository.save(new AppUser(DefaultShoppingListConfig.DEFAULT_USERNAME,
                        DefaultShoppingListConfig.DEFAULT_USER_DISPLAY_NAME)));

        shoppingListRepository
                .findByNameAndAppUserUsername(DefaultShoppingListConfig.DEFAULT_SHOPPING_LIST,
                        DefaultShoppingListConfig.DEFAULT_USERNAME)
                .orElseGet(() -> shoppingListRepository
                        .save(new ShoppingList(user, DefaultShoppingListConfig.DEFAULT_SHOPPING_LIST)));
    }
}
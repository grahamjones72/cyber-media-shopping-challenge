package uk.co.graham.shopping.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import uk.co.graham.shopping.list.ShoppingList;
import uk.co.graham.shopping.list.ShoppingListRepository;
import uk.co.graham.shopping.user.AppUser;
import uk.co.graham.shopping.user.AppUserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    public static final String DEFAULT_USERNAME = "default-user";
    private static final String DEFAULT_USER_DISPLAY_NAME = "Default User";
    public static final String DEFAULT_SHOPPING_LIST = "My Shopping List";

    private final AppUserRepository appUserRepository;
    private final ShoppingListRepository shoppingListRepository;

    public DataSeeder(
            AppUserRepository appUserRepository,
            ShoppingListRepository shoppingListRepository
    ) {
        this.appUserRepository = appUserRepository;
        this.shoppingListRepository = shoppingListRepository;
    }

    @Override
    public void run(String... args) {
        AppUser user = appUserRepository.findByUsername(DEFAULT_USERNAME)
                .orElseGet(() -> appUserRepository.save(new AppUser(DEFAULT_USERNAME, DEFAULT_USER_DISPLAY_NAME)));

        shoppingListRepository.findByNameAndAppUserUsername(DEFAULT_SHOPPING_LIST, DEFAULT_USERNAME)
                .orElseGet(() -> shoppingListRepository.save(new ShoppingList(user, DEFAULT_SHOPPING_LIST)));
    }
}
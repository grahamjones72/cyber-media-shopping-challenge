package uk.co.graham.shopping.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import uk.co.graham.shopping.list.ShoppingList;
import uk.co.graham.shopping.list.ShoppingListRepository;
import uk.co.graham.shopping.user.AppUser;
import uk.co.graham.shopping.user.AppUserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

        private final AppUserRepository appUserRepository;
        private final ShoppingListRepository shoppingListRepository;
        private final PasswordEncoder passwordEncoder;

        public DataSeeder(
                        AppUserRepository appUserRepository,
                        ShoppingListRepository shoppingListRepository,
                        PasswordEncoder passwordEncoder) {
                this.appUserRepository = appUserRepository;
                this.shoppingListRepository = shoppingListRepository;
                this.passwordEncoder = passwordEncoder;
        }

        @Override
        public void run(String... args) {
                createUserWithShoppingList("aaron", "Aaron", "password", "Aaron's Shopping List");
                createUserWithShoppingList("david", "David", "password", "David's Shopping List");
                createUserWithShoppingList("graham", "Graham", "password", "Graham's Shopping List");
        }

        private void createUserWithShoppingList(String username, String displayName, String rawPassword,
                        String shoppingListName) {

                if (appUserRepository.findByUsername(username).isPresent()) {
                        return;
                }

                AppUser user = new AppUser(username, displayName, passwordEncoder.encode(rawPassword));

                appUserRepository.save(user);

                ShoppingList shoppingList = new ShoppingList(user, shoppingListName);
                shoppingListRepository.save(shoppingList);
        }
}
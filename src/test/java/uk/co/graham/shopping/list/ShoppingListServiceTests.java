package uk.co.graham.shopping.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.co.graham.shopping.config.DefaultShoppingListConfig;
import uk.co.graham.shopping.user.AppUser;

@ExtendWith(MockitoExtension.class)
class ShoppingListServiceTests {

    @Mock
    private ShoppingListRepository shoppingListRepository;

    private ShoppingListService shoppingListService;

    private AppUser defaultUser;
    private ShoppingList defaultShoppingList;

    @BeforeEach
    void setUp() {
        shoppingListService = new ShoppingListService(shoppingListRepository);

        defaultUser = new AppUser(DefaultShoppingListConfig.DEFAULT_USERNAME,
                DefaultShoppingListConfig.DEFAULT_USER_DISPLAY_NAME);

        defaultShoppingList = new ShoppingList(defaultUser, DefaultShoppingListConfig.DEFAULT_SHOPPING_LIST);
    }

    @Test
    void getCurrentShoppingListReturnsDefaultShoppingList() {
        when(shoppingListRepository.findByNameAndAppUserUsername(
                DefaultShoppingListConfig.DEFAULT_SHOPPING_LIST,
                DefaultShoppingListConfig.DEFAULT_USERNAME)).thenReturn(Optional.of(defaultShoppingList));

        ShoppingList result = shoppingListService.getCurrentShoppingList();

        assertEquals(defaultShoppingList, result);
    }

    @Test
    void getCurrentShoppingListThrowsExceptionWhenDefaultListDoesNotExist() {
        when(shoppingListRepository.findByNameAndAppUserUsername(
                DefaultShoppingListConfig.DEFAULT_SHOPPING_LIST,
                DefaultShoppingListConfig.DEFAULT_USERNAME)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> shoppingListService.getCurrentShoppingList());
    }

    @Test
    void updateBudgetSetsBudgetInPence() {
        when(shoppingListRepository.findByNameAndAppUserUsername(
                DefaultShoppingListConfig.DEFAULT_SHOPPING_LIST,
                DefaultShoppingListConfig.DEFAULT_USERNAME)).thenReturn(Optional.of(defaultShoppingList));

        shoppingListService.updateBudget(2500);

        assertEquals(2500, defaultShoppingList.getBudgetInPence());
    }

    @Test
    void updateBudgetAllowsNullBudget() {
        defaultShoppingList.setBudgetInPence(2500);

        when(shoppingListRepository.findByNameAndAppUserUsername(
                DefaultShoppingListConfig.DEFAULT_SHOPPING_LIST,
                DefaultShoppingListConfig.DEFAULT_USERNAME)).thenReturn(Optional.of(defaultShoppingList));

        shoppingListService.updateBudget(null);

        assertEquals(null, defaultShoppingList.getBudgetInPence());
    }

    @Test
    void updateBudgetThrowsExceptionWhenBudgetIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> shoppingListService.updateBudget(-1));
    }
}
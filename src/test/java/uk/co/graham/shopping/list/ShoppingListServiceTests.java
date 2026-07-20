package uk.co.graham.shopping.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.co.graham.shopping.security.CurrentUserService;
import uk.co.graham.shopping.user.AppUser;

@ExtendWith(MockitoExtension.class)
class ShoppingListServiceTests {

    private static final String CURRENT_USERNAME = "graham";

    @Mock
    private ShoppingListRepository shoppingListRepository;

    @Mock
    private CurrentUserService currentUserService;

    private ShoppingListService shoppingListService;

    private AppUser currentUser;
    private ShoppingList currentUserShoppingList;

    @BeforeEach
    void setUp() {
        shoppingListService = new ShoppingListService(shoppingListRepository, currentUserService);

        currentUser = new AppUser(
                CURRENT_USERNAME,
                "Graham",
                "testpassword");

        currentUserShoppingList = new ShoppingList(
                currentUser,
                "Graham's Shopping List");
    }

    @Test
    void getCurrentShoppingListReturnsCurrentUsersShoppingList() {
        when(currentUserService.getCurrentUsername())
                .thenReturn(CURRENT_USERNAME);

        when(shoppingListRepository.findFirstByAppUser_Username(CURRENT_USERNAME))
                .thenReturn(Optional.of(currentUserShoppingList));

        ShoppingList result = shoppingListService.getCurrentShoppingList();

        assertSame(currentUserShoppingList, result);
    }

    @Test
    void getCurrentShoppingListThrowsExceptionWhenCurrentUserHasNoShoppingList() {
        when(currentUserService.getCurrentUsername())
                .thenReturn(CURRENT_USERNAME);

        when(shoppingListRepository.findFirstByAppUser_Username(CURRENT_USERNAME))
                .thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> shoppingListService.getCurrentShoppingList());
    }

    @Test
    void updateBudgetSetsBudgetInPence() {
        when(currentUserService.getCurrentUsername())
                .thenReturn(CURRENT_USERNAME);

        when(shoppingListRepository.findFirstByAppUser_Username(CURRENT_USERNAME))
                .thenReturn(Optional.of(currentUserShoppingList));

        shoppingListService.updateBudget(2500);

        assertEquals(2500, currentUserShoppingList.getBudgetInPence());
    }

    @Test
    void updateBudgetAllowsNullBudget() {
        currentUserShoppingList.setBudgetInPence(2500);

        when(currentUserService.getCurrentUsername())
                .thenReturn(CURRENT_USERNAME);

        when(shoppingListRepository.findFirstByAppUser_Username(CURRENT_USERNAME))
                .thenReturn(Optional.of(currentUserShoppingList));

        shoppingListService.updateBudget(null);

        assertNull(currentUserShoppingList.getBudgetInPence());
    }

    @Test
    void updateBudgetThrowsExceptionWhenBudgetIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> shoppingListService.updateBudget(-1));
    }
}
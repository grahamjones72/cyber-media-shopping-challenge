package uk.co.graham.shopping.list.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.co.graham.shopping.list.ShoppingList;
import uk.co.graham.shopping.list.ShoppingListService;
import uk.co.graham.shopping.user.AppUser;
import uk.co.graham.shopping.config.DefaultShoppingListConfig;

@ExtendWith(MockitoExtension.class)
class ShoppingListItemServiceTests {

    @Mock
    private ShoppingListItemRepository shoppingListItemRepository;

    @Mock
    private ShoppingListService shoppingListService;

    private ShoppingListItemService shoppingListItemService;

    private AppUser defaultUser;
    private ShoppingList defaultShoppingList;

    @BeforeEach
    void setUp() {
        shoppingListItemService = new ShoppingListItemService(
                shoppingListItemRepository,
                shoppingListService);

        defaultUser = new AppUser(DefaultShoppingListConfig.DEFAULT_USERNAME,
                DefaultShoppingListConfig.DEFAULT_USER_DISPLAY_NAME);

        defaultShoppingList = new ShoppingList(defaultUser, DefaultShoppingListConfig.DEFAULT_SHOPPING_LIST);
    }

    @Test
    void addItemSavesNewItemWithPrice() {
        when(shoppingListService.getCurrentShoppingList()).thenReturn(defaultShoppingList);
        when(shoppingListItemRepository.existsByShoppingListAndNormalisedName(defaultShoppingList, "milk"))
                .thenReturn(false);
        when(shoppingListItemRepository.findTopByShoppingListOrderByDisplayOrderDesc(defaultShoppingList))
                .thenReturn(Optional.empty());

        shoppingListItemService.addItem("Milk", 145);

        ArgumentCaptor<ShoppingListItem> itemCaptor = ArgumentCaptor.forClass(ShoppingListItem.class);

        verify(shoppingListItemRepository).save(itemCaptor.capture());

        ShoppingListItem savedItem = itemCaptor.getValue();

        assertEquals("Milk", savedItem.getName());
        assertEquals("milk", savedItem.getNormalisedName());
        assertEquals(145, savedItem.getPriceInPence());
        assertEquals(1, savedItem.getDisplayOrder());
        assertFalse(savedItem.isPurchased());
        assertEquals(defaultShoppingList, savedItem.getShoppingList());
    }

    @Test
    void addItemSetsNextDisplayOrder() {
        ShoppingListItem existingItem = new ShoppingListItem();
        existingItem.setDisplayOrder(3);

        when(shoppingListService.getCurrentShoppingList()).thenReturn(defaultShoppingList);
        when(shoppingListItemRepository.existsByShoppingListAndNormalisedName(defaultShoppingList, "eggs"))
                .thenReturn(false);
        when(shoppingListItemRepository.findTopByShoppingListOrderByDisplayOrderDesc(defaultShoppingList))
                .thenReturn(Optional.of(existingItem));

        shoppingListItemService.addItem("Eggs", 250);

        ArgumentCaptor<ShoppingListItem> itemCaptor = ArgumentCaptor.forClass(ShoppingListItem.class);

        verify(shoppingListItemRepository).save(itemCaptor.capture());

        assertEquals(4, itemCaptor.getValue().getDisplayOrder());
    }

    @Test
    void addItemThrowsExceptionForBlankName() {
        assertThrows(IllegalArgumentException.class, () -> shoppingListItemService.addItem("   ", 100));

        verify(shoppingListItemRepository, never()).save(any());
    }

    @Test
    void addItemThrowsExceptionForNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> shoppingListItemService.addItem("Milk", -1));

        verify(shoppingListItemRepository, never()).save(any());
    }

    @Test
    void addItemThrowsExceptionForDuplicateNormalisedName() {
        when(shoppingListService.getCurrentShoppingList()).thenReturn(defaultShoppingList);
        when(shoppingListItemRepository.existsByShoppingListAndNormalisedName(defaultShoppingList, "milk"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> shoppingListItemService.addItem("Milk", 145));

        verify(shoppingListItemRepository, never()).save(any());
    }

    @Test
    void getTotalEstimatedPriceInPenceSumsItemPricesAndIgnoresNullPrices() {
        ShoppingListItem milk = new ShoppingListItem();
        milk.setPriceInPence(145);

        ShoppingListItem bread = new ShoppingListItem();
        bread.setPriceInPence(120);

        ShoppingListItem unknownPriceItem = new ShoppingListItem();
        unknownPriceItem.setPriceInPence(null);

        when(shoppingListService.getCurrentShoppingList()).thenReturn(defaultShoppingList);
        when(shoppingListItemRepository.findByShoppingListOrderByDisplayOrderAsc(defaultShoppingList))
                .thenReturn(List.of(milk, bread, unknownPriceItem));

        int result = shoppingListItemService.getTotalPriceInPence();

        assertEquals(265, result);
    }

    @Test
    void togglePurchasedChangesItemFromNotPurchasedToPurchased() {
        ShoppingListItem item = new ShoppingListItem();
        item.setShoppingList(defaultShoppingList);
        item.setPurchased(false);

        when(shoppingListService.getCurrentShoppingList()).thenReturn(defaultShoppingList);
        when(shoppingListItemRepository.findByIdAndShoppingList(10L, defaultShoppingList))
                .thenReturn(Optional.of(item));

        shoppingListItemService.togglePurchased(10L);

        assertTrue(item.isPurchased());
    }

    @Test
    void togglePurchasedThrowsExceptionWhenItemDoesNotBelongToCurrentList() {
        when(shoppingListService.getCurrentShoppingList()).thenReturn(defaultShoppingList);
        when(shoppingListItemRepository.findByIdAndShoppingList(999L, defaultShoppingList))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> shoppingListItemService.togglePurchased(999L));
    }
}

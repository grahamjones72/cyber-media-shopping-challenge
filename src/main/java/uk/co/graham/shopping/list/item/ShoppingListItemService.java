package uk.co.graham.shopping.list.item;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import uk.co.graham.shopping.list.ShoppingList;
import uk.co.graham.shopping.list.ShoppingListService;

@Service
public class ShoppingListItemService {

    private final ShoppingListItemRepository shoppingListItemRepository;
    private final ShoppingListService shoppingListService;

    public ShoppingListItemService(ShoppingListItemRepository shoppingListItemRepository,
            ShoppingListService shoppingListService) {
        this.shoppingListItemRepository = shoppingListItemRepository;
        this.shoppingListService = shoppingListService;
    }

    @Transactional(readOnly = true)
    public List<ShoppingListItem> getItems() {
        ShoppingList shoppingList = shoppingListService.getCurrentShoppingList();

        return shoppingListItemRepository
                .findByShoppingListOrderByDisplayOrderAsc(shoppingList);
    }

    @Transactional
    public void addItem(String name, Integer priceInPence) {
        ShoppingList shoppingList = shoppingListService.getCurrentShoppingList();

        int nextDisplayOrder = shoppingListItemRepository
                .findTopByShoppingListOrderByDisplayOrderDesc(shoppingList)
                .map(item -> item.getDisplayOrder() + 1)
                .orElse(1);

        ShoppingListItem item = new ShoppingListItem();
        item.setShoppingList(shoppingList);
        item.setName(name); // this calculates normalisedName as well

        if (item.getNormalisedName() == null || item.getNormalisedName().isEmpty()) {
            throw new IllegalArgumentException("Item name is required");
        }

        if (priceInPence != null && priceInPence < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        if (shoppingListItemRepository.existsByShoppingListAndNormalisedName(shoppingList, item.getNormalisedName())) {
            throw new IllegalArgumentException("Item with the same name already exists in the shopping list");
        }

        item.setPriceInPence(priceInPence);
        item.setDisplayOrder(nextDisplayOrder);

        shoppingListItemRepository.save(item);
    }

    @Transactional
    public void removeItem(Long itemId) {
        ShoppingListItem item = getCurrentListItemOrThrow(itemId);
        ShoppingList shoppingList = item.getShoppingList();

        shoppingListItemRepository.delete(item);
        shoppingListItemRepository.flush();

        renumberItems(shoppingList);
    }

    @Transactional
    public void moveItemUp(Long itemId) {
        moveItem(itemId, -1);
    }

    @Transactional
    public void moveItemDown(Long itemId) {
        moveItem(itemId, 1);
    }

    private void moveItem(Long itemId, int direction) {
        ShoppingListItem item = getCurrentListItemOrThrow(itemId);
        ShoppingList shoppingList = item.getShoppingList();

        int currentOrder = item.getDisplayOrder();
        int targetOrder = currentOrder + direction;

        if (targetOrder < 1) {
            return;
        }

        ShoppingListItem otherItem = shoppingListItemRepository
                .findByShoppingListAndDisplayOrder(shoppingList, targetOrder)
                .orElse(null);

        if (otherItem == null) {
            return;
        }

        item.setDisplayOrder(targetOrder);
        otherItem.setDisplayOrder(currentOrder);
    }

    @Transactional
    public void togglePurchased(Long itemId) {
        ShoppingListItem item = getCurrentListItemOrThrow(itemId);
        item.setPurchased(!item.isPurchased());
        this.shoppingListItemRepository.save(item);
    }

    private void renumberItems(ShoppingList shoppingList) {
        List<ShoppingListItem> items = shoppingListItemRepository
                .findByShoppingListOrderByDisplayOrderAsc(shoppingList);

        for (int i = 0; i < items.size(); i++) {
            items.get(i).setDisplayOrder(i + 1);
        }
    }

    @Transactional(readOnly = true)
    public int getTotalPriceInPence() {
        ShoppingList shoppingList = shoppingListService.getCurrentShoppingList();

        return shoppingListItemRepository
                .findByShoppingListOrderByDisplayOrderAsc(shoppingList)
                .stream()
                .map(ShoppingListItem::getPriceInPence)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();
    }

    /**
     * Retrieves the shopping list item with the given ID from the current shopping
     * list.
     * If the item is not found, an IllegalArgumentException is thrown.
     *
     * This ensures that operations are only performed on items that belong to the
     * current shopping list.
     * 
     * @param itemId the ID of the shopping list item to retrieve
     * @return the shopping list item with the given ID
     * @throws IllegalArgumentException if the item is not found in the current
     *                                  shopping list
     */
    private ShoppingListItem getCurrentListItemOrThrow(Long itemId) {
        ShoppingList shoppingList = shoppingListService.getCurrentShoppingList();

        return shoppingListItemRepository
                .findByIdAndShoppingList(itemId, shoppingList)
                .orElseThrow(() -> new IllegalArgumentException("Shopping list item not found for current user"));
    }
}

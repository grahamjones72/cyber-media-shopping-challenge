package uk.co.graham.shopping.list;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ShoppingListItemService {

    private final ShoppingListItemRepository shoppingListItemRepository;
    
    public ShoppingListItemService(ShoppingListItemRepository shoppingListItemRepository) {
        this.shoppingListItemRepository = shoppingListItemRepository;
    }

    public List<ShoppingListItem> getItems() {
        // TODO: Implement filtering by shopping list ID
        return this.shoppingListItemRepository.findAll();
    }

    public void addItem(String name) {
        ShoppingListItem item = new ShoppingListItem();
        item.setName(name);
        this.shoppingListItemRepository.save(item);
    }

    public void removeItem(Long itemId) {
        if (!this.shoppingListItemRepository.existsById(itemId)) {
            throw new RuntimeException("Item not found");
        }
        this.shoppingListItemRepository.deleteById(itemId);
    }

    public void togglePurchased(Long itemId) {
        ShoppingListItem item = this.shoppingListItemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setPurchased(!item.isPurchased());
        this.shoppingListItemRepository.save(item);
    }  
}

package uk.co.graham.shopping.list.item;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.co.graham.shopping.list.ShoppingList;

public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
    List<ShoppingListItem> findByShoppingListOrderByDisplayOrderAsc(ShoppingList shoppingList);

    Optional<ShoppingListItem> findTopByShoppingListOrderByDisplayOrderDesc(ShoppingList shoppingList);

    Optional<ShoppingListItem> findByIdAndShoppingList(Long id, ShoppingList shoppingList);

    Optional<ShoppingListItem> findByShoppingListAndDisplayOrder(
            ShoppingList shoppingList,
            Integer displayOrder);

    Optional<ShoppingListItem> findByIdAndShoppingList_AppUser_Username(Long id, String username);

    boolean existsByShoppingListAndNormalisedName(ShoppingList shoppingList, String normalisedName);
}

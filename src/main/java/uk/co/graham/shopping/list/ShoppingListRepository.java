package uk.co.graham.shopping.list;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    Optional<ShoppingList> findByNameAndAppUserUsername(String name, String username);

    Optional<ShoppingList> findFirstByAppUser_Username(String username);
}
package uk.co.graham.shopping.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ShoppingListItemTests {

    @Test
    void testNormaliseNameOnConstructor() {
        // Test cases for normaliseName method used in the constructor of ShoppingListItem
        ShoppingListItem item1 = new ShoppingListItem(
            null, 
            "Apple", 
            100, 
            1);

        ShoppingListItem item2 = new ShoppingListItem(
            null, 
            "   Apple  ", 
            100, 
            2);

            ShoppingListItem item3 = new ShoppingListItem(
            null, 
            "APPLE", 
            100, 
            3);

            assertEquals("apple", item1.getNormalisedName());
            assertEquals("apple", item2.getNormalisedName());

    }

    @Test
    void testNormaliseNameOnSetName() {
        ShoppingListItem item1 = new ShoppingListItem(
            null, 
            "Banana", 
            150, 
            1);

        // Change the name and check if normalisedName is updated correctly
        item1.setName("   Banana   Split  ");
        assertEquals("banana split", item1.getNormalisedName());
    } 

    @Test
    void normalisedNameShouldCollapseMultipleSpaces() {
    ShoppingListItem item = new ShoppingListItem(
            null,
            "  Herbal   Tea     ",
            365,
            1
    );

    assertEquals("herbal tea", item.getNormalisedName());
}
}

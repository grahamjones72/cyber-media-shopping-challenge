package uk.co.graham.shopping.list;

import java.util.Locale;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ForeignKey;

@Entity
@Table(name = "shopping_list_item",
        uniqueConstraints = {
                @UniqueConstraint(
                    name = "UK_SHOPPING_LIST_ITEM_NORMALISED_NAME",
                    columnNames = {"shopping_list_id", "normalised_name"})
        },
        indexes = {
                @Index(
                    name = "IDX_SHOPPING_LIST_ITEM_SHOPPING_LIST_DISPLAY_ORDER", 
                    columnList = "shopping_list_id, display_order")
        }
)
public class ShoppingListItem {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "shopping_list_id",
        nullable = false,
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "FK_SHOPPING_LIST_ITEM_SHOPPING_LIST")
    )
    private ShoppingList shoppingList;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String normalisedName;

    @Column(nullable = false)
    private Integer estimatedPriceInPence;

    @Column(nullable = false)
    private boolean purchased = false;
    
    @Column(nullable = false)
    private Integer displayOrder;

    protected ShoppingListItem() {
    }

    public ShoppingListItem(ShoppingList shoppingList, String name, Integer estimatedPriceInPence, Integer displayOrder) {
        this.shoppingList = shoppingList;
        this.name = name;
        this.normalisedName = normaliseName(name);
        this.estimatedPriceInPence = estimatedPriceInPence;
        this.displayOrder = displayOrder;
    }  

private static String normaliseName(String name) {
    return name == null
            ? null
            : name.trim()
                  .replaceAll("\\s+", " ")
                  .toLowerCase(Locale.ROOT);
}

    public Long getId() {
        return id;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public String getName() {
        return name;
    }

    public String getNormalisedName() {
        return normalisedName;
    }

    public Integer getEstimatedPriceInPence() {
        return estimatedPriceInPence;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public void setName(String name) {
        this.name = name;
        this.normalisedName = normaliseName(name);
    }

    public void setEstimatedPriceInPence(Integer estimatedPriceInPence) {
        this.estimatedPriceInPence = estimatedPriceInPence;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}

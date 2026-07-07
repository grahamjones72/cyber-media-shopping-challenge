package uk.co.graham.shopping.list;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import uk.co.graham.shopping.user.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;

@Entity
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "app_user_id", nullable = false, unique = true, referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_SHOPPING_LIST_APP_USER"))
    private AppUser appUser;

    @Column(nullable = false, length = 100)
    private String name;

    @Column
    private Integer budgetInPence;

    protected ShoppingList() {
    }

    public ShoppingList(AppUser appUser, String name) {
        this.appUser = appUser;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public String getName() {
        return name;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBudgetInPence() {
        return budgetInPence;
    }

    public void setBudgetInPence(Integer budgetInPence) {
        this.budgetInPence = budgetInPence;
    }
}

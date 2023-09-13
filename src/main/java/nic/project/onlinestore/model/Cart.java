package nic.project.onlinestore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Map;

@Entity
@Table(name = "carts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection
    @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyJoinColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<Product, Integer> items;

    public void addProductToCart(Product product) {
        items.put(product, 1);
    }

    public void removeProductFromCart(Product product) {
        items.remove(product);
    }

    public void incProduct(Product product) {
        items.put(product, items.get(product) + 1);
    }

    public void decProduct(Product product) {
        items.put(product, items.get(product) - 1);
    }

    public boolean containsItem(Product product) {
        return items.containsKey(product);
    }

    public void clear() {
        items.clear();
    }

    public Integer getQuantity(Product product) {
        return items.get(product);
    }

}

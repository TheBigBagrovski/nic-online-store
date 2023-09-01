package nic.project.onlinestore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "products")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @Builder.Default
    @ManyToMany( cascade = CascadeType.ALL)
    @JoinTable(name = "products_categories", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private Set<Category> categories = new HashSet<>();

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "products_filter_values",
            inverseJoinColumns = @JoinColumn(name = "filter_value_id")
    )
    @MapKeyJoinColumn(name = "filter_id")
    private final Map<Filter, FilterValue> filterProperties = new HashMap<>(); // final чтобы билдер игнорировал

    public void addImage(Image image) {
        images.add(image);
    }

    public void clearImages() {
        images.clear();
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
    }

    public boolean containsCategory(Category category) {
        return categories.contains(category);
    }

    public void addFilterProperty(Filter filter, FilterValue value) {
        filterProperties.put(filter, value);
    }

    public void removeFilterProperty(Filter filter) {
        filterProperties.remove(filter);
    }

    public boolean containsProperty(Filter filter) {
        return filterProperties.containsKey(filter);
    }

    public void clearCategories() {
        categories.clear();
    }

    public void clearFilterProperties() {
        filterProperties.clear();
    }

}

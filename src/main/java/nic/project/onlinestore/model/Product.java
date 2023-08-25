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
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @NotBlank
    @Size(max = 255, message = "В названии товара должно быть до 255 символов")
    @Column(nullable = false)
    private String name;

    @Size(max = 2000, message = "В описании товара должно быть до 2000 символов")
    private String description;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @Builder.Default
    @ManyToMany( cascade = CascadeType.ALL)
    @JoinTable(name = "products_categories", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private Set<Category> categories = new HashSet<>();

    @NotNull
    @Min(value = 0, message = "Минимальная цена - 0 рублей")
    @Digits(integer = 50, fraction = 10, message = "Некорректное число")
    @Column(nullable = false)
    private Double price;

    @NotNull
    @Min(value = 0, message = "Минимальное количество - 0")
    @Digits(integer = 50, fraction = 0, message = "Некорректное число")
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

    public void removeImage(Image image) {
        images.remove(image);
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

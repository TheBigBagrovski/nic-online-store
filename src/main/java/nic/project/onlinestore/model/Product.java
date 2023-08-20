package nic.project.onlinestore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Data
@Entity
@Table(name = "product")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255, message = "В названии товара должно быть до 255 символов")
    @Column(nullable = false)
    private String name;

//    @NotBlank
    @Size(max = 2000, message = "В описании товара должно быть до 2000 символов")
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Image> images;

    @ManyToMany( cascade = CascadeType.ALL)
    @JoinTable(name = "product_categories", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private List<Category> categories;

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

}

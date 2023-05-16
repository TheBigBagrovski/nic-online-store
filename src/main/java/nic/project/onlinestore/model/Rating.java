package nic.project.onlinestore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "rating")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 1, message = "Минимальная оценка - 1")
    @Max(value = 5, message = "Максимальная оценка - 5")
    @Digits(integer = 1, fraction = 0, message = "Некорректное значение оценки")
    @Column(nullable = false)
    private Integer value; // todo - ограничение на значение?

    @ManyToOne
    @JoinTable(name = "user_ratings", joinColumns = @JoinColumn(name = "rating_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private User user;

    @ManyToOne
    @JoinTable(name = "product_ratings", joinColumns = @JoinColumn(name = "rating_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    private Product product;

}

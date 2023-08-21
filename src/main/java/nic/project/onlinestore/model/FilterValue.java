package nic.project.onlinestore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "filter_values")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    @ManyToOne
    @JoinColumn(name = "filter_id")
    private Filter filter; // Фильтр, к которому относится это значение

}

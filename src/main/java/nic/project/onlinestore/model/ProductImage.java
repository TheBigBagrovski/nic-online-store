package nic.project.onlinestore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product_image")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path; // todo - нужен ли путь до изображения, нужно ли сохранять изображения в папке

    private String name; // todo - ограничения на имя?

    private String type;

    private byte[] image;

}

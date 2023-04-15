package nic.project.onlinestore.models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateCreated = new Date();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

}

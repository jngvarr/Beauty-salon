package dao.entities;

import dao.converters.UnitsConverter;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "consumables")
@Data
public class Consumable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "unit")
    @Convert(converter = UnitsConverter.class)
    private Unit unit;
    @Column(name = "price")
    private Double price;
}


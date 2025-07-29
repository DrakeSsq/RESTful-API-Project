package online.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import online.store.entity.enums.Category;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "article", nullable = false, unique = true)
    private String article;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "last_quantity_update_at")
    private LocalDateTime lastQuantityUpdatedAt;

    @Version
    private Long version;

    public void setQuantity(Integer quantity) {
        if ((this.quantity == null && quantity != null) ||
                (this.quantity != null && !this.quantity.equals(quantity))) {
            this.lastQuantityUpdatedAt = LocalDateTime.now();
        }
        this.quantity = quantity;
    }

}

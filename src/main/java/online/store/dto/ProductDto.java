package online.store.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import online.store.entity.enums.Category;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 2, max = 25, message = "Name must be between 2 and 25 characters")
    private String name;

    @NotBlank(message = "Article cannot be blank")
    @Size(min = 5, max = 30, message = "Article must be between 5 and 30 characters")
    private String article;

    @NotNull(message = "Description cannot be null")
    @Size(min = 10, max = 100, message = "Description must be between 10 and 100 characters")
    private String description;

    @NotNull(message = "Category cannot be null")
    private Category category;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @PositiveOrZero(message = "Quantity cannot be negative")
    private Integer quantity;
}

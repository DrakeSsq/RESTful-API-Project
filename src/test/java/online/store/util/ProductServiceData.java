package online.store.util;

import online.store.dto.ProductDto;
import online.store.entity.enums.Category;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductServiceData {
    public static List<ProductDto> createProducts() {
        List<ProductDto> result = new ArrayList<>();

        result.add(new ProductDto(
                "Smartphone X",
                "ART-12345",
                "Flagship smartphone with advanced camera",
                Category.ELECTRONICS,
                new BigDecimal("999.99"),
                50));

        result.add(new ProductDto(
                "Wireless Headphones",
                "ART-67890",
                "Noise-cancelling wireless headphones",
                Category.ELECTRONICS,
                new BigDecimal("199.50"),
                100));

        result.add(new ProductDto(
                "Organic Coffee",
                "ART-COFF-001",
                "Premium organic arabica coffee beans",
                Category.CLOTHING,
                new BigDecimal("12.99"),
                200));

        result.add(new ProductDto(
                "Yoga Mat",
                "ART-SPT-202",
                "Eco-friendly non-slip yoga mat",
                Category.BOOKS,
                new BigDecimal("29.95"),
                70));

        result.add(new ProductDto(
                "Leather Wallet",
                "ART-ACC-555",
                "Genuine leather bifold wallet",
                Category.CLOTHING,
                new BigDecimal("45.00"),
                30));

        result.add(new ProductDto(
                "Programming Book",
                "ART-BOK-777",
                "Complete guide to Java programming",
                Category.BOOKS,
                new BigDecimal("39.99"),
                25));

        result.add(new ProductDto(
                "Desk Lamp",
                "ART-HOM-333",
                "LED desk lamp with adjustable brightness",
                Category.CLOTHING,
                new BigDecimal("24.50"),
                60));

        result.add(new ProductDto(
                "Vitamin C",
                "ART-HLT-888",
                "1000mg Vitamin C supplements",
                Category.BOOKS,
                new BigDecimal("9.99"),
                150));

        result.add(new ProductDto(
                "Kids T-Shirt",
                "ART-CLD-111",
                "100% cotton t-shirt for kids",
                Category.CLOTHING,
                new BigDecimal("15.75"),
                120));

        result.add(new ProductDto(
                "Gardening Gloves",
                "ART-GDN-444",
                "Durable gardening gloves with grip",
                Category.ELECTRONICS,
                new BigDecimal("8.25"),
                90));

        return result;
    }

    public static String getSingleSearchString() {
        return """
            [{
                "field": "price",
                "value": 120,
                "operation": "GREATER_THAN"
            }]
        """;
    }

    public static String getSearchByMultipleCriteria() {
        return """
                [
                    {
                        "field": "price",
                        "value": 120,
                        "operation": "GREATER_THAN"
                    },
                    {
                        "field": "category",
                        "value": "BOOKS",
                        "operation": "NOT_EQUAL"
                    }
                ]
                """;
    }

    public static String getEmptyData() {
        return "[]";
    }

    public static String getNotFoundCriteria() {
        return """
                    [{
                        "field": "price",
                        "value": 5,
                        "operation": "LESS_THAN"
                    }]
                """;
    }
}

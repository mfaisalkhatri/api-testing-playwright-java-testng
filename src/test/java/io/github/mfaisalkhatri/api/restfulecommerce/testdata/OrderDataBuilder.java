package io.github.mfaisalkhatri.api.restfulecommerce.testdata;

import net.datafaker.Faker;

public class OrderDataBuilder {

    private static final Faker FAKER = new Faker();

    public static OrderData getNewOrder() {
        int userId = FAKER.number().numberBetween(2, 4);
        int productId = FAKER.number().numberBetween(331,333);
        int productAmount = FAKER.number().numberBetween(400, 903);
        int quantity = FAKER.number().numberBetween(1, 5);
        int taxAmount = FAKER.number().numberBetween(10,50);
        int totalAmount = (productAmount*quantity)+taxAmount;


        return OrderData.builder()
                .userId(String.valueOf(userId))
                .productId(String.valueOf(productId))
                .productName(FAKER.commerce().productName())
                .productAmount(productAmount)
                .qty(quantity)
                .taxAmt(taxAmount)
                .totalAmt(totalAmount)
                .build();
    }

    public static OrderData getUpdatedOrder() {
        int userId = FAKER.number().numberBetween(4, 5);
        int productId = FAKER.number().numberBetween(335,337);
        int productAmount = FAKER.number().numberBetween(510, 515);
        int quantity = FAKER.number().numberBetween(1, 2);
        int taxAmount = FAKER.number().numberBetween(35,45);
        int totalAmount = (productAmount*quantity)+taxAmount;


        return OrderData.builder()
                .userId(String.valueOf(userId))
                .productId(String.valueOf(productId))
                .productName(FAKER.commerce().productName())
                .productAmount(productAmount)
                .qty(quantity)
                .taxAmt(taxAmount)
                .totalAmt(totalAmount)
                .build();
    }

    public static OrderData getPartialUpdatedOrder() {
        return OrderData.builder()
                .productName(FAKER.commerce().productName())
                .productAmount(FAKER.number().numberBetween(550,560))
                .qty(FAKER.number().numberBetween(3, 4))
                .build();

    }
}

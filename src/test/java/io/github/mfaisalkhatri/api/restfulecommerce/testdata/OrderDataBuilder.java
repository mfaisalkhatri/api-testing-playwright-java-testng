package io.github.mfaisalkhatri.api.restfulecommerce.testdata;

import net.datafaker.Faker;

public class OrderDataBuilder {

    private static final Faker FAKER = new Faker();

    public static OrderData getNewOrder() {
        final int userId = FAKER.number().numberBetween(2, 4);
        final int productId = FAKER.number().numberBetween(331,333);
        final int productAmount = FAKER.number().numberBetween(400, 903);
        final int quantity = FAKER.number().numberBetween(1, 5);
        final int taxAmount = FAKER.number().numberBetween(10,50);
        final int totalAmount = (productAmount*quantity)+taxAmount;


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

    public static OrderData getOrderDataWithMissingProductId() {
        final int userId = FAKER.number().numberBetween(2, 4);
        final int productAmount = FAKER.number().numberBetween(400, 903);
        final int quantity = FAKER.number().numberBetween(1, 5);
        final int taxAmount = FAKER.number().numberBetween(10,50);
        final int totalAmount = (productAmount*quantity)+taxAmount;


        return OrderData.builder()
                .userId(String.valueOf(userId))
                .productName(FAKER.commerce().productName())
                .productAmount(productAmount)
                .qty(quantity)
                .taxAmt(taxAmount)
                .totalAmt(totalAmount)
                .build();


    }

    public static OrderData getUpdatedOrder() {
        final int userId = FAKER.number().numberBetween(4, 5);
        final int productId = FAKER.number().numberBetween(335,337);
        final int productAmount = FAKER.number().numberBetween(510, 515);
        final int quantity = FAKER.number().numberBetween(1, 2);
        final int taxAmount = FAKER.number().numberBetween(35,45);
        final int totalAmount = (productAmount*quantity)+taxAmount;


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

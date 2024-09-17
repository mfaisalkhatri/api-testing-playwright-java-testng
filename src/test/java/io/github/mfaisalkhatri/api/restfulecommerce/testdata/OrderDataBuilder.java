package io.github.mfaisalkhatri.api.restfulecommerce.testdata;

import net.datafaker.Faker;

public class OrderDataBuilder {

    private static final Faker FAKER = new Faker();

    public static OrderData getNewOrder() {
        int randomInt = FAKER.number().numberBetween(1, 10);
        int randomAmount = FAKER.number().numberBetween(400, 903);

        return OrderData.builder()
                .userId(String.valueOf(randomInt))
                .productId(String.valueOf(randomInt))
                .productName(FAKER.commerce().productName())
                .productAmount(randomAmount).qty(randomInt)
                .qty(randomInt)
                .taxAmt(randomAmount)
                .totalAmt(randomAmount)
                .build();
    }
}

package io.github.mfaisalkhatri.api.restfulecommerce.testdata;


import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderData {
    @SerializedName("user_id")
    private String userId;
    @SerializedName("product_id")
    private String productId;
    @SerializedName("product_name")
    private String productName;
    @SerializedName("product_amount")
    private int productAmount;
    private int qty;
    @SerializedName("tax_amt")
    private int taxAmt;
    @SerializedName("total_amt")
    private int totalAmt;

}

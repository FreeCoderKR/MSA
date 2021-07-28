package com.msa.order.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestOrder {
    //뭐를 얼마나 얼마에 살지만 전달받음.
    @NotNull(message = "Product cannot be Null")
    private String productId;
    @NotNull(message = "Quantity cannot be Null")
    private int qty;
    @NotNull(message = "UnitPrice cannot be Null")
    private int unitPrice;
}

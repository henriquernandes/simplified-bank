package com.henriquernandes.PicPay.responses;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferAuthorizationResponse {
    private String status;
    private Data data;

    @Getter
    @Setter
    public static class Data {
        private boolean authorization;
    }
}

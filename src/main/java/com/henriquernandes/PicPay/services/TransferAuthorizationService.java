package com.henriquernandes.PicPay.services;

import com.henriquernandes.PicPay.responses.TransferAuthorizationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TransferAuthorizationService {
    @Autowired
    private RestTemplate restTemplate;

    public boolean authorizeTransfer() {
        String authorizationUrl = "https://util.devi.tools/api/v2/authorize";


        try {
            ResponseEntity<TransferAuthorizationResponse> response = restTemplate.getForEntity(authorizationUrl, TransferAuthorizationResponse.class);

            if(response.getStatusCode().is2xxSuccessful()) {
                TransferAuthorizationResponse transferAuthorizationResponse = response.getBody();

                if (transferAuthorizationResponse == null) {
                    return false;
                }

                return transferAuthorizationResponse.getData().isAuthorization();
            }
        } catch (RestClientException e) {
            return false;
        }

        return false;
    }
}

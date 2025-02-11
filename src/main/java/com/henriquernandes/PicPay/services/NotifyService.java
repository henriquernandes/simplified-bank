package com.henriquernandes.PicPay.services;

import com.henriquernandes.PicPay.dtos.NotificationDTO;
import com.henriquernandes.PicPay.entities.FailedNotification;
import com.henriquernandes.PicPay.entities.Transaction;
import com.henriquernandes.PicPay.repositories.FailedNotificationRepository;
import com.henriquernandes.PicPay.usecases.NotifyServiceUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotifyService implements NotifyServiceUseCase {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    FailedNotificationRepository failedNotificationRepository;


    public void notifyPayee(Transaction transaction) {
        String url = "https://util.devi.tools/api/v2/notify";

        NotificationDTO notificationDTO = new NotificationDTO(transaction.getPayee().getEmail(),"You have received a payment from " + transaction.getPayer().getFirstName() + " in the value of " + transaction.getValue());

        try {
            restTemplate.postForEntity(url, notificationDTO, String.class);
        } catch (Exception e) {
            FailedNotification failedNotification = new FailedNotification(notificationDTO, transaction);
            failedNotificationRepository.save(failedNotification);
        }

    }
}

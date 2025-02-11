package com.henriquernandes.PicPay.repositories;

import com.henriquernandes.PicPay.entities.FailedNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedNotificationRepository extends JpaRepository<FailedNotification, Long> {
}

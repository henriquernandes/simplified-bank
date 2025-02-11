package com.henriquernandes.PicPay.usecases;

import com.henriquernandes.PicPay.entities.Transaction;

public interface NotifyServiceUseCase {
    void notifyPayee(Transaction transaction);
}
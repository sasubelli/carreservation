package com.ss.carreservation.service;

import com.ss.carreservation.entity.Payment;

import java.util.List;

public interface PaymentService {
    Payment processPayment(Payment payment);
    Payment getPaymentById(Long id);
    List<Payment> getAllPayments();
}

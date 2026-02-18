package com.ss.carreservation.service;

import com.ss.carreservation.entity.Payment;
import com.ss.carreservation.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired  // bad way of need to implement construction injection
    private PaymentRepository paymentRepository;

    @Override
    public Payment processPayment(Payment payment) {
        // Logic to interface with a payment gateway would go here
        // If successful, you might update payment.setStatus("SUCCESS");
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
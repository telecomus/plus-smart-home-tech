package ru.yandex.practicum.commerce.payment.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.payment.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}

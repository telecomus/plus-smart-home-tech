package ru.yandex.practicum.commerce.delivery.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.delivery.model.Delivery;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, String> {
    Optional<Delivery> findByOrderId(String id);
}

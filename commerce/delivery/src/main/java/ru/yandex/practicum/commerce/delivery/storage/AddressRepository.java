package ru.yandex.practicum.commerce.delivery.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.delivery.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}

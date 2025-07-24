package ru.yandex.practicum.warehouse.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.warehouse.model.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, String> {
    boolean existsByProductId(String id);
}

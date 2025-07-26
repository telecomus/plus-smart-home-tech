package ru.yandex.practicum.warehouse.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.warehouse.model.ProductInWarehouse;

public interface WarehouseRepository extends JpaRepository<ProductInWarehouse, String> {
    boolean existsByProductId(String id);
}

package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressWarehouseDto {
	private String country;
	private String city;
	private String street;
	private String house;
	private String flat;
}

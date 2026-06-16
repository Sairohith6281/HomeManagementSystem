package com.hsms.categoryservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CategoryRequestDTO {
	 private String categoryName;
	 private String description;
	 private Double basePrice;

}

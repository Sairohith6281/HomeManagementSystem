package com.hsms.categoryservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter

@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="category_tbl")
public class ServiceCategory {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(unique = true,length = 50)
    private String categoryName;

    @Column(length = 100)
    private String description;

    private Double basePrice;
    
    private Boolean active = true;

}

package com.hsms.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "technicians")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Technician {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long technicianId;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(length = 100)
    private String skillSet;
    
    @Column
    private Integer experience;
    
    @Column(precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal rating = BigDecimal.ZERO;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isVerified = false;
    
    @Column(length = 200)
    private String serviceAreas;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
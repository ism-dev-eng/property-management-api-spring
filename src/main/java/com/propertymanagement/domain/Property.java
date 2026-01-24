package com.propertymanagement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
    
    @Column(nullable = false)
    private String address;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyType type;
    
    private Integer bedrooms;
    
    private Integer bathrooms;
    
    @Column(name = "square_meters")
    private Double squareMeters;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal rentPrice;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyStatus status;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Business methods (domain logic)
    public boolean isAvailable() {
        return status == PropertyStatus.AVAILABLE;
    }
    
    public void markAsRented() {
        this.status = PropertyStatus.RENTED;
    }
    
    public void markAsAvailable() {
        this.status = PropertyStatus.AVAILABLE;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = PropertyStatus.AVAILABLE;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
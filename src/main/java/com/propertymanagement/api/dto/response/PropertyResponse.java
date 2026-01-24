package com.propertymanagement.api.dto.response;

import com.propertymanagement.domain.PropertyStatus;
import com.propertymanagement.domain.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponse {
    
    private UUID id;
    private String address;
    private PropertyType type;
    private Integer bedrooms;
    private Integer bathrooms;
    private Double squareMeters;
    private BigDecimal rentPrice;
    private PropertyStatus status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
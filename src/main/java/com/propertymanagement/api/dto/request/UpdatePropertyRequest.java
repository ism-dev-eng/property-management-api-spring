package com.propertymanagement.api.dto.request;

import com.propertymanagement.domain.PropertyStatus;
import com.propertymanagement.domain.PropertyType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePropertyRequest {
    
    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;
    
    @NotNull(message = "Property type is required")
    private PropertyType type;
    
    @Min(value = 0, message = "Bedrooms must be 0 or greater")
    @Max(value = 50, message = "Bedrooms must not exceed 50")
    private Integer bedrooms;
    
    @Min(value = 0, message = "Bathrooms must be 0 or greater")
    @Max(value = 20, message = "Bathrooms must not exceed 20")
    private Integer bathrooms;
    
    @DecimalMin(value = "0.01", message = "Square meters must be greater than 0")
    private Double squareMeters;
    
    @NotNull(message = "Rent price is required")
    @DecimalMin(value = "0.01", message = "Rent price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Rent price must have at most 8 integer digits and 2 decimal places")
    private BigDecimal rentPrice;
    
    @NotNull(message = "Status is required")
    private PropertyStatus status;  // Required for update
    
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;
}
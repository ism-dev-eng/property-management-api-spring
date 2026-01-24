package com.propertymanagement.api.v1;

import com.propertymanagement.api.dto.request.CreatePropertyRequest;
import com.propertymanagement.api.dto.request.UpdatePropertyRequest;
import com.propertymanagement.api.dto.response.PropertyResponse;
import com.propertymanagement.domain.Property;
import com.propertymanagement.domain.PropertyStatus;
import com.propertymanagement.domain.PropertyType;
import com.propertymanagement.domain.service.PropertyService;
import com.propertymanagement.infrastructure.mapper.PropertyMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class PropertyController {
    
    private final PropertyService propertyService;
    private final PropertyMapper propertyMapper;
    
    @GetMapping
    public ResponseEntity<List<PropertyResponse>> getAllProperties(
            @RequestParam(required = false) PropertyStatus status,
            @RequestParam(required = false) PropertyType type,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        
        List<Property> properties;
        
        if (status != null && type != null) {
            properties = propertyService.findAll().stream()
                    .filter(p -> p.getStatus() == status && p.getType() == type)
                    .toList();
        } else if (status != null) {
            properties = propertyService.findByStatus(status);
        } else if (type != null) {
            properties = propertyService.findByType(type);
        } else {
            properties = propertyService.findAll();
        }
        
        // Filter by price range if provided
        if (minPrice != null || maxPrice != null) {
            BigDecimal min = minPrice != null ? minPrice : BigDecimal.ZERO;
            BigDecimal max = maxPrice != null ? maxPrice : new BigDecimal("999999999");
            properties = properties.stream()
                    .filter(p -> p.getRentPrice().compareTo(min) >= 0 && 
                                p.getRentPrice().compareTo(max) <= 0)
                    .toList();
        }
        
        List<PropertyResponse> responses = properties.stream()
                .map(propertyMapper::toResponse)
                .toList();
        
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getPropertyById(@PathVariable UUID id) {
        Property property = propertyService.findById(id);
        PropertyResponse response = propertyMapper.toResponse(property);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    public ResponseEntity<PropertyResponse> createProperty(
            @Valid @RequestBody CreatePropertyRequest request) {
        
        Property property = propertyMapper.toDomain(request);
        Property created = propertyService.create(property);
        PropertyResponse response = propertyMapper.toResponse(created);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/v1/properties/" + response.getId())
                .body(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PropertyResponse> updateProperty(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePropertyRequest request) {
        
        Property propertyToUpdate = propertyMapper.toDomainForUpdate(request);
        Property updated = propertyService.update(id, propertyToUpdate);
        PropertyResponse response = propertyMapper.toResponse(updated);
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable UUID id) {
        propertyService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<PropertyResponse>> searchProperties(
            @RequestParam String address) {
        
        List<Property> properties = propertyService.searchByAddress(address);
        List<PropertyResponse> responses = properties.stream()
                .map(propertyMapper::toResponse)
                .toList();
        
        return ResponseEntity.ok(responses);
    }
}
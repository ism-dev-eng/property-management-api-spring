package com.propertymanagement.domain.service;

import com.propertymanagement.domain.Property;
import com.propertymanagement.domain.exception.PropertyNotFoundException;
import com.propertymanagement.persistence.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PropertyService {
    
    private final PropertyRepository propertyRepository;
    
    public Property create(Property property) {
        // Business validation
        validateProperty(property);
        
        return propertyRepository.save(property);
    }
    
    @Transactional(readOnly = true)
    public Property findById(UUID id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException(id));
    }
    
    @Transactional(readOnly = true)
    public List<Property> findAll() {
        return propertyRepository.findAll();
    }
    
    public Property update(UUID id, Property updatedProperty) {
        Property existing = findById(id);
        
        // Business validation
        validateProperty(updatedProperty);
        
        // Update fields
        existing.setAddress(updatedProperty.getAddress());
        existing.setType(updatedProperty.getType());
        existing.setBedrooms(updatedProperty.getBedrooms());
        existing.setBathrooms(updatedProperty.getBathrooms());
        existing.setSquareMeters(updatedProperty.getSquareMeters());
        existing.setRentPrice(updatedProperty.getRentPrice());
        existing.setStatus(updatedProperty.getStatus());
        existing.setDescription(updatedProperty.getDescription());
        
        return propertyRepository.save(existing);
    }
    
    public void delete(UUID id) {
        Property property = findById(id);
        propertyRepository.delete(property);
    }
    
    @Transactional(readOnly = true)
    public List<Property> findByStatus(com.propertymanagement.domain.PropertyStatus status) {
        return propertyRepository.findByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public List<Property> findByType(com.propertymanagement.domain.PropertyType type) {
        return propertyRepository.findByType(type);
    }
    
    @Transactional(readOnly = true)
    public List<Property> searchByAddress(String address) {
        return propertyRepository.findByAddressContainingIgnoreCase(address);
    }
    
    // Business validation
    private void validateProperty(Property property) {
        if (property.getRentPrice() == null || 
            property.getRentPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Rent price must be greater than zero");
        }
        
        if (property.getAddress() == null || property.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }
        
        if (property.getType() == null) {
            throw new IllegalArgumentException("Property type is required");
        }
    }
}
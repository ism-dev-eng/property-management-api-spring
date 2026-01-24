package com.propertymanagement.persistence.repository;

import com.propertymanagement.domain.Property;
import com.propertymanagement.domain.PropertyStatus;
import com.propertymanagement.domain.PropertyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") 
class PropertyRepositoryTest {

    @Autowired
    private PropertyRepository propertyRepository;

    private Property testProperty;

    @BeforeEach
    void setUp() {
        testProperty = new Property();
        testProperty.setAddress("123 Main Street");
        testProperty.setType(PropertyType.APARTMENT);
        testProperty.setBedrooms(2);
        testProperty.setBathrooms(1);
        testProperty.setSquareMeters(75.5);
        testProperty.setRentPrice(new BigDecimal("1500.00"));
        testProperty.setStatus(PropertyStatus.AVAILABLE);
        testProperty.setDescription("Nice apartment");
    }

    @Test
    void shouldSaveProperty() {
        Property saved = propertyRepository.save(testProperty);
        
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getAddress()).isEqualTo("123 Main Street");
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldFindPropertyById() {
        Property saved = propertyRepository.save(testProperty);
        Optional<Property> found = propertyRepository.findById(saved.getId());
        
        assertThat(found).isPresent();
        assertThat(found.get().getAddress()).isEqualTo("123 Main Street");
    }

    @Test
    void shouldFindPropertiesByStatus() {
        propertyRepository.save(testProperty);
        
        Property rentedProperty = new Property();
        rentedProperty.setAddress("456 Oak Avenue");
        rentedProperty.setType(PropertyType.HOUSE);
        rentedProperty.setRentPrice(new BigDecimal("2000.00"));
        rentedProperty.setStatus(PropertyStatus.RENTED);
        propertyRepository.save(rentedProperty);
        
        List<Property> availableProperties = propertyRepository.findByStatus(PropertyStatus.AVAILABLE);
        
        assertThat(availableProperties).hasSize(1);
        assertThat(availableProperties.get(0).getStatus()).isEqualTo(PropertyStatus.AVAILABLE);
    }

    @Test
    void shouldFindPropertiesByType() {
        propertyRepository.save(testProperty);
        
        Property house = new Property();
        house.setAddress("789 Pine Road");
        house.setType(PropertyType.HOUSE);
        house.setRentPrice(new BigDecimal("2500.00"));
        house.setStatus(PropertyStatus.AVAILABLE);
        propertyRepository.save(house);
        
        List<Property> apartments = propertyRepository.findByType(PropertyType.APARTMENT);
        
        assertThat(apartments).hasSize(1);
        assertThat(apartments.get(0).getType()).isEqualTo(PropertyType.APARTMENT);
    }

    @Test
    void shouldFindPropertiesByAddressContaining() {
        propertyRepository.save(testProperty);
        
        List<Property> found = propertyRepository.findByAddressContainingIgnoreCase("main");
        
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getAddress()).containsIgnoringCase("main");
    }
}
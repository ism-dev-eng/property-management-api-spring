package com.propertymanagement.domain.service;

import com.propertymanagement.domain.Property;
import com.propertymanagement.domain.PropertyStatus;
import com.propertymanagement.domain.PropertyType;
import com.propertymanagement.domain.exception.PropertyNotFoundException;
import com.propertymanagement.persistence.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private PropertyService propertyService;

    private Property testProperty;

    @BeforeEach
    void setUp() {
        testProperty = new Property();
        testProperty.setId(UUID.randomUUID());
        testProperty.setAddress("123 Main Street");
        testProperty.setType(PropertyType.APARTMENT);
        testProperty.setBedrooms(2);
        testProperty.setBathrooms(1);
        testProperty.setRentPrice(new BigDecimal("1500.00"));
        testProperty.setStatus(PropertyStatus.AVAILABLE);
    }

    @Test
    void shouldCreateProperty() {
        when(propertyRepository.save(any(Property.class))).thenReturn(testProperty);
        
        Property created = propertyService.create(testProperty);
        
        assertThat(created).isNotNull();
        assertThat(created.getAddress()).isEqualTo("123 Main Street");
        verify(propertyRepository).save(testProperty);
    }

    @Test
    void shouldThrowExceptionWhenRentPriceIsZero() {
        testProperty.setRentPrice(BigDecimal.ZERO);
        
        assertThatThrownBy(() -> propertyService.create(testProperty))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Rent price must be greater than zero");
    }

    @Test
    void shouldThrowExceptionWhenAddressIsEmpty() {
        testProperty.setAddress("");
        
        assertThatThrownBy(() -> propertyService.create(testProperty))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Address is required");
    }

    @Test
    void shouldFindPropertyById() {
        UUID id = testProperty.getId();
        when(propertyRepository.findById(id)).thenReturn(Optional.of(testProperty));
        
        Property found = propertyService.findById(id);
        
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(id);
        verify(propertyRepository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenPropertyNotFound() {
        UUID id = UUID.randomUUID();
        when(propertyRepository.findById(id)).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> propertyService.findById(id))
                .isInstanceOf(PropertyNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void shouldFindAllProperties() {
        when(propertyRepository.findAll()).thenReturn(List.of(testProperty));
        
        List<Property> properties = propertyService.findAll();
        
        assertThat(properties).hasSize(1);
        verify(propertyRepository).findAll();
    }

    @Test
    void shouldUpdateProperty() {
        UUID id = testProperty.getId();
        when(propertyRepository.findById(id)).thenReturn(Optional.of(testProperty));
        when(propertyRepository.save(any(Property.class))).thenReturn(testProperty);
        
        Property updated = new Property();
        updated.setAddress("456 New Street");
        updated.setType(PropertyType.HOUSE);
        updated.setRentPrice(new BigDecimal("2000.00"));
        updated.setStatus(PropertyStatus.AVAILABLE);
        
        Property result = propertyService.update(id, updated);
        
        assertThat(result.getAddress()).isEqualTo("456 New Street");
        assertThat(result.getType()).isEqualTo(PropertyType.HOUSE);
        verify(propertyRepository).save(any(Property.class));
    }

    @Test
    void shouldDeleteProperty() {
        UUID id = testProperty.getId();
        when(propertyRepository.findById(id)).thenReturn(Optional.of(testProperty));
        doNothing().when(propertyRepository).delete(testProperty);
        
        propertyService.delete(id);
        
        verify(propertyRepository).delete(testProperty);
    }
}
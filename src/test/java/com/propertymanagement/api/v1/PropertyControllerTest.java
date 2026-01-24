package com.propertymanagement.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.propertymanagement.api.dto.request.CreatePropertyRequest;
import com.propertymanagement.api.dto.request.UpdatePropertyRequest;
import com.propertymanagement.api.dto.response.PropertyResponse;
import com.propertymanagement.domain.Property;
import com.propertymanagement.domain.PropertyStatus;
import com.propertymanagement.domain.PropertyType;
import com.propertymanagement.domain.exception.PropertyNotFoundException;
import com.propertymanagement.domain.service.PropertyService;
import com.propertymanagement.infrastructure.mapper.PropertyMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PropertyController.class)
class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyService propertyService;

    @MockBean
    private PropertyMapper propertyMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllProperties() throws Exception {
        Property property = createTestProperty();
        PropertyResponse response = createTestPropertyResponse(property);
        
        when(propertyService.findAll()).thenReturn(List.of(property));
        when(propertyMapper.toResponse(any(Property.class))).thenReturn(response);
        
        mockMvc.perform(get("/api/v1/properties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].address").value("123 Main Street"));
    }

    @Test
    void shouldGetPropertyById() throws Exception {
        UUID id = UUID.randomUUID();
        Property property = createTestProperty();
        property.setId(id);
        PropertyResponse response = createTestPropertyResponse(property);
        
        when(propertyService.findById(id)).thenReturn(property);
        when(propertyMapper.toResponse(property)).thenReturn(response);
        
        mockMvc.perform(get("/api/v1/properties/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.address").value("123 Main Street"));
    }

    @Test
    void shouldReturn404WhenPropertyNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        
        when(propertyService.findById(id)).thenThrow(new PropertyNotFoundException(id));
        
        mockMvc.perform(get("/api/v1/properties/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void shouldCreateProperty() throws Exception {
        CreatePropertyRequest request = new CreatePropertyRequest();
        request.setAddress("123 Main Street");
        request.setType(PropertyType.APARTMENT);
        request.setBedrooms(2);
        request.setRentPrice(new BigDecimal("1500.00"));
        
        Property property = createTestProperty();
        PropertyResponse response = createTestPropertyResponse(property);
        
        when(propertyMapper.toDomain(any(CreatePropertyRequest.class))).thenReturn(property);
        when(propertyService.create(any(Property.class))).thenReturn(property);
        when(propertyMapper.toResponse(property)).thenReturn(response);
        
        mockMvc.perform(post("/api/v1/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.address").value("123 Main Street"));
    }

    @Test
    void shouldReturn400WhenValidationFails() throws Exception {
        CreatePropertyRequest request = new CreatePropertyRequest();
        // Missing required fields
        
        mockMvc.perform(post("/api/v1/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldUpdateProperty() throws Exception {
        UUID id = UUID.randomUUID();
        UpdatePropertyRequest request = new UpdatePropertyRequest();
        request.setAddress("456 New Street");
        request.setType(PropertyType.HOUSE);
        request.setRentPrice(new BigDecimal("2000.00"));
        request.setStatus(PropertyStatus.AVAILABLE);
        
        Property property = createTestProperty();
        property.setId(id);
        property.setAddress("456 New Street");
        PropertyResponse response = createTestPropertyResponse(property);
        
        when(propertyMapper.toDomainForUpdate(any(UpdatePropertyRequest.class))).thenReturn(property);
        when(propertyService.update(eq(id), any(Property.class))).thenReturn(property);
        when(propertyMapper.toResponse(property)).thenReturn(response);
        
        mockMvc.perform(put("/api/v1/properties/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("456 New Street"));
    }

    @Test
    void shouldDeleteProperty() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(propertyService).delete(id);
        
        mockMvc.perform(delete("/api/v1/properties/{id}", id))
                .andExpect(status().isNoContent());
        
        verify(propertyService).delete(id);
    }

    @Test
    void shouldSearchPropertiesByAddress() throws Exception {
        Property property = createTestProperty();
        PropertyResponse response = createTestPropertyResponse(property);
        
        when(propertyService.searchByAddress("main")).thenReturn(List.of(property));
        when(propertyMapper.toResponse(any(Property.class))).thenReturn(response);
        
        mockMvc.perform(get("/api/v1/properties/search")
                        .param("address", "main"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].address").value("123 Main Street"));
    }

    // Helper methods
    private Property createTestProperty() {
        Property property = new Property();
        property.setId(UUID.randomUUID());
        property.setAddress("123 Main Street");
        property.setType(PropertyType.APARTMENT);
        property.setBedrooms(2);
        property.setBathrooms(1);
        property.setRentPrice(new BigDecimal("1500.00"));
        property.setStatus(PropertyStatus.AVAILABLE);
        return property;
    }

    private PropertyResponse createTestPropertyResponse(Property property) {
        PropertyResponse response = new PropertyResponse();
        response.setId(property.getId());
        response.setAddress(property.getAddress());
        response.setType(property.getType());
        response.setBedrooms(property.getBedrooms());
        response.setBathrooms(property.getBathrooms());
        response.setRentPrice(property.getRentPrice());
        response.setStatus(property.getStatus());
        return response;
    }
}
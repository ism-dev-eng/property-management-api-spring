package com.propertymanagement.infrastructure.mapper;

import com.propertymanagement.api.dto.request.CreatePropertyRequest;
import com.propertymanagement.api.dto.request.UpdatePropertyRequest;
import com.propertymanagement.api.dto.response.PropertyResponse;
import com.propertymanagement.domain.Property;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PropertyMapper {
    
    // Convert CreatePropertyRequest → Property (for creating)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Property toDomain(CreatePropertyRequest request);
    
    // Convert Property → PropertyResponse (for responses)
    PropertyResponse toResponse(Property property);
    
    // Helper method to convert UpdatePropertyRequest → Property (for updating)
    default Property toDomainForUpdate(UpdatePropertyRequest request) {
        Property property = new Property();
        property.setAddress(request.getAddress());
        property.setType(request.getType());
        property.setBedrooms(request.getBedrooms());
        property.setBathrooms(request.getBathrooms());
        property.setSquareMeters(request.getSquareMeters());
        property.setRentPrice(request.getRentPrice());
        property.setStatus(request.getStatus());
        property.setDescription(request.getDescription());
        return property;
    }
}
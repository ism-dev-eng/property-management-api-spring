package com.propertymanagement.persistence.repository;

import com.propertymanagement.domain.Property;
import com.propertymanagement.domain.PropertyStatus;
import com.propertymanagement.domain.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID> {
    
    // Find by status
    List<Property> findByStatus(PropertyStatus status);
    
    // Find by type
    List<Property> findByType(PropertyType type);
    
    // Find by address containing (case-insensitive)
    List<Property> findByAddressContainingIgnoreCase(String address);
    
    // Find by status and type
    List<Property> findByStatusAndType(PropertyStatus status, PropertyType type);
    
    // Find by price range (custom query)
    @Query("SELECT p FROM Property p WHERE p.rentPrice BETWEEN :minPrice AND :maxPrice")
    List<Property> findByRentPriceBetween(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice
    );
    
    // Find available properties ordered by price
    List<Property> findByStatusOrderByRentPriceAsc(PropertyStatus status);
}
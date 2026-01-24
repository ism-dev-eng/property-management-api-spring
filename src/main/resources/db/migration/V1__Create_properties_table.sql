CREATE TABLE properties (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    address VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    bedrooms INTEGER,
    bathrooms INTEGER,
    square_meters DOUBLE PRECISION,
    rent_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_rent_price_positive CHECK (rent_price > 0),
    CONSTRAINT chk_bedrooms_positive CHECK (bedrooms IS NULL OR bedrooms >= 0),
    CONSTRAINT chk_bathrooms_positive CHECK (bathrooms IS NULL OR bathrooms >= 0),
    CONSTRAINT chk_square_meters_positive CHECK (square_meters IS NULL OR square_meters > 0)
);

CREATE INDEX idx_properties_status ON properties(status);
CREATE INDEX idx_properties_type ON properties(type);
CREATE INDEX idx_properties_address ON properties(address);
CREATE TABLE IF NOT EXISTS api_keys
(
    id UUID PRIMARY KEY,
    key_hash VARCHAR(255) NOT NULL,            -- Lưu hash, không lưu plaintext
    name VARCHAR(255),
    status VARCHAR(20) NOT NULL,               -- 'ACTIVE' / 'REVOKED'
    expires_at TIMESTAMP,                      -- Check khi validate
    revoked_at TIMESTAMP,                      -- Set khi revoke
    last_used_at TIMESTAMP,                    -- Audit trail
    rotated_from_id UUID REFERENCES api_keys,  -- Track rotation
    created_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP
);

CREATE INDEX idx_status_expires ON api_keys(status, expires_at);
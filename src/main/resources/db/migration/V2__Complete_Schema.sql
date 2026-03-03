-- V2__Complete_Schema.sql
-- Complete database schema for Clinic Management System

-- ============================================================
-- CORE TABLES
-- ============================================================

-- Create roles table
CREATE TABLE IF NOT EXISTS roles
(
    id   UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Create users table
CREATE TABLE IF NOT EXISTS users
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email           VARCHAR(255) NOT NULL UNIQUE,
    date_of_birth   VARCHAR(255) NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    phone           VARCHAR(20)  NOT NULL UNIQUE,
    full_name       VARCHAR(255) NOT NULL,
    role            VARCHAR(20)  NOT NULL,
    path_avatar     VARCHAR(500),
    status          VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    gender          VARCHAR(20)  NOT NULL DEFAULT 'OTHER',
    email_verified  BOOLEAN      NOT NULL DEFAULT FALSE,
    phone_verified  BOOLEAN      NOT NULL DEFAULT FALSE,
    last_login      TIMESTAMP,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(100),
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(100),
    deleted_at      TIMESTAMP,
    deleted_by      VARCHAR(100)
);

CREATE INDEX idx_email ON users (email);
CREATE INDEX idx_phone ON users (phone);

-- ============================================================
-- SPECIALTY AND SERVICE TABLES
-- ============================================================

-- Create specialties table
CREATE TABLE IF NOT EXISTS specialties
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name           VARCHAR(255) NOT NULL,
    slug           VARCHAR(255) NOT NULL UNIQUE,
    description    TEXT,
    image          VARCHAR(500),
    display_order  INTEGER      NOT NULL DEFAULT 0,
    is_active      BOOLEAN      NOT NULL DEFAULT TRUE,
    specialty_type VARCHAR(50)  NOT NULL DEFAULT 'GENERAL',
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     VARCHAR(100),
    updated_at     TIMESTAMP,
    updated_by     VARCHAR(100),
    deleted_at     TIMESTAMP,
    deleted_by     VARCHAR(100)
);

CREATE INDEX idx_slug ON specialties (slug);
CREATE INDEX idx_is_active ON specialties (is_active);

-- Create services table
CREATE TABLE IF NOT EXISTS services
(
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    specialty_id       UUID,
    name               VARCHAR(255)   NOT NULL,
    slug               VARCHAR(255)   NOT NULL UNIQUE,
    description        TEXT,
    price              DECIMAL(10, 2) NOT NULL,
    promotional_price  DECIMAL(10, 2),
    duration           INTEGER        NOT NULL DEFAULT 30,
    image              VARCHAR(500),
    is_featured        BOOLEAN        NOT NULL DEFAULT FALSE,
    is_active          BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by         VARCHAR(100),
    updated_at         TIMESTAMP,
    updated_by         VARCHAR(100),
    deleted_at         TIMESTAMP,
    deleted_by         VARCHAR(100),
    CONSTRAINT fk_service_specialty FOREIGN KEY (specialty_id) REFERENCES specialties (id)
);

CREATE INDEX idx_service_slug ON services (slug);
CREATE INDEX idx_specialty_id ON services (specialty_id);
CREATE INDEX idx_service_is_featured ON services (is_featured);
CREATE INDEX idx_service_is_active ON services (is_active);

-- ============================================================
-- MEDICATION TABLES
-- ============================================================

-- Create medications table
CREATE TABLE IF NOT EXISTS medications
(
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name         VARCHAR(255) NOT NULL,
    generic_name VARCHAR(255),
    category     VARCHAR(100),
    form         VARCHAR(100),
    strength     VARCHAR(100),
    unit         VARCHAR(50),
    price        DECIMAL(10, 2),
    is_active    BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(100),
    updated_at   TIMESTAMP,
    updated_by   VARCHAR(100),
    deleted_at   TIMESTAMP,
    deleted_by   VARCHAR(100)
);

CREATE INDEX idx_medication_name ON medications (name);
CREATE INDEX idx_medication_category ON medications (category);
CREATE INDEX idx_medication_is_active ON medications (is_active);

-- ============================================================
-- PERSON TABLES (Patient, Doctor, Staff)
-- ============================================================

-- Create patients table
CREATE TABLE IF NOT EXISTS patients
(
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id           UUID         NOT NULL UNIQUE,
    patient_code      VARCHAR(20)  NOT NULL UNIQUE,
    date_of_birth     DATE,
    gender            VARCHAR(10),
    address           TEXT,
    insurance_number  VARCHAR(100),
    blood_type        VARCHAR(5),
    allergies         TEXT,
    chronic_diseases  TEXT,
    loyalty_points    INTEGER      NOT NULL DEFAULT 0,
    total_visits      INTEGER      NOT NULL DEFAULT 0,
    created_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by        VARCHAR(100),
    updated_at        TIMESTAMP,
    updated_by        VARCHAR(100),
    deleted_at        TIMESTAMP,
    deleted_by        VARCHAR(100),
    CONSTRAINT fk_patient_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_patient_user_id ON patients (user_id);
CREATE INDEX idx_patient_code ON patients (patient_code);

-- Create doctors table
CREATE TABLE IF NOT EXISTS doctors
(
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id           UUID           NOT NULL UNIQUE,
    doctor_code       VARCHAR(20)    NOT NULL UNIQUE,
    specialty_id      UUID           NOT NULL,
    degree            VARCHAR(100),
    experience_years  INTEGER        NOT NULL DEFAULT 0,
    education         TEXT,
    bio               TEXT,
    consultation_fee  DECIMAL(10, 2) NOT NULL DEFAULT 0,
    average_rating    DECIMAL(3, 2)  NOT NULL DEFAULT 0,
    total_reviews     INTEGER        NOT NULL DEFAULT 0,
    total_patients    INTEGER        NOT NULL DEFAULT 0,
    is_featured       BOOLEAN        NOT NULL DEFAULT FALSE,
    status            VARCHAR(20)    NOT NULL DEFAULT 'active',
    created_at        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by        VARCHAR(100),
    updated_at        TIMESTAMP,
    updated_by        VARCHAR(100),
    deleted_at        TIMESTAMP,
    deleted_by        VARCHAR(100),
    CONSTRAINT fk_doctor_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_doctor_specialty FOREIGN KEY (specialty_id) REFERENCES specialties (id)
);

CREATE INDEX idx_doctor_user_id ON doctors (user_id);
CREATE INDEX idx_doctor_code ON doctors (doctor_code);
CREATE INDEX idx_doctor_specialty_id ON doctors (specialty_id);
CREATE INDEX idx_doctor_is_featured ON doctors (is_featured);
CREATE INDEX idx_doctor_status ON doctors (status);

-- Create staff table
CREATE TABLE IF NOT EXISTS staff
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID        NOT NULL UNIQUE,
    staff_code  VARCHAR(20) NOT NULL UNIQUE,
    position    VARCHAR(100),
    department  VARCHAR(100),
    hire_date   DATE,
    status      VARCHAR(20) NOT NULL DEFAULT 'active',
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(100),
    updated_at  TIMESTAMP,
    updated_by  VARCHAR(100),
    deleted_at  TIMESTAMP,
    deleted_by  VARCHAR(100),
    CONSTRAINT fk_staff_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_staff_user_id ON staff (user_id);
CREATE INDEX idx_staff_code ON staff (staff_code);
CREATE INDEX idx_staff_department ON staff (department);
CREATE INDEX idx_staff_status ON staff (status);

-- ============================================================
-- APPOINTMENT AND MEDICAL RECORD TABLES
-- ============================================================

-- Create appointments table
CREATE TABLE IF NOT EXISTS appointments
(
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    appointment_code   VARCHAR(20) NOT NULL UNIQUE,
    patient_id         UUID        NOT NULL,
    doctor_id          UUID        NOT NULL,
    service_id         UUID,
    appointment_date   DATE        NOT NULL,
    start_time         TIME        NOT NULL,
    end_time           TIME        NOT NULL,
    status             VARCHAR(20) NOT NULL DEFAULT 'pending',
    booking_type       VARCHAR(20) NOT NULL DEFAULT 'online',
    reason             TEXT,
    symptoms           TEXT,
    notes              TEXT,
    queue_number       INTEGER,
    created_at         TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by         VARCHAR(100),
    updated_at         TIMESTAMP,
    updated_by         VARCHAR(100),
    deleted_at         TIMESTAMP,
    deleted_by         VARCHAR(100),
    CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id) REFERENCES patients (id),
    CONSTRAINT fk_appointment_doctor FOREIGN KEY (doctor_id) REFERENCES doctors (id),
    CONSTRAINT fk_appointment_service FOREIGN KEY (service_id) REFERENCES services (id)
);

CREATE INDEX idx_appointment_code ON appointments (appointment_code);
CREATE INDEX idx_appointment_patient_id ON appointments (patient_id);
CREATE INDEX idx_appointment_doctor_id ON appointments (doctor_id);
CREATE INDEX idx_appointment_date ON appointments (appointment_date);
CREATE INDEX idx_appointment_status ON appointments (status);
CREATE INDEX idx_doctor_date_status ON appointments (doctor_id, appointment_date, status);
CREATE INDEX idx_patient_status_date ON appointments (patient_id, status, appointment_date);
CREATE INDEX idx_date_status ON appointments (appointment_date, status);

-- Create medical_records table
CREATE TABLE IF NOT EXISTS medical_records
(
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    record_code       VARCHAR(20) NOT NULL UNIQUE,
    appointment_id    UUID        NOT NULL UNIQUE,
    patient_id        UUID        NOT NULL,
    doctor_id         UUID        NOT NULL,
    chief_complaint   TEXT,
    vital_signs       JSON,
    diagnosis         TEXT        NOT NULL,
    treatment_plan    TEXT,
    follow_up_date    DATE,
    doctor_notes      TEXT,
    created_at        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by        VARCHAR(100),
    updated_at        TIMESTAMP,
    updated_by        VARCHAR(100),
    deleted_at        TIMESTAMP,
    deleted_by        VARCHAR(100),
    CONSTRAINT fk_record_appointment FOREIGN KEY (appointment_id) REFERENCES appointments (id),
    CONSTRAINT fk_record_patient FOREIGN KEY (patient_id) REFERENCES patients (id),
    CONSTRAINT fk_record_doctor FOREIGN KEY (doctor_id) REFERENCES doctors (id)
);

CREATE INDEX idx_record_code ON medical_records (record_code);
CREATE INDEX idx_record_appointment_id ON medical_records (appointment_id);
CREATE INDEX idx_record_patient_id ON medical_records (patient_id);
CREATE INDEX idx_record_doctor_id ON medical_records (doctor_id);
CREATE INDEX idx_patient_created ON medical_records (patient_id, created_at);
CREATE INDEX idx_doctor_created ON medical_records (doctor_id, created_at);

-- ============================================================
-- PRESCRIPTION TABLES
-- ============================================================

-- Create prescriptions table
CREATE TABLE IF NOT EXISTS prescriptions
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    prescription_code   VARCHAR(20) NOT NULL UNIQUE,
    medical_record_id   UUID        NOT NULL,
    patient_id          UUID        NOT NULL,
    doctor_id           UUID        NOT NULL,
    prescription_date   DATE        NOT NULL,
    notes               TEXT,
    status              VARCHAR(20) NOT NULL DEFAULT 'active',
    created_at          TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(100),
    updated_at          TIMESTAMP,
    updated_by          VARCHAR(100),
    deleted_at          TIMESTAMP,
    deleted_by          VARCHAR(100),
    CONSTRAINT fk_prescription_medical_record FOREIGN KEY (medical_record_id) REFERENCES medical_records (id),
    CONSTRAINT fk_prescription_patient FOREIGN KEY (patient_id) REFERENCES patients (id),
    CONSTRAINT fk_prescription_doctor FOREIGN KEY (doctor_id) REFERENCES doctors (id)
);

CREATE INDEX idx_prescription_code ON prescriptions (prescription_code);
CREATE INDEX idx_prescription_medical_record_id ON prescriptions (medical_record_id);
CREATE INDEX idx_prescription_patient_id ON prescriptions (patient_id);
CREATE INDEX idx_prescription_doctor_id ON prescriptions (doctor_id);
CREATE INDEX idx_prescription_status ON prescriptions (status);

-- Create prescription_items table
CREATE TABLE IF NOT EXISTS prescription_items
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    prescription_id UUID        NOT NULL,
    medication_id   UUID        NOT NULL,
    dosage          VARCHAR(100),
    frequency       VARCHAR(100),
    duration        VARCHAR(100),
    quantity        INTEGER     NOT NULL,
    instructions    TEXT,
    created_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(100),
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(100),
    deleted_at      TIMESTAMP,
    deleted_by      VARCHAR(100),
    CONSTRAINT fk_item_prescription FOREIGN KEY (prescription_id) REFERENCES prescriptions (id),
    CONSTRAINT fk_item_medication FOREIGN KEY (medication_id) REFERENCES medications (id)
);

CREATE INDEX idx_prescription_item_prescription_id ON prescription_items (prescription_id);
CREATE INDEX idx_prescription_item_medication_id ON prescription_items (medication_id);

-- ============================================================
-- INVOICE AND PAYMENT TABLES
-- ============================================================

-- Create invoices table
CREATE TABLE IF NOT EXISTS invoices
(
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    invoice_code       VARCHAR(20)    NOT NULL UNIQUE,
    appointment_id     UUID,
    patient_id         UUID           NOT NULL,
    invoice_date       DATE           NOT NULL,
    subtotal           DECIMAL(10, 2) NOT NULL,
    discount_amount    DECIMAL(10, 2) NOT NULL DEFAULT 0,
    total_amount       DECIMAL(10, 2) NOT NULL,
    insurance_covered  DECIMAL(10, 2) NOT NULL DEFAULT 0,
    patient_paid       DECIMAL(10, 2) NOT NULL DEFAULT 0,
    balance            DECIMAL(10, 2) NOT NULL DEFAULT 0,
    status             VARCHAR(20)    NOT NULL DEFAULT 'pending',
    created_at         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by         VARCHAR(100),
    updated_at         TIMESTAMP,
    updated_by         VARCHAR(100),
    deleted_at         TIMESTAMP,
    deleted_by         VARCHAR(100),
    CONSTRAINT fk_invoice_appointment FOREIGN KEY (appointment_id) REFERENCES appointments (id),
    CONSTRAINT fk_invoice_patient FOREIGN KEY (patient_id) REFERENCES patients (id)
);

CREATE INDEX idx_invoice_code ON invoices (invoice_code);
CREATE INDEX idx_invoice_appointment_id ON invoices (appointment_id);
CREATE INDEX idx_invoice_patient_id ON invoices (patient_id);
CREATE INDEX idx_invoice_date ON invoices (invoice_date);
CREATE INDEX idx_invoice_status ON invoices (status);
CREATE INDEX idx_invoice_patient_status_date ON invoices (patient_id, status, invoice_date);
CREATE INDEX idx_invoice_date_status ON invoices (invoice_date, status);
CREATE INDEX idx_invoice_status_balance ON invoices (status, balance);

-- Create invoice_items table
CREATE TABLE IF NOT EXISTS invoice_items
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    invoice_id  UUID           NOT NULL,
    item_type   VARCHAR(20)    NOT NULL,
    item_name   VARCHAR(255)   NOT NULL,
    quantity    INTEGER        NOT NULL DEFAULT 1,
    unit_price  DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    created_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(100),
    updated_at  TIMESTAMP,
    updated_by  VARCHAR(100),
    deleted_at  TIMESTAMP,
    deleted_by  VARCHAR(100),
    CONSTRAINT fk_item_invoice FOREIGN KEY (invoice_id) REFERENCES invoices (id)
);

CREATE INDEX idx_invoice_item_invoice_id ON invoice_items (invoice_id);
CREATE INDEX idx_invoice_item_type ON invoice_items (item_type);

-- Create payments table
CREATE TABLE IF NOT EXISTS payments
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    payment_code   VARCHAR(20)    NOT NULL UNIQUE,
    invoice_id     UUID           NOT NULL,
    patient_id     UUID           NOT NULL,
    amount         DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(20)    NOT NULL,
    payment_date   TIMESTAMP      NOT NULL,
    status         VARCHAR(20)    NOT NULL DEFAULT 'pending',
    created_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     VARCHAR(100),
    updated_at     TIMESTAMP,
    updated_by     VARCHAR(100),
    deleted_at     TIMESTAMP,
    deleted_by     VARCHAR(100),
    CONSTRAINT fk_payment_invoice FOREIGN KEY (invoice_id) REFERENCES invoices (id),
    CONSTRAINT fk_payment_patient FOREIGN KEY (patient_id) REFERENCES patients (id)
);

CREATE INDEX idx_payment_code ON payments (payment_code);
CREATE INDEX idx_payment_invoice_id ON payments (invoice_id);
CREATE INDEX idx_payment_patient_id ON payments (patient_id);
CREATE INDEX idx_payment_date ON payments (payment_date);
CREATE INDEX idx_payment_status ON payments (status);
CREATE INDEX idx_payment_status_date ON payments (status, payment_date);
CREATE INDEX idx_payment_patient_status ON payments (patient_id, status);

-- ============================================================
-- MEDICAL EQUIPMENT TABLES
-- ============================================================

-- Create medical_equipment table
CREATE TABLE IF NOT EXISTS medical_equipment
(
    id                         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    equipment_code             VARCHAR(50)    NOT NULL UNIQUE,
    name                       VARCHAR(255)   NOT NULL,
    category                   VARCHAR(100),
    manufacturer               VARCHAR(255),
    model                      VARCHAR(100),
    serial_number              VARCHAR(100),
    purchase_date              DATE,
    purchase_price             DECIMAL(12, 2),
    warranty_expiry            DATE,
    location                   VARCHAR(255),
    status                     VARCHAR(20)    NOT NULL DEFAULT 'operational',
    last_maintenance_date      DATE,
    next_maintenance_date      DATE,
    maintenance_interval_days  INTEGER        NOT NULL DEFAULT 90,
    notes                      TEXT,
    created_at                 TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by                 VARCHAR(100),
    updated_at                 TIMESTAMP,
    updated_by                 VARCHAR(100),
    deleted_at                 TIMESTAMP,
    deleted_by                 VARCHAR(100)
);

CREATE INDEX idx_equipment_code ON medical_equipment (equipment_code);
CREATE INDEX idx_equipment_status ON medical_equipment (status);
CREATE INDEX idx_equipment_next_maintenance ON medical_equipment (next_maintenance_date);

-- Create equipment_maintenance table
CREATE TABLE IF NOT EXISTS equipment_maintenance
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    equipment_id        UUID           NOT NULL,
    maintenance_type    VARCHAR(20)    NOT NULL,
    scheduled_date      DATE,
    completed_date      DATE,
    performed_by        VARCHAR(255),
    cost                DECIMAL(10, 2),
    description         TEXT,
    issues_found        TEXT,
    actions_taken       TEXT,
    status              VARCHAR(20)    NOT NULL DEFAULT 'scheduled',
    created_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(100),
    updated_at          TIMESTAMP,
    updated_by          VARCHAR(100),
    deleted_at          TIMESTAMP,
    deleted_by          VARCHAR(100),
    CONSTRAINT fk_maintenance_equipment FOREIGN KEY (equipment_id) REFERENCES medical_equipment (id)
);

CREATE INDEX idx_maintenance_equipment ON equipment_maintenance (equipment_id);
CREATE INDEX idx_maintenance_scheduled ON equipment_maintenance (scheduled_date);
CREATE INDEX idx_maintenance_status ON equipment_maintenance (status);

-- ============================================================
-- INVENTORY TABLES
-- ============================================================

-- Create inventory table
CREATE TABLE IF NOT EXISTS inventory
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    medication_id   UUID        NOT NULL,
    batch_number    VARCHAR(100),
    quantity        INTEGER     NOT NULL DEFAULT 0,
    expiry_date     DATE,
    supplier        VARCHAR(255),
    status          VARCHAR(20) NOT NULL DEFAULT 'in_stock',
    alert_threshold INTEGER     NOT NULL DEFAULT 10,
    created_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(100),
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(100),
    deleted_at      TIMESTAMP,
    deleted_by      VARCHAR(100),
    CONSTRAINT fk_inventory_medication FOREIGN KEY (medication_id) REFERENCES medications (id)
);

CREATE INDEX idx_inventory_medication_id ON inventory (medication_id);
CREATE INDEX idx_inventory_batch_number ON inventory (batch_number);
CREATE INDEX idx_inventory_status ON inventory (status);
CREATE INDEX idx_inventory_expiry_date ON inventory (expiry_date);

-- ============================================================
-- DOCTOR SCHEDULE AND LEAVE TABLES
-- ============================================================

-- Create doctor_schedules table
CREATE TABLE IF NOT EXISTS doctor_schedules
(
    id                     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    doctor_id              UUID        NOT NULL,
    day_of_week            INTEGER     NOT NULL,
    start_time             TIME        NOT NULL,
    end_time               TIME        NOT NULL,
    slot_duration          INTEGER     NOT NULL DEFAULT 30,
    max_patients_per_slot  INTEGER     NOT NULL DEFAULT 1,
    location               VARCHAR(255),
    is_active              BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at             TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by             VARCHAR(100),
    updated_at             TIMESTAMP,
    updated_by             VARCHAR(100),
    deleted_at             TIMESTAMP,
    deleted_by             VARCHAR(100),
    CONSTRAINT fk_schedule_doctor FOREIGN KEY (doctor_id) REFERENCES doctors (id)
);

CREATE INDEX idx_schedule_doctor_id ON doctor_schedules (doctor_id);
CREATE INDEX idx_schedule_day_of_week ON doctor_schedules (day_of_week);
CREATE INDEX idx_schedule_is_active ON doctor_schedules (is_active);

-- Create doctor_leave table
CREATE TABLE IF NOT EXISTS doctor_leave
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    doctor_id   UUID        NOT NULL,
    leave_date  DATE        NOT NULL,
    start_time  TIME,
    end_time    TIME,
    reason      VARCHAR(255),
    status      VARCHAR(20) NOT NULL DEFAULT 'pending',
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(100),
    updated_at  TIMESTAMP,
    updated_by  VARCHAR(100),
    deleted_at  TIMESTAMP,
    deleted_by  VARCHAR(100),
    CONSTRAINT fk_leave_doctor FOREIGN KEY (doctor_id) REFERENCES doctors (id)
);

CREATE INDEX idx_leave_doctor_id ON doctor_leave (doctor_id);
CREATE INDEX idx_leave_date ON doctor_leave (leave_date);
CREATE INDEX idx_leave_status ON doctor_leave (status);

-- ============================================================
-- PERFORMANCE AND REPORTING TABLES
-- ============================================================

-- Create doctor_performance table
CREATE TABLE IF NOT EXISTS doctor_performance
(
    id                       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    doctor_id                UUID           NOT NULL,
    month                    INTEGER        NOT NULL,
    year                     INTEGER        NOT NULL,
    total_appointments       INTEGER        NOT NULL DEFAULT 0,
    completed_appointments   INTEGER        NOT NULL DEFAULT 0,
    cancelled_appointments   INTEGER        NOT NULL DEFAULT 0,
    total_patients           INTEGER        NOT NULL DEFAULT 0,
    average_rating           DECIMAL(3, 2),
    total_revenue            DECIMAL(12, 2) NOT NULL DEFAULT 0,
    created_at               TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by               VARCHAR(100),
    updated_at               TIMESTAMP,
    updated_by               VARCHAR(100),
    deleted_at               TIMESTAMP,
    deleted_by               VARCHAR(100),
    CONSTRAINT fk_performance_doctor FOREIGN KEY (doctor_id) REFERENCES doctors (id),
    CONSTRAINT unique_doctor_month UNIQUE (doctor_id, month, year)
);

CREATE INDEX idx_performance_doctor ON doctor_performance (doctor_id);
CREATE INDEX idx_performance_period ON doctor_performance (year, month);

-- Create daily_reports table
CREATE TABLE IF NOT EXISTS daily_reports
(
    id                       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    report_date              DATE           NOT NULL UNIQUE,
    total_appointments       INTEGER        NOT NULL DEFAULT 0,
    completed_appointments   INTEGER        NOT NULL DEFAULT 0,
    cancelled_appointments   INTEGER        NOT NULL DEFAULT 0,
    new_patients             INTEGER        NOT NULL DEFAULT 0,
    returning_patients       INTEGER        NOT NULL DEFAULT 0,
    total_revenue            DECIMAL(12, 2) NOT NULL DEFAULT 0,
    cash_revenue             DECIMAL(12, 2) NOT NULL DEFAULT 0,
    online_revenue           DECIMAL(12, 2) NOT NULL DEFAULT 0,
    insurance_revenue        DECIMAL(12, 2) NOT NULL DEFAULT 0,
    pending_payments         DECIMAL(12, 2) NOT NULL DEFAULT 0,
    created_at               TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by               VARCHAR(100),
    updated_at               TIMESTAMP,
    updated_by               VARCHAR(100),
    deleted_at               TIMESTAMP,
    deleted_by               VARCHAR(100),
    CONSTRAINT unique_report_date UNIQUE (report_date)
);

CREATE INDEX idx_daily_report_date ON daily_reports (report_date);

-- Create revenue_reports table
CREATE TABLE IF NOT EXISTS revenue_reports
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    report_type         VARCHAR(20)    NOT NULL,
    report_date         DATE           NOT NULL,
    start_date          DATE           NOT NULL,
    end_date            DATE           NOT NULL,
    total_revenue       DECIMAL(12, 2) NOT NULL DEFAULT 0,
    service_revenue     DECIMAL(12, 2) NOT NULL DEFAULT 0,
    medication_revenue  DECIMAL(12, 2) NOT NULL DEFAULT 0,
    lab_test_revenue    DECIMAL(12, 2) NOT NULL DEFAULT 0,
    total_appointments  INTEGER        NOT NULL DEFAULT 0,
    total_patients      INTEGER        NOT NULL DEFAULT 0,
    created_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(100),
    updated_at          TIMESTAMP,
    updated_by          VARCHAR(100),
    deleted_at          TIMESTAMP,
    deleted_by          VARCHAR(100)
);

CREATE INDEX idx_revenue_report_date ON revenue_reports (report_date);
CREATE INDEX idx_revenue_report_type ON revenue_reports (report_type);

-- ============================================================
-- PROMOTION AND LOYALTY TABLES
-- ============================================================

-- Create promotions table
CREATE TABLE IF NOT EXISTS promotions
(
    id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code                 VARCHAR(50)    NOT NULL UNIQUE,
    name                 VARCHAR(255)   NOT NULL,
    description          TEXT,
    discount_type        VARCHAR(20)    NOT NULL,
    discount_value       DECIMAL(10, 2) NOT NULL,
    min_purchase_amount  DECIMAL(10, 2) NOT NULL DEFAULT 0,
    max_discount_amount  DECIMAL(10, 2),
    usage_limit          INTEGER,
    usage_count          INTEGER        NOT NULL DEFAULT 0,
    usage_per_user       INTEGER        NOT NULL DEFAULT 1,
    applicable_services  JSON,
    start_date           TIMESTAMP      NOT NULL,
    end_date             TIMESTAMP      NOT NULL,
    is_active            BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at           TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by           VARCHAR(100),
    updated_at           TIMESTAMP,
    updated_by           VARCHAR(100),
    deleted_at           TIMESTAMP,
    deleted_by           VARCHAR(100)
);

CREATE INDEX idx_promotion_code ON promotions (code);
CREATE INDEX idx_promotion_dates ON promotions (start_date, end_date);
CREATE INDEX idx_promotion_active ON promotions (is_active);

-- Create promotion_usage table
CREATE TABLE IF NOT EXISTS promotion_usage
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    promotion_id    UUID           NOT NULL,
    user_id         UUID           NOT NULL,
    invoice_id      UUID,
    discount_amount DECIMAL(10, 2) NOT NULL,
    used_at         TIMESTAMP      NOT NULL,
    created_at      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(100),
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(100),
    deleted_at      TIMESTAMP,
    deleted_by      VARCHAR(100),
    CONSTRAINT fk_usage_promotion FOREIGN KEY (promotion_id) REFERENCES promotions (id),
    CONSTRAINT fk_usage_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_usage_invoice FOREIGN KEY (invoice_id) REFERENCES invoices (id)
);

CREATE INDEX idx_usage_promotion ON promotion_usage (promotion_id);
CREATE INDEX idx_usage_user ON promotion_usage (user_id);
CREATE INDEX idx_usage_invoice ON promotion_usage (invoice_id);

-- Create loyalty_transactions table
CREATE TABLE IF NOT EXISTS loyalty_transactions
(
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    patient_id        UUID        NOT NULL,
    transaction_type  VARCHAR(20) NOT NULL,
    points            INTEGER     NOT NULL,
    reference_type    VARCHAR(50),
    reference_id      INTEGER,
    description       TEXT,
    balance_after     INTEGER     NOT NULL,
    expires_at        DATE,
    created_at        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by        VARCHAR(100),
    updated_at        TIMESTAMP,
    updated_by        VARCHAR(100),
    deleted_at        TIMESTAMP,
    deleted_by        VARCHAR(100),
    CONSTRAINT fk_loyalty_patient FOREIGN KEY (patient_id) REFERENCES patients (id)
);

CREATE INDEX idx_loyalty_patient ON loyalty_transactions (patient_id);
CREATE INDEX idx_loyalty_type ON loyalty_transactions (transaction_type);
CREATE INDEX idx_loyalty_created ON loyalty_transactions (created_at);

-- ============================================================
-- REVIEW AND FAQ TABLES
-- ============================================================

-- Create reviews table
CREATE TABLE IF NOT EXISTS reviews
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    patient_id     UUID        NOT NULL,
    doctor_id      UUID,
    appointment_id UUID,
    rating         INTEGER     NOT NULL,
    title          VARCHAR(255),
    content        TEXT,
    status         VARCHAR(20) NOT NULL DEFAULT 'pending',
    created_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     VARCHAR(100),
    updated_at     TIMESTAMP,
    updated_by     VARCHAR(100),
    deleted_at     TIMESTAMP,
    deleted_by     VARCHAR(100),
    CONSTRAINT fk_review_patient FOREIGN KEY (patient_id) REFERENCES patients (id),
    CONSTRAINT fk_review_doctor FOREIGN KEY (doctor_id) REFERENCES doctors (id),
    CONSTRAINT fk_review_appointment FOREIGN KEY (appointment_id) REFERENCES appointments (id)
);

CREATE INDEX idx_review_patient_id ON reviews (patient_id);
CREATE INDEX idx_review_doctor_id ON reviews (doctor_id);
CREATE INDEX idx_review_appointment_id ON reviews (appointment_id);
CREATE INDEX idx_review_rating ON reviews (rating);
CREATE INDEX idx_review_status ON reviews (status);

-- Create faqs table
CREATE TABLE IF NOT EXISTS faqs
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    category       VARCHAR(100),
    question       TEXT        NOT NULL,
    answer         TEXT        NOT NULL,
    display_order  INTEGER     NOT NULL DEFAULT 0,
    is_active      BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     VARCHAR(100),
    updated_at     TIMESTAMP,
    updated_by     VARCHAR(100),
    deleted_at     TIMESTAMP,
    deleted_by     VARCHAR(100)
);

CREATE INDEX idx_faq_category ON faqs (category);
CREATE INDEX idx_faq_is_active ON faqs (is_active);
CREATE INDEX idx_faq_display_order ON faqs (display_order);

-- ============================================================
-- SECURITY TABLES
-- ============================================================

-- Create password_reset_tokens table
CREATE TABLE IF NOT EXISTS password_reset_tokens
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID         NOT NULL,
    token       VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP    NOT NULL,
    used        BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reset_token_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_reset_token ON password_reset_tokens (token);
CREATE INDEX idx_reset_token_user_id ON password_reset_tokens (user_id);

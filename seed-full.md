# Seed Data cho Clinic API Database

## 1. Roles Table

```sql
INSERT INTO roles (id, name)
VALUES
('00000000-0000-0000-0000-000000000001'::uuid, 'ROLE_ADMIN'),
('00000000-0000-0000-0000-000000000002'::uuid, 'ROLE_DOCTOR'),
('00000000-0000-0000-0000-000000000003'::uuid, 'ROLE_PATIENT'),
('00000000-0000-0000-0000-000000000004'::uuid, 'ROLE_STAFF');
```

## 2. Specialties Table

```sql
INSERT INTO specialties (id, name, slug, description, image, display_order, is_active, specialty_type, created_at, updated_at, deleted_at)
VALUES
-- Nội khoa
('a1b2c3d4-1111-1111-1111-000000000001'::uuid, 'Nội khoa', 'noi-khoa', 'Khám và điều trị các bệnh lý nội khoa tổng quát như tiểu đường, huyết áp, gan mật...', 'https://example.com/images/noi-khoa.jpg', 1, true, 'GENERAL', NOW(), NOW(), NULL),

-- Ngoại khoa
('a1b2c3d4-1111-1111-1111-000000000002'::uuid, 'Ngoại khoa', 'ngoai-khoa', 'Phẫu thuật và điều trị các bệnh lý ngoại khoa', 'https://example.com/images/ngoai-khoa.jpg', 2, true, 'SURGERY', NOW(), NOW(), NULL),

-- Nhi khoa
('a1b2c3d4-1111-1111-1111-000000000003'::uuid, 'Nhi khoa', 'nhi-khoa', 'Chuyên khoa chăm sóc và điều trị bệnh cho trẻ em từ 0-16 tuổi', 'https://example.com/images/nhi-khoa.jpg', 3, true, 'PEDIATRICS', NOW(), NOW(), NULL),

-- Sản phụ khoa
('a1b2c3d4-1111-1111-1111-000000000004'::uuid, 'Sản phụ khoa', 'san-phu-khoa', 'Chăm sóc sức khỏe sinh sản, thai sản và các bệnh lý phụ khoa', 'https://example.com/images/san-phu-khoa.jpg', 4, true, 'GYNECOLOGY', NOW(), NOW(), NULL),

-- Tim mạch
('a1b2c3d4-1111-1111-1111-000000000005'::uuid, 'Tim mạch', 'tim-mach', 'Khám và điều trị các bệnh lý về tim mạch, huyết áp, mạch máu', 'https://example.com/images/tim-mach.jpg', 5, true, 'CARDIOLOGY', NOW(), NOW(), NULL),

-- Thần kinh
('a1b2c3d4-1111-1111-1111-000000000006'::uuid, 'Thần kinh', 'than-kinh', 'Điều trị các bệnh lý thần kinh như đau đầu, tai biến, Parkinson...', 'https://example.com/images/than-kinh.jpg', 6, true, 'NEUROLOGY', NOW(), NOW(), NULL),

-- Da liễu
('a1b2c3d4-1111-1111-1111-000000000007'::uuid, 'Da liễu', 'da-lieu', 'Điều trị các bệnh lý về da như mụn, viêm da, nấm, dị ứng da...', 'https://example.com/images/da-lieu.jpg', 7, true, 'DERMATOLOGY', NOW(), NOW(), NULL),

-- Tai mũi họng
('a1b2c3d4-1111-1111-1111-000000000008'::uuid, 'Tai mũi họng', 'tai-mui-hong', 'Khám và điều trị các bệnh lý về tai, mũi, họng', 'https://example.com/images/tai-mui-hong.jpg', 8, true, 'GENERAL', NOW(), NOW(), NULL),

-- Mắt
('a1b2c3d4-1111-1111-1111-000000000009'::uuid, 'Mắt', 'mat', 'Khám và điều trị các bệnh lý về mắt, cận thị, viễn thị, đục thủy tinh thể...', 'https://example.com/images/mat.jpg', 9, true, 'GENERAL', NOW(), NOW(), NULL),

-- Răng hàm mặt
('a1b2c3d4-1111-1111-1111-000000000010'::uuid, 'Răng hàm mặt', 'rang-ham-mat', 'Nha khoa tổng quát, chăm sóc răng miệng, niềng răng, cấy ghép implant', 'https://example.com/images/rang-ham-mat.jpg', 10, true, 'GENERAL', NOW(), NOW(), NULL),

-- Xương khớp
('a1b2c3d4-1111-1111-1111-000000000011'::uuid, 'Xương khớp', 'xuong-khop', 'Điều trị các bệnh lý về xương khớp, cột sống, gãy xương, thoái hóa khớp', 'https://example.com/images/xuong-khop.jpg', 11, true, 'ORTHOPEDICS', NOW(), NOW(), NULL),

-- Tâm thần
('a1b2c3d4-1111-1111-1111-000000000012'::uuid, 'Tâm thần', 'tam-than', 'Tư vấn và điều trị các rối loạn tâm lý, stress, lo âu, trầm cảm', 'https://example.com/images/tam-than.jpg', 12, true, 'PSYCHIATRY', NOW(), NOW(), NULL),

-- Nội tiết
('a1b2c3d4-1111-1111-1111-000000000013'::uuid, 'Nội tiết', 'noi-tiet', 'Điều trị bệnh lý nội tiết như đái tháo đường, tuyến giáp, hormone', 'https://example.com/images/noi-tiet.jpg', 13, true, 'ENDOCRINOLOGY', NOW(), NOW(), NULL);
```

## 3. Users Table

```sql
INSERT INTO users (id, email, date_of_birth, password_hash, phone, full_name, path_avatar, role, status, gender, email_verified, phone_verified, last_login, created_at, updated_at, deleted_at)
VALUES
-- Admin user
('10000000-0000-0000-0000-000000000001'::uuid, 'admin@clinic.com', '1985-01-15', '$2a$10$N.ePdF9JQQYf1gR3Q9gXO.xL3wKXwKzPvw4eKzf9gXO.xL3wKXwKzP', '0901234567', 'Quản trị viên', 'https://i.pravatar.cc/150?img=1', 'ROLE_ADMIN', 'ACTIVE', 'MALE', true, true, NOW(), NOW(), NOW(), NULL),

-- Doctor users
('10000000-0000-0000-0000-000000000002'::uuid, 'bs.nguyen@clinic.com', '1980-05-20', '$2a$10$N.ePdF9JQQYf1gR3Q9gXO.xL3wKXwKzPvw4eKzf9gXO.xL3wKXwKzP', '0901234568', 'Bác sĩ Nguyễn Văn A', 'https://i.pravatar.cc/150?img=11', 'ROLE_DOCTOR', 'ACTIVE', 'MALE', true, true, NOW(), NOW(), NOW(), NULL),
('10000000-0000-0000-0000-000000000003'::uuid, 'bs.tran@clinic.com', '1985-08-12', '$2a$10$N.ePdF9JQQYf1gR3Q9gXO.xL3wKXwKzPvw4eKzf9gXO.xL3wKXwKzP', '0901234569', 'Bác sĩ Trần Thị B', 'https://i.pravatar.cc/150?img=20', 'ROLE_DOCTOR', 'ACTIVE', 'FEMALE', true, true, NOW(), NOW(), NOW(), NULL),
('10000000-0000-0000-0000-000000000004'::uuid, 'bs.le@clinic.com', '1978-03-25', '$2a$10$N.ePdF9JQQYf1gR3Q9gXO.xL3wKXwKzPvw4eKzf9gXO.xL3wKXwKzP', '0901234570', 'Bác sĩ Lê Văn C', 'https://i.pravatar.cc/150?img=12', 'ROLE_DOCTOR', 'ACTIVE', 'MALE', true, true, NOW(), NOW(), NOW(), NULL),
('10000000-0000-0000-0000-000000000005'::uuid, 'bs.pham@clinic.com', '1982-11-08', '$2a$10$N.ePdF9JQQYf1gR3Q9gXO.xL3wKXwKzPvw4eKzf9gXO.xL3wKXwKzP', '0901234571', 'Bác sĩ Phạm Thị D', 'https://i.pravatar.cc/150?img=21', 'ROLE_DOCTOR', 'ACTIVE', 'FEMALE', true, true, NOW(), NOW(), NOW(), NULL),
('10000000-0000-0000-0000-000000000006'::uuid, 'bs.hoang@clinic.com', '1983-07-14', '$2a$10$N.ePdF9JQQYf1gR3Q9gXO.xL3wKXwKzPvw4eKzf9gXO.xL3wKXwKzP', '0901234572', 'Bác sĩ Hoàng Văn E', 'https://i.pravatar.cc/150?img=13', 'ROLE_DOCTOR', 'ACTIVE', 'MALE', true, true, NOW(), NOW(), NOW(), NULL),

-- Patient users
('10000000-0000-0000-0000-000000000011'::uuid, 'patient1@email.com', '1990-05-15', '$2a$10$N.ePdF9JQQYf1gR3Q9gXO.xL3wKXwKzPvw4eKzf9gXO.xL3wKXwKzP', '0912345678', 'Nguyễn Văn An', 'https://i.pravatar.cc/150?img=31', 'ROLE_PATIENT', 'ACTIVE', 'MALE', true, true, NOW(), NOW(), NOW(), NULL),
('10000000-0000-0000-0000-000000000012'::uuid, 'patient2@email.com', '1985-08-20', '$2a$10$N.ePdF9JQQYf1gR3Q9gXO.xL3wKXwKzPvw4eKzf9gXO.xL3wKXwKzP', '0912345679', 'Trần Thị Bình', 'https://i.pravatar.cc/150?img=32', 'ROLE_PATIENT', 'ACTIVE', 'FEMALE', true, true, NOW(), NOW(), NOW(), NULL),
('10000000-0000-0000-0000-000000000013'::uuid, 'patient3@email.com', '1995-03-10', '$2a$10$N.ePdF9JQQYf1gR3Q9gXO.xL3wKXwKzPvw4eKzf9gXO.xL3wKXwKzP', '0912345680', 'Lê Văn Cường', 'https://i.pravatar.cc/150?img=33', 'ROLE_PATIENT', 'ACTIVE', 'MALE', true, true, NOW(), NOW(), NOW(), NULL),
('10000000-0000-0000-0000-000000000014'::uuid, 'patient4@email.com', '1988-12-25', '$2a$10$N.ePdF9JQQYf1gR3Q9gXO.xL3wKXwKzPvw4eKzf9gXO.xL3wKXwKzP', '0912345681', 'Phạm Thị Dung', 'https://i.pravatar.cc/150?img=34', 'ROLE_PATIENT', 'ACTIVE', 'FEMALE', true, true, NOW(), NOW(), NOW(), NULL),
('10000000-0000-0000-0000-000000000015'::uuid, 'patient5@email.com', '2000-07-08', '$2a$10$N.ePdF9JQQYf1gR3Q9gXO.xL3wKXwKzPvw4eKzf9gXO.xL3wKXwKzP', '0912345682', 'Hoàng Văn Em', 'https://i.pravatar.cc/150?img=35', 'ROLE_PATIENT', 'ACTIVE', 'MALE', true, false, NOW(), NOW(), NOW(), NULL),

-- Staff users
('10000000-0000-0000-0000-000000000021'::uuid, 'staff1@clinic.com', '1992-04-18', '$2a$10$N.ePdF9JQQYf1gR3Q9gXO.xL3wKXwKzPvw4eKzf9gXO.xL3wKXwKzP', '0923456789', 'Võ Thị Phương', 'https://i.pravatar.cc/150?img=41', 'ROLE_STAFF', 'ACTIVE', 'FEMALE', true, true, NOW(), NOW(), NOW(), NULL),
('10000000-0000-0000-0000-000000000022'::uuid, 'staff2@clinic.com', '1988-09-22', '$2a$10$N.ePdF9JQQYf1gR3Q9gXO.xL3wKXwKzPvw4eKzf9gXO.xL3wKXwKzP', '0923456790', 'Đặng Văn Quân', 'https://i.pravatar.cc/150?img=42', 'ROLE_STAFF', 'ACTIVE', 'MALE', true, true, NOW(), NOW(), NOW(), NULL);
```

## 4. Doctors Table

```sql
INSERT INTO doctors (id, user_id, doctor_code, specialty_id, degree, experience_years, education, bio, consultation_fee, average_rating, total_reviews, total_patients, is_featured, status, created_at, updated_at, deleted_at)
VALUES
('20000000-0000-0000-0000-000000000001'::uuid, '10000000-0000-0000-0000-000000000002'::uuid, 'DOC001', 'a1b2c3d4-1111-1111-1111-000000000001'::uuid, 'Tiến sĩ', 15, 'Đại học Y Hà Nội, Tiến sĩ Y khoa Pháp', 'Bác sĩ có 15 năm kinh nghiệm trong điều trị các bệnh lý nội khoa, đặc biệt là tiểu đường và huyết áp', 300000, 4.8, 120, 450, true, 'active', NOW(), NOW(), NULL),

('20000000-0000-0000-0000-000000000002'::uuid, '10000000-0000-0000-0000-000000000003'::uuid, 'DOC002', 'a1b2c3d4-1111-1111-1111-000000000003'::uuid, 'Thạc sĩ', 10, 'Đại học Y Dược TP.HCM, Thạc sĩ Nhi khoa', 'Chuyên khoa nhi với kinh nghiệm điều trị các bệnh lý trẻ em, đặc biệt là hô hấp và tiêu hóa', 250000, 4.9, 200, 650, true, 'active', NOW(), NOW(), NULL),

('20000000-0000-0000-0000-000000000003'::uuid, '10000000-0000-0000-0000-000000000004'::uuid, 'DOC003', 'a1b2c3d4-1111-1111-1111-000000000005'::uuid, 'Tiến sĩ', 20, 'Đại học Y Hà Nội, Tiến sĩ Tim mạch Nhật Bản', 'Chuyên gia tim mạch hàng đầu với nhiều công trình nghiên cứu quốc tế', 400000, 4.95, 180, 520, true, 'active', NOW(), NOW(), NULL),

('20000000-0000-0000-0000-000000000004'::uuid, '10000000-0000-0000-0000-000000000005'::uuid, 'DOC004', 'a1b2c3d4-1111-1111-1111-000000000007'::uuid, 'Bác sĩ', 8, 'Đại học Y Dược Cần Thơ', 'Bác sĩ da liễu với kinh nghiệm điều trị mụn, nám, lão hóa da', 200000, 4.7, 95, 380, false, 'active', NOW(), NOW(), NULL),

('20000000-0000-0000-0000-000000000005'::uuid, '10000000-0000-0000-0000-000000000006'::uuid, 'DOC005', 'a1b2c3d4-1111-1111-1111-000000000011'::uuid, 'Thạc sĩ', 12, 'Đại học Y Hải Phòng, Thạc sĩ Chấn thương chỉnh hình', 'Chuyên điều trị các chấn thương thể thao và thoái hóa khớp', 280000, 4.75, 150, 480, true, 'active', NOW(), NOW(), NULL);
```

## 5. Patients Table

```sql
INSERT INTO patients (id, user_id, patient_code, date_of_birth, gender, address, insurance_number, blood_type, allergies, chronic_diseases, loyalty_points, total_visits, created_at, updated_at, deleted_at)
VALUES
('30000000-0000-0000-0000-000000000001'::uuid, '10000000-0000-0000-0000-000000000011'::uuid, 'PAT001', '1990-05-15', 'male', '123 Nguyễn Huệ, Q1, TP.HCM', 'BH123456789', 'O_POSITIVE', 'Không có', 'Không có', 100, 5, NOW(), NOW(), NULL),

('30000000-0000-0000-0000-000000000002'::uuid, '10000000-0000-0000-0000-000000000012'::uuid, 'PAT002', '1985-08-20', 'female', '456 Lê Lợi, Q1, TP.HCM', 'BH987654321', 'A_POSITIVE', 'Dị ứng penicillin', 'Cao huyết áp', 250, 12, NOW(), NOW(), NULL),

('30000000-0000-0000-0000-000000000003'::uuid, '10000000-0000-0000-0000-000000000013'::uuid, 'PAT003', '1995-03-10', 'male', '789 Trần Hưng Đạo, Q5, TP.HCM', NULL, 'B_POSITIVE', 'Không có', 'Không có', 50, 3, NOW(), NOW(), NULL),

('30000000-0000-0000-0000-000000000004'::uuid, '10000000-0000-0000-0000-000000000014'::uuid, 'PAT004', '1988-12-25', 'female', '321 Võ Thị Sáu, Q3, TP.HCM', 'BH555666777', 'AB_POSITIVE', 'Dị ứng hải sản', 'Tiểu đường type 2', 180, 8, NOW(), NOW(), NULL),

('30000000-0000-0000-0000-000000000005'::uuid, '10000000-0000-0000-0000-000000000015'::uuid, 'PAT005', '2000-07-08', 'male', '654 Pasteur, Q1, TP.HCM', NULL, 'O_NEGATIVE', 'Không có', 'Không có', 0, 1, NOW(), NOW(), NULL);
```

## 6. Staff Table

```sql
INSERT INTO staff (id, user_id, staff_code, position, department, hire_date, status, created_at, updated_at, deleted_at)
VALUES
('40000000-0000-0000-0000-000000000001'::uuid, '10000000-0000-0000-0000-000000000021'::uuid, 'STF001', 'Y tá trưởng', 'Khoa Nội', '2020-01-15', 'active', NOW(), NOW(), NULL),

('40000000-0000-0000-0000-000000000002'::uuid, '10000000-0000-0000-0000-000000000022'::uuid, 'STF002', 'Lễ tân', 'Tiếp nhận', '2021-06-01', 'active', NOW(), NOW(), NULL);
```

## 7. Services Table

```sql
INSERT INTO services (id, specialty_id, name, slug, description, price, promotional_price, duration, image, is_featured, is_active, created_at, updated_at, deleted_at)
VALUES
('50000000-0000-0000-0000-000000000001'::uuid, 'a1b2c3d4-1111-1111-1111-000000000001'::uuid, 'Khám nội khoa tổng quát', 'kham-noi-khoa-tong-quat', 'Khám sức khỏe tổng quát, tầm soát các bệnh lý nội khoa', 200000, 150000, 30, 'https://example.com/service-noi-khoa.jpg', true, true, NOW(), NOW(), NULL),

('50000000-0000-0000-0000-000000000002'::uuid, 'a1b2c3d4-1111-1111-1111-000000000003'::uuid, 'Khám nhi khoa', 'kham-nhi-khoa', 'Khám sức khỏe cho trẻ em, tư vấn dinh dưỡng, tiêm chủng', 180000, NULL, 30, 'https://example.com/service-nhi-khoa.jpg', true, true, NOW(), NOW(), NULL),

('50000000-0000-0000-0000-000000000003'::uuid, 'a1b2c3d4-1111-1111-1111-000000000005'::uuid, 'Siêu âm tim', 'sieu-am-tim', 'Siêu âm tim mạch, đánh giá chức năng tim', 350000, 300000, 45, 'https://example.com/service-sieu-am-tim.jpg', true, true, NOW(), NOW(), NULL),

('50000000-0000-0000-0000-000000000004'::uuid, 'a1b2c3d4-1111-1111-1111-000000000007'::uuid, 'Điều trị mụn chuyên sâu', 'dieu-tri-mun-chuyen-sau', 'Điều trị mụn, mụn bọc, thâm mụn bằng công nghệ hiện đại', 500000, 450000, 60, 'https://example.com/service-tri-mun.jpg', true, true, NOW(), NOW(), NULL),

('50000000-0000-0000-0000-000000000005'::uuid, 'a1b2c3d4-1111-1111-1111-000000000011'::uuid, 'Khám xương khớp', 'kham-xuong-khop', 'Khám và điều trị các bệnh lý xương khớp, cột sống', 250000, NULL, 30, 'https://example.com/service-xuong-khop.jpg', false, true, NOW(), NOW(), NULL),

('50000000-0000-0000-0000-000000000006'::uuid, NULL, 'Xét nghiệm máu tổng quát', 'xet-nghiem-mau-tong-quat', 'Xét nghiệm công thức máu, sinh hóa máu cơ bản', 150000, NULL, 15, 'https://example.com/service-xet-nghiem.jpg', false, true, NOW(), NOW(), NULL);
```

## 8. Medications Table

```sql
INSERT INTO medications (id, name, generic_name, category, form, strength, unit, price, is_active, created_at, updated_at, deleted_at)
VALUES
('60000000-0000-0000-0000-000000000001'::uuid, 'Paracetamol 500mg', 'Paracetamol', 'Giảm đau, hạ sốt', 'Viên nén', '500mg', 'viên', 2000, true, NOW(), NOW(), NULL),

('60000000-0000-0000-0000-000000000002'::uuid, 'Amoxicillin 500mg', 'Amoxicillin', 'Kháng sinh', 'Viên nang', '500mg', 'viên', 5000, true, NOW(), NOW(), NULL),

('60000000-0000-0000-0000-000000000003'::uuid, 'Vitamin C 1000mg', 'Acid ascorbic', 'Vitamin', 'Viên sủi', '1000mg', 'viên', 8000, true, NOW(), NOW(), NULL),

('60000000-0000-0000-0000-000000000004'::uuid, 'Metformin 500mg', 'Metformin HCl', 'Điều trị tiểu đường', 'Viên nén', '500mg', 'viên', 3000, true, NOW(), NOW(), NULL),

('60000000-0000-0000-0000-000000000005'::uuid, 'Amlodipine 5mg', 'Amlodipine besylate', 'Hạ huyết áp', 'Viên nén', '5mg', 'viên', 4000, true, NOW(), NOW(), NULL),

('60000000-0000-0000-0000-000000000006'::uuid, 'Omeprazole 20mg', 'Omeprazole', 'Điều trị dạ dày', 'Viên nang', '20mg', 'viên', 6000, true, NOW(), NOW(), NULL),

('60000000-0000-0000-0000-000000000007'::uuid, 'Cetirizine 10mg', 'Cetirizine HCl', 'Chống dị ứng', 'Viên nén', '10mg', 'viên', 2500, true, NOW(), NOW(), NULL),

('60000000-0000-0000-0000-000000000008'::uuid, 'Ibuprofen 400mg', 'Ibuprofen', 'Giảm đau, chống viêm', 'Viên nén', '400mg', 'viên', 3500, true, NOW(), NOW(), NULL);
```

## 9. Appointments Table

```sql
INSERT INTO appointments (id, appointment_code, patient_id, doctor_id, service_id, appointment_date, start_time, end_time, status, booking_type, reason, symptoms, notes, queue_number, created_at, updated_at, deleted_at)
VALUES
('70000000-0000-0000-0000-000000000001'::uuid, 'APT20260301001', '30000000-0000-0000-0000-000000000001'::uuid, '20000000-0000-0000-0000-000000000001'::uuid, '50000000-0000-0000-0000-000000000001'::uuid, '2026-03-05', '09:00:00', '09:30:00', 'confirmed', 'online', 'Khám sức khỏe định kỳ', 'Không có triệu chứng đặc biệt', NULL, 1, NOW(), NOW(), NULL),

('70000000-0000-0000-0000-000000000002'::uuid, 'APT20260301002', '30000000-0000-0000-0000-000000000002'::uuid, '20000000-0000-0000-0000-000000000002'::uuid, '50000000-0000-0000-0000-000000000002'::uuid, '2026-03-05', '10:00:00', '10:30:00', 'confirmed', 'phone', 'Con bị sốt và ho', 'Sốt 38.5 độ, ho có đờm', NULL, 2, NOW(), NOW(), NULL),

('70000000-0000-0000-0000-000000000003'::uuid, 'APT20260301003', '30000000-0000-0000-0000-000000000003'::uuid, '20000000-0000-0000-0000-000000000003'::uuid, '50000000-0000-0000-0000-000000000003'::uuid, '2026-03-06', '14:00:00', '14:45:00', 'pending', 'online', 'Đau ngực, khó thở', 'Đau ngực khi gắng sức', NULL, NULL, NOW(), NOW(), NULL),

('70000000-0000-0000-0000-000000000004'::uuid, 'APT20260228001', '30000000-0000-0000-0000-000000000004'::uuid, '20000000-0000-0000-0000-000000000004'::uuid, '50000000-0000-0000-0000-000000000004'::uuid, '2026-02-28', '15:00:00', '16:00:00', 'completed', 'online', 'Điều trị mụn', 'Mụn trứng cá nhiều ở má và trán', 'Đã khám xong, kê đơn thuốc', 3, NOW() - INTERVAL '2 days', NOW(), NULL),

('70000000-0000-0000-0000-000000000005'::uuid, 'APT20260227001', '30000000-0000-0000-0000-000000000001'::uuid, '20000000-0000-0000-0000-000000000005'::uuid, '50000000-0000-0000-0000-000000000005'::uuid, '2026-02-27', '09:00:00', '09:30:00', 'completed', 'walk_in', 'Đau khớp gối', 'Đau khi đi bộ lâu', 'Đã điều trị, tái khám sau 2 tuần', 1, NOW() - INTERVAL '3 days', NOW(), NULL);
```

## 10. Medical Records Table

```sql
INSERT INTO medical_records (id, record_code, appointment_id, patient_id, doctor_id, chief_complaint, vital_signs, diagnosis, treatment_plan, follow_up_date, doctor_notes, created_at, updated_at, deleted_at)
VALUES
('80000000-0000-0000-0000-000000000001'::uuid, 'MR20260228001', '70000000-0000-0000-0000-000000000004'::uuid, '30000000-0000-0000-0000-000000000004'::uuid, '20000000-0000-0000-0000-000000000004'::uuid, 'Mụn trứng cá', '{"temperature": 36.5, "bloodPressure": "120/80", "heartRate": 75, "weight": 55}', 'Mụn trứng cá mức độ trung bình, viêm nhẹ', 'Kê đơn thuốc bôi ngoài và uống, hướng dẫn chăm sóc da', '2026-03-14', 'Bệnh nhân cần kiêng ăn đồ chiên rán, uống nhiều nước', NOW() - INTERVAL '2 days', NOW(), NULL),

('80000000-0000-0000-0000-000000000002'::uuid, 'MR20260227001', '70000000-0000-0000-0000-000000000005'::uuid, '30000000-0000-0000-0000-000000000001'::uuid, '20000000-0000-0000-0000-000000000005'::uuid, 'Đau khớp gối', '{"temperature": 36.8, "bloodPressure": "130/85", "heartRate": 72, "weight": 70}', 'Thoái hóa khớp gối độ 1', 'Vật lý trị liệu, kê đơn thuốc giảm đau và bổ khớp', '2026-03-13', 'Hạn chế vận động mạnh, tập vật lý trị liệu đều đặn', NOW() - INTERVAL '3 days', NOW(), NULL);
```

## 11. Prescriptions Table

```sql
INSERT INTO prescriptions (id, prescription_code, medical_record_id, patient_id, doctor_id, prescription_date, notes, status, created_at, updated_at, deleted_at)
VALUES
('90000000-0000-0000-0000-000000000001'::uuid, 'PRE20260228001', '80000000-0000-0000-0000-000000000001'::uuid, '30000000-0000-0000-0000-000000000004'::uuid, '20000000-0000-0000-0000-000000000004'::uuid, '2026-02-28', 'Uống thuốc đúng giờ, tránh thức khuya', 'active', NOW() - INTERVAL '2 days', NOW(), NULL),

('90000000-0000-0000-0000-000000000002'::uuid, 'PRE20260227001', '80000000-0000-0000-0000-000000000002'::uuid, '30000000-0000-0000-0000-000000000001'::uuid, '20000000-0000-0000-0000-000000000005'::uuid, '2026-02-27', 'Uống sau ăn, nghỉ ngơi hợp lý', 'active', NOW() - INTERVAL '3 days', NOW(), NULL);
```

## 12. Prescription Items Table

```sql
INSERT INTO prescription_items (id, prescription_id, medication_id, dosage, frequency, duration, quantity, instructions, created_at, updated_at, deleted_at)
VALUES
-- Đơn thuốc 1 (điều trị mụn)
('A0000000-0000-0000-0000-000000000001'::uuid, '90000000-0000-0000-0000-000000000001'::uuid, '60000000-0000-0000-0000-000000000007'::uuid, '1 viên', '1 lần/ngày', '14 ngày', 14, 'Uống vào buổi tối trước khi ngủ', NOW() - INTERVAL '2 days', NOW(), NULL),

('A0000000-0000-0000-0000-000000000002'::uuid, '90000000-0000-0000-0000-000000000001'::uuid, '60000000-0000-0000-0000-000000000003'::uuid, '1 viên', '1 lần/ngày', '30 ngày', 30, 'Pha với nước, uống vào buổi sáng', NOW() - INTERVAL '2 days', NOW(), NULL),

-- Đơn thuốc 2 (điều trị đau khớp)
('A0000000-0000-0000-0000-000000000003'::uuid, '90000000-0000-0000-0000-000000000002'::uuid, '60000000-0000-0000-0000-000000000008'::uuid, '1 viên', '2 lần/ngày', '7 ngày', 14, 'Uống sau bữa ăn sáng và tối', NOW() - INTERVAL '3 days', NOW(), NULL),

('A0000000-0000-0000-0000-000000000004'::uuid, '90000000-0000-0000-0000-000000000002'::uuid, '60000000-0000-0000-0000-000000000003'::uuid, '1 viên', '1 lần/ngày', '30 ngày', 30, 'Bổ sung vitamin C, uống buổi sáng', NOW() - INTERVAL '3 days', NOW(), NULL);
```

## 13. Invoices Table

```sql
INSERT INTO invoices (id, invoice_code, appointment_id, patient_id, invoice_date, subtotal, discount_amount, total_amount, insurance_covered, patient_paid, balance, status, created_at, updated_at, deleted_at)
VALUES
('B0000000-0000-0000-0000-000000000001'::uuid, 'INV20260228001', '70000000-0000-0000-0000-000000000004'::uuid, '30000000-0000-0000-0000-000000000004'::uuid, '2026-02-28', 578000, 0, 578000, 200000, 378000, 0, 'paid', NOW() - INTERVAL '2 days', NOW(), NULL),

('B0000000-0000-0000-0000-000000000002'::uuid, 'INV20260227001', '70000000-0000-0000-0000-000000000005'::uuid, '30000000-0000-0000-0000-000000000001'::uuid, '2026-02-27', 400000, 0, 400000, 0, 400000, 0, 'paid', NOW() - INTERVAL '3 days', NOW(), NULL);
```

## 14. Invoice Items Table

```sql
INSERT INTO invoice_items (id, invoice_id, item_type, item_name, quantity, unit_price, total_price, created_at, updated_at, deleted_at)
VALUES
-- Hóa đơn 1
('C0000000-0000-0000-0000-000000000001'::uuid, 'B0000000-0000-0000-0000-000000000001'::uuid, 'service', 'Điều trị mụn chuyên sâu', 1, 450000, 450000, NOW() - INTERVAL '2 days', NOW(), NULL),

('C0000000-0000-0000-0000-000000000002'::uuid, 'B0000000-0000-0000-0000-000000000001'::uuid, 'medication', 'Cetirizine 10mg', 14, 2500, 35000, NOW() - INTERVAL '2 days', NOW(), NULL),

('C0000000-0000-0000-0000-000000000003'::uuid, 'B0000000-0000-0000-0000-000000000001'::uuid, 'medication', 'Vitamin C 1000mg', 30, 3100, 93000, NOW() - INTERVAL '2 days', NOW(), NULL),

-- Hóa đơn 2
('C0000000-0000-0000-0000-000000000004'::uuid, 'B0000000-0000-0000-0000-000000000002'::uuid, 'service', 'Khám xương khớp', 1, 250000, 250000, NOW() - INTERVAL '3 days', NOW(), NULL),

('C0000000-0000-0000-0000-000000000005'::uuid, 'B0000000-0000-0000-0000-000000000002'::uuid, 'medication', 'Ibuprofen 400mg', 14, 3500, 49000, NOW() - INTERVAL '3 days', NOW(), NULL),

('C0000000-0000-0000-0000-000000000006'::uuid, 'B0000000-0000-0000-0000-000000000002'::uuid, 'medication', 'Vitamin C 1000mg', 30, 3367, 101000, NOW() - INTERVAL '3 days', NOW(), NULL);
```

## 15. Payments Table

```sql
INSERT INTO payments (id, payment_code, invoice_id, patient_id, amount, payment_method, payment_date, status, created_at, updated_at, deleted_at)
VALUES
('D0000000-0000-0000-0000-000000000001'::uuid, 'PAY20260228001', 'B0000000-0000-0000-0000-000000000001'::uuid, '30000000-0000-0000-0000-000000000004'::uuid, 378000, 'momo', NOW() - INTERVAL '2 days', 'completed', NOW() - INTERVAL '2 days', NOW(), NULL),

('D0000000-0000-0000-0000-000000000002'::uuid, 'PAY20260227001', 'B0000000-0000-0000-0000-000000000002'::uuid, '30000000-0000-0000-0000-000000000001'::uuid, 400000, 'cash', NOW() - INTERVAL '3 days', 'completed', NOW() - INTERVAL '3 days', NOW(), NULL);
```

## 16. Reviews Table

```sql
INSERT INTO reviews (id, patient_id, doctor_id, appointment_id, rating, title, content, status, created_at, updated_at, deleted_at)
VALUES
('E0000000-0000-0000-0000-000000000001'::uuid, '30000000-0000-0000-0000-000000000004'::uuid, '20000000-0000-0000-0000-000000000004'::uuid, '70000000-0000-0000-0000-000000000004'::uuid, 5, 'Bác sĩ rất tận tâm', 'Bác sĩ khám rất kỹ, tư vấn chi tiết. Sau điều trị da mình đã cải thiện rõ rệt.', 'approved', NOW() - INTERVAL '1 day', NOW(), NULL),

('E0000000-0000-0000-0000-000000000002'::uuid, '30000000-0000-0000-0000-000000000001'::uuid, '20000000-0000-0000-0000-000000000005'::uuid, '70000000-0000-0000-0000-000000000005'::uuid, 5, 'Chuyên môn cao', 'Bác sĩ giỏi, nhiệt tình. Sau điều trị đau khớp đã giảm đáng kể.', 'approved', NOW() - INTERVAL '2 days', NOW(), NULL),

('E0000000-0000-0000-0000-000000000003'::uuid, '30000000-0000-0000-0000-000000000002'::uuid, '20000000-0000-0000-0000-000000000002'::uuid, NULL, 4, 'Bác sĩ nhi khoa giỏi', 'Bác sĩ rất yêu trẻ, khám nhẹ nhàng. Con mình rất thích.', 'approved', NOW() - INTERVAL '5 days', NOW(), NULL);
```

## 17. Promotions Table

```sql
INSERT INTO promotions (id, code, name, description, discount_type, discount_value, min_purchase_amount, max_discount_amount, usage_limit, usage_count, usage_per_user, applicable_services, start_date, end_date, is_active, created_at, updated_at, deleted_at)
VALUES
('F0000000-0000-0000-0000-000000000001'::uuid, 'KHAMTQ2026', 'Khám tổng quát giảm 25%', 'Giảm giá 25% cho dịch vụ khám tổng quát', 'percentage', 25, 150000, 100000, 100, 5, 1, '["50000000-0000-0000-0000-000000000001"]', '2026-03-01 00:00:00', '2026-03-31 23:59:59', true, NOW(), NOW(), NULL),

('F0000000-0000-0000-0000-000000000002'::uuid, 'WELCOME50K', 'Chào mừng khách hàng mới', 'Giảm 50.000đ cho khách hàng đăng ký mới', 'fixed_amount', 50000, 200000, NULL, 500, 15, 1, NULL, '2026-03-01 00:00:00', '2026-12-31 23:59:59', true, NOW(), NOW(), NULL),

('F0000000-0000-0000-0000-000000000003'::uuid, 'KHAMNHI30', 'Giảm giá khám nhi', 'Giảm 30% cho dịch vụ khám nhi khoa', 'percentage', 30, 100000, 80000, 200, 3, 2, '["50000000-0000-0000-0000-000000000002"]', '2026-03-01 00:00:00', '2026-03-31 23:59:59', true, NOW(), NOW(), NULL);
```

## 18. Promotion Usage Table

```sql
INSERT INTO promotion_usage (id, promotion_id, user_id, invoice_id, discount_amount, used_at, created_at, updated_at, deleted_at)
VALUES
('G0000000-0000-0000-0000-000000000001'::uuid, 'F0000000-0000-0000-0000-000000000001'::uuid, '10000000-0000-0000-0000-000000000011'::uuid, NULL, 50000, NOW() - INTERVAL '10 days', NOW() - INTERVAL '10 days', NOW(), NULL),

('G0000000-0000-0000-0000-000000000002'::uuid, 'F0000000-0000-0000-0000-000000000002'::uuid, '10000000-0000-0000-0000-000000000012'::uuid, NULL, 50000, NOW() - INTERVAL '15 days', NOW() - INTERVAL '15 days', NOW(), NULL);
```

## 19. Doctor Schedules Table

```sql
INSERT INTO doctor_schedules (id, doctor_id, day_of_week, start_time, end_time, slot_duration, max_patients_per_slot, location, is_active, created_at, updated_at, deleted_at)
VALUES
-- Lịch bác sĩ Nguyễn Văn A (DOC001)
('H0000000-0000-0000-0000-000000000001'::uuid, '20000000-0000-0000-0000-000000000001'::uuid, 1, '08:00:00', '12:00:00', 30, 1, 'Phòng khám 101', true, NOW(), NOW(), NULL),
('H0000000-0000-0000-0000-000000000002'::uuid, '20000000-0000-0000-0000-000000000001'::uuid, 1, '13:00:00', '17:00:00', 30, 1, 'Phòng khám 101', true, NOW(), NOW(), NULL),
('H0000000-0000-0000-0000-000000000003'::uuid, '20000000-0000-0000-0000-000000000001'::uuid, 3, '08:00:00', '12:00:00', 30, 1, 'Phòng khám 101', true, NOW(), NOW(), NULL),
('H0000000-0000-0000-0000-000000000004'::uuid, '20000000-0000-0000-0000-000000000001'::uuid, 5, '08:00:00', '12:00:00', 30, 1, 'Phòng khám 101', true, NOW(), NOW(), NULL),

-- Lịch bác sĩ Trần Thị B (DOC002)
('H0000000-0000-0000-0000-000000000005'::uuid, '20000000-0000-0000-0000-000000000002'::uuid, 2, '08:00:00', '12:00:00', 30, 1, 'Phòng khám 102', true, NOW(), NOW(), NULL),
('H0000000-0000-0000-0000-000000000006'::uuid, '20000000-0000-0000-0000-000000000002'::uuid, 4, '13:00:00', '17:00:00', 30, 1, 'Phòng khám 102', true, NOW(), NOW(), NULL),
('H0000000-0000-0000-0000-000000000007'::uuid, '20000000-0000-0000-0000-000000000002'::uuid, 6, '08:00:00', '12:00:00', 30, 1, 'Phòng khám 102', true, NOW(), NOW(), NULL),

-- Lịch bác sĩ Lê Văn C (DOC003)
('H0000000-0000-0000-0000-000000000008'::uuid, '20000000-0000-0000-0000-000000000003'::uuid, 1, '13:00:00', '17:00:00', 45, 1, 'Phòng khám 201', true, NOW(), NOW(), NULL),
('H0000000-0000-0000-0000-000000000009'::uuid, '20000000-0000-0000-0000-000000000003'::uuid, 3, '13:00:00', '17:00:00', 45, 1, 'Phòng khám 201', true, NOW(), NOW(), NULL);
```

## 20. Doctor Leave Table

```sql
INSERT INTO doctor_leave (id, doctor_id, leave_date, start_time, end_time, reason, status, created_at, updated_at, deleted_at)
VALUES
('I0000000-0000-0000-0000-000000000001'::uuid, '20000000-0000-0000-0000-000000000001'::uuid, '2026-03-10', NULL, NULL, 'Tham gia hội nghị y khoa', 'approved', NOW(), NOW(), NULL),

('I0000000-0000-0000-0000-000000000002'::uuid, '20000000-0000-0000-0000-000000000002'::uuid, '2026-03-15', '13:00:00', '17:00:00', 'Làm việc cá nhân', 'pending', NOW(), NOW(), NULL);
```

## 21. Inventory Table

```sql
INSERT INTO inventory (id, medication_id, batch_number, quantity, expiry_date, supplier, status, alert_threshold, created_at, updated_at, deleted_at)
VALUES
('J0000000-0000-0000-0000-000000000001'::uuid, '60000000-0000-0000-0000-000000000001'::uuid, 'BATCH-PAR-2026-001', 5000, '2027-12-31', 'Công ty Dược phẩm ABC', 'in_stock', 500, NOW(), NOW(), NULL),

('J0000000-0000-0000-0000-000000000002'::uuid, '60000000-0000-0000-0000-000000000002'::uuid, 'BATCH-AMO-2026-001', 3000, '2027-06-30', 'Công ty Dược phẩm XYZ', 'in_stock', 300, NOW(), NOW(), NULL),

('J0000000-0000-0000-0000-000000000003'::uuid, '60000000-0000-0000-0000-000000000003'::uuid, 'BATCH-VTC-2026-001', 2000, '2028-12-31', 'Công ty Vitamin VN', 'in_stock', 200, NOW(), NOW(), NULL),

('J0000000-0000-0000-0000-000000000004'::uuid, '60000000-0000-0000-0000-000000000004'::uuid, 'BATCH-MET-2026-001', 1500, '2027-09-30', 'Công ty Dược phẩm ABC', 'in_stock', 200, NOW(), NOW(), NULL),

('J0000000-0000-0000-0000-000000000005'::uuid, '60000000-0000-0000-0000-000000000005'::uuid, 'BATCH-AML-2026-001', 500, '2027-03-31', 'Công ty Dược phẩm XYZ', 'in_stock', 100, NOW(), NOW(), NULL),

('J0000000-0000-0000-0000-000000000006'::uuid, '60000000-0000-0000-0000-000000000006'::uuid, 'BATCH-OME-2025-012', 80, '2026-06-30', 'Công ty Dược phẩm ABC', 'low_stock', 100, NOW(), NOW(), NULL);
```

## 22. Medical Equipment Table

```sql
INSERT INTO medical_equipment (id, equipment_code, name, category, manufacturer, model, serial_number, purchase_date, purchase_price, warranty_expiry, location, status, last_maintenance_date, next_maintenance_date, maintenance_interval_days, notes, created_at, updated_at, deleted_at)
VALUES
('K0000000-0000-0000-0000-000000000001'::uuid, 'EQP-US-001', 'Máy siêu âm tim', 'Siêu âm', 'GE Healthcare', 'Vivid E95', 'SN-VE95-2024-001', '2024-01-15', 850000000, '2027-01-15', 'Phòng siêu âm 1', 'operational', '2025-12-01', '2026-03-01', 90, 'Máy hoạt động tốt', NOW(), NOW(), NULL),

('K0000000-0000-0000-0000-000000000002'::uuid, 'EQP-XR-001', 'Máy X-quang kỹ thuật số', 'X-quang', 'Philips', 'DigitalDiagnost C90', 'SN-DDC90-2023-001', '2023-06-20', 1200000000, '2026-06-20', 'Phòng X-quang', 'operational', '2025-11-15', '2026-02-13', 90, NULL, NOW(), NOW(), NULL),

('K0000000-0000-0000-0000-000000000003'::uuid, 'EQP-ECG-001', 'Máy điện tim', 'Điện tim', 'Fukuda Denshi', 'CardiMax FX-8322', 'SN-CM-2024-001', '2024-03-10', 45000000, '2027-03-10', 'Phòng khám 201', 'operational', '2025-12-20', '2026-03-20', 90, NULL, NOW(), NOW(), NULL),

('K0000000-0000-0000-0000-000000000004'::uuid, 'EQP-END-001', 'Máy nội soi', 'Nội soi', 'Olympus', 'EVIS EXERA III', 'SN-EE3-2022-001', '2022-08-15', 650000000, '2025-08-15', 'Phòng nội soi', 'under_maintenance', '2025-11-01', '2026-01-30', 90, 'Đang bảo trì định kỳ', NOW(), NOW(), NULL);
```

## 23. Equipment Maintenance Table

```sql
INSERT INTO equipment_maintenance (id, equipment_id, maintenance_type, scheduled_date, completed_date, performed_by, cost, description, issues_found, actions_taken, status, created_at, updated_at, deleted_at)
VALUES
('L0000000-0000-0000-0000-000000000001'::uuid, 'K0000000-0000-0000-0000-000000000001'::uuid, 'routine', '2026-03-01', '2026-03-01', 'Công ty GE Healthcare VN', 15000000, 'Bảo trì định kỳ 3 tháng', 'Không có vấn đề', 'Vệ sinh, hiệu chỉnh, kiểm tra toàn bộ hệ thống', 'completed', NOW() - INTERVAL '1 day', NOW(), NULL),

('L0000000-0000-0000-0000-000000000002'::uuid, 'K0000000-0000-0000-0000-000000000004'::uuid, 'routine', '2026-02-28', NULL, 'Công ty Olympus VN', NULL, 'Bảo trì và vệ sinh định kỳ', NULL, NULL, 'in_progress', NOW() - INTERVAL '2 days', NOW(), NULL),

('L0000000-0000-0000-0000-000000000003'::uuid, 'K0000000-0000-0000-0000-000000000002'::uuid, 'calibration', '2026-02-13', '2026-02-13', 'Công ty Philips VN', 8000000, 'Hiệu chỉnh và kiểm tra độ chính xác', 'Cần điều chỉnh nhẹ', 'Đã hiệu chỉnh theo tiêu chuẩn', 'completed', NOW() - INTERVAL '17 days', NOW(), NULL);
```

## 24. Loyalty Transactions Table

```sql
INSERT INTO loyalty_transactions (id, patient_id, transaction_type, points, reference_type, reference_id, description, balance_after, expires_at, created_at, updated_at, deleted_at)
VALUES
('M0000000-0000-0000-0000-000000000001'::uuid, '30000000-0000-0000-0000-000000000001'::uuid, 'earn', 50, 'appointment', NULL, 'Hoàn thành lịch khám', 50, '2027-02-27', NOW() - INTERVAL '3 days', NOW(), NULL),

('M0000000-0000-0000-0000-000000000002'::uuid, '30000000-0000-0000-0000-000000000001'::uuid, 'earn', 50, 'payment', NULL, 'Thanh toán hóa đơn INV20260227001', 100, '2027-02-27', NOW() - INTERVAL '3 days', NOW(), NULL),

('M0000000-0000-0000-0000-000000000003'::uuid, '30000000-0000-0000-0000-000000000002'::uuid, 'earn', 100, 'appointment', NULL, 'Hoàn thành lịch khám', 100, '2027-02-28', NOW() - INTERVAL '2 days', NOW(), NULL),

('M0000000-0000-0000-0000-000000000004'::uuid, '30000000-0000-0000-0000-000000000002'::uuid, 'earn', 150, 'payment', NULL, 'Thanh toán hóa đơn', 250, '2027-02-28', NOW() - INTERVAL '2 days', NOW(), NULL),

('M0000000-0000-0000-0000-000000000005'::uuid, '30000000-0000-0000-0000-000000000004'::uuid, 'earn', 80, 'appointment', NULL, 'Hoàn thành lịch khám', 80, '2027-02-28', NOW() - INTERVAL '2 days', NOW(), NULL),

('M0000000-0000-0000-0000-000000000006'::uuid, '30000000-0000-0000-0000-000000000004'::uuid, 'earn', 100, 'payment', NULL, 'Thanh toán hóa đơn INV20260228001', 180, '2027-02-28', NOW() - INTERVAL '2 days', NOW(), NULL);
```

## 25. FAQs Table

```sql
INSERT INTO faqs (id, category, question, answer, display_order, is_active, created_at, updated_at, deleted_at)
VALUES
('10000000-0000-0000-0000-000000000001'::uuid, 'Đặt lịch khám', 'Làm thế nào để đặt lịch khám?', 'Bạn có thể đặt lịch khám qua website, ứng dụng di động hoặc gọi điện trực tiếp đến tổng đài 1900-xxxx. Chọn chuyên khoa, bác sĩ, ngày giờ phù hợp và điền thông tin cá nhân.', 1, true, NOW(), NOW(), NULL),

('10000000-0000-0000-0000-000000000002'::uuid, 'Đặt lịch khám', 'Tôi có thể hủy lịch khám không?', 'Có, bạn có thể hủy lịch khám trước giờ hẹn ít nhất 2 tiếng. Vui lòng liên hệ tổng đài hoặc hủy trực tiếp trên ứng dụng.', 2, true, NOW(), NOW(), NULL),

('10000000-0000-0000-0000-000000000003'::uuid, 'Thanh toán', 'Phòng khám chấp nhận hình thức thanh toán nào?', 'Chúng tôi chấp nhận thanh toán bằng tiền mặt, thẻ ATM/Credit, chuyển khoản ngân hàng, Momo và VNPay.', 1, true, NOW(), NOW(), NULL),

('10000000-0000-0000-0000-000000000004'::uuid, 'Thanh toán', 'Tôi có được hoàn tiền khi hủy lịch không?', 'Nếu hủy trước 24 giờ, bạn sẽ được hoàn 100% phí đặt lịch. Hủy trong vòng 24 giờ sẽ được hoàn 50%.', 2, true, NOW(), NOW(), NULL),

('10000000-0000-0000-0000-000000000005'::uuid, 'Bảo hiểm', 'Phòng khám có chấp nhận bảo hiểm y tế không?', 'Có, chúng tôi chấp nhận bảo hiểm y tế. Vui lòng mang theo thẻ BHYT khi đến khám.', 1, true, NOW(), NOW(), NULL),

('10000000-0000-0000-0000-000000000006'::uuid, 'Chung', 'Giờ làm việc của phòng khám?', 'Phòng khám làm việc từ thứ 2 đến thứ 7, từ 8:00 - 20:00. Chủ nhật và ngày lễ nghỉ.', 1, true, NOW(), NOW(), NULL),

('10000000-0000-0000-0000-000000000007'::uuid, 'Chung', 'Tôi có cần mang theo gì khi đến khám?', 'Vui lòng mang theo CMND/CCCD, thẻ BHYT (nếu có), kết quả xét nghiệm/chẩn đoán cũ (nếu có).', 2, true, NOW(), NOW(), NULL);
```

## 26. Doctor Performance Table

```sql
INSERT INTO doctor_performance (id, doctor_id, month, year, total_appointments, completed_appointments, cancelled_appointments, total_patients, average_rating, total_revenue, created_at, updated_at, deleted_at)
VALUES
('O0000000-0000-0000-0000-000000000001'::uuid, '20000000-0000-0000-0000-000000000001'::uuid, 2, 2026, 45, 42, 3, 38, 4.80, 12600000, NOW(), NOW(), NULL),

('O0000000-0000-0000-0000-000000000002'::uuid, '20000000-0000-0000-0000-000000000002'::uuid, 2, 2026, 58, 55, 3, 52, 4.90, 13750000, NOW(), NOW(), NULL),

('O0000000-0000-0000-0000-000000000003'::uuid, '20000000-0000-0000-0000-000000000003'::uuid, 2, 2026, 38, 36, 2, 35, 4.95, 14400000, NOW(), NOW(), NULL),

('O0000000-0000-0000-0000-000000000004'::uuid, '20000000-0000-0000-0000-000000000004'::uuid, 2, 2026, 42, 39, 3, 36, 4.70, 7800000, NOW(), NOW(), NULL),

('O0000000-0000-0000-0000-000000000005'::uuid, '20000000-0000-0000-0000-000000000005'::uuid, 2, 2026, 35, 33, 2, 31, 4.75, 9240000, NOW(), NOW(), NULL);
```

## 27. Daily Reports Table

```sql
INSERT INTO daily_reports (id, report_date, total_appointments, completed_appointments, cancelled_appointments, new_patients, returning_patients, total_revenue, cash_revenue, online_revenue, insurance_revenue, pending_payments, created_at, updated_at, deleted_at)
VALUES
('P0000000-0000-0000-0000-000000000001'::uuid, '2026-02-27', 15, 13, 2, 3, 10, 4500000, 2000000, 2200000, 300000, 0, NOW() - INTERVAL '3 days', NOW(), NULL),

('P0000000-0000-0000-0000-000000000002'::uuid, '2026-02-28', 18, 16, 2, 5, 11, 5200000, 1800000, 2800000, 600000, 0, NOW() - INTERVAL '2 days', NOW(), NULL),

('P0000000-0000-0000-0000-000000000003'::uuid, '2026-03-01', 12, 10, 2, 2, 8, 3800000, 1500000, 1900000, 400000, 0, NOW() - INTERVAL '1 day', NOW(), NULL);
```

## 28. Revenue Reports Table

```sql
INSERT INTO revenue_reports (id, report_type, report_date, start_date, end_date, total_revenue, service_revenue, medication_revenue, lab_test_revenue, total_appointments, total_patients, created_at, updated_at, deleted_at)
VALUES
('Q0000000-0000-0000-0000-000000000001'::uuid, 'weekly', '2026-03-01', '2026-02-24', '2026-03-01', 28500000, 18000000, 8500000, 2000000, 95, 78, NOW(), NOW(), NULL),

('Q0000000-0000-0000-0000-000000000002'::uuid, 'monthly', '2026-03-01', '2026-02-01', '2026-02-28', 115000000, 72000000, 35000000, 8000000, 380, 285, NOW(), NOW(), NULL),

('Q0000000-0000-0000-0000-000000000003'::uuid, 'monthly', '2026-02-01', '2026-01-01', '2026-01-31', 108000000, 68000000, 32000000, 8000000, 365, 270, NOW(), NOW(), NULL);
```

## 29. Password Reset Tokens Table

```sql
-- Không insert seed data cho bảng này vì đây là dữ liệu tạm thời
-- Được tạo khi user yêu cầu reset password
```

---

## Lưu ý khi chạy seed data:

1. **Thứ tự insert**: Phải insert theo đúng thứ tự để đảm bảo foreign key constraints:

   - roles → users → specialties
   - doctors, patients, staff (cần users và specialties)
   - services (cần specialties)
   - medications
   - appointments (cần patients, doctors, services)
   - medical_records (cần appointments)
   - prescriptions (cần medical_records)
   - prescription_items (cần prescriptions, medications)
   - invoices (cần appointments, patients)
   - invoice_items (cần invoices)
   - payments (cần invoices, patients)
   - reviews (cần patients, doctors, appointments)
   - Các bảng còn lại có thể insert theo thứ tự bất kỳ

2. **Password hash**: Tất cả password đã được hash với BCrypt. Password gốc là: `Password123!`

3. **UUID**: Sử dụng UUID cố định để dễ dàng tham chiếu giữa các bảng

4. **Timestamps**: Sử dụng NOW() và INTERVAL để tạo dữ liệu có thời gian hợp lý

5. **JSON fields**: Một số bảng có trường JSON (như vital_signs, applicable_services) cần PostgreSQL hỗ trợ kiểu JSON

6. **Soft delete**: Tất cả bảng có trường deleted_at được set NULL (chưa xóa)

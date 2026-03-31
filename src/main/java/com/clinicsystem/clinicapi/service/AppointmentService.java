package com.clinicsystem.clinicapi.service;

import org.springframework.stereotype.Service;

import com.clinicsystem.clinicapi.dto.CreateAppointmentRequest;
import com.clinicsystem.clinicapi.dto.UpdateAppointmentRequest;
import com.clinicsystem.clinicapi.model.Appointment;
import com.clinicsystem.clinicapi.model.Doctor;
import com.clinicsystem.clinicapi.model.Patient;
import com.clinicsystem.clinicapi.model.Appointment.AppointmentStatus;
import com.clinicsystem.clinicapi.repository.AppointmentRepository;
import com.clinicsystem.clinicapi.repository.DoctorRepository;
import com.clinicsystem.clinicapi.repository.PatientRepository;
import com.clinicsystem.clinicapi.repository.ServiceRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentService {
        private final AppointmentRepository appointmentRepository;
        private final PatientRepository patientRepository;
        private final DoctorRepository doctorRepository;
        private final ServiceRepository serviceRepository;

        @Transactional(rollbackFor = Exception.class)
        public Appointment createAppointment(CreateAppointmentRequest createAppointmentRequest) {
                Appointment appointment = new Appointment();
                Patient patient = patientRepository.findById(createAppointmentRequest.getPatientId())
                                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
                Doctor doctor = doctorRepository.findById(createAppointmentRequest.getDoctorId())
                                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
                com.clinicsystem.clinicapi.model.Service clinicService = serviceRepository
                                .findById(createAppointmentRequest.getServiceId())
                                .orElseThrow(() -> new IllegalArgumentException("Clinic service not found"));

                appointment.setPatient(patient);
                appointment.setDoctor(doctor);
                appointment.setService(clinicService);
                appointment.setAppointmentDate(createAppointmentRequest.getAppointmentDate());
                appointment.setStartTime(createAppointmentRequest.getStartTime());
                appointment.setEndTime(createAppointmentRequest.getEndTime());
                appointment.setBookingType(createAppointmentRequest.getBookingType());
                appointment.setReason(createAppointmentRequest.getReason());
                appointment.setSymptoms(createAppointmentRequest.getSymptoms());
                appointment.setNotes(createAppointmentRequest.getNotes());
                appointment.setQueueNumber(createAppointmentRequest.getQueueNumber());
                appointment.setStatus(AppointmentStatus.pending);
                appointment.setAppointmentCode(generateUniqueAppointmentCode());

                return appointmentRepository.save(appointment);
        }

        @Transactional(rollbackFor = Exception.class)
        public Appointment updateAppointment(UpdateAppointmentRequest updateAppointmentRequest) {
                Appointment appointment = appointmentRepository.findById(updateAppointmentRequest.getAppointmentId())
                                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

                if (updateAppointmentRequest.getStatus() != null)
                        appointment.setStatus(updateAppointmentRequest.getStatus());

                return appointmentRepository.save(appointment);
        }

        private String generateUniqueAppointmentCode() {
                String code = "APT-" + System.currentTimeMillis();
                return code;
        }

}

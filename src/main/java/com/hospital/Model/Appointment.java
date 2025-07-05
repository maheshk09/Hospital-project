package com.hospital.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patientName;
    private String email;
    private String phone;
    private LocalDate appointmentDate;

    private String reason;

    // ✅ Optional: store doctor name separately
    private String doctorName;

    // ✅ Relationship to Doctor
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    // Constructors
    public Appointment() {}

    public Appointment(Long id, String patientName, String email, String phone,
                       LocalDate appointmentDate, String doctorName, String reason) {
        this.id = id;
        this.patientName = patientName;
        this.email = email;
        this.phone = phone;
        this.appointmentDate = appointmentDate;
        this.doctorName = doctorName;
        this.reason = reason;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    // toString
    @Override
    public String toString() {
        return "Appointment [id=" + id + ", patientName=" + patientName + ", email=" + email +
               ", phone=" + phone + ", appointmentDate=" + appointmentDate +
               ", doctorName=" + doctorName + ", reason=" + reason + "]";
    }
}

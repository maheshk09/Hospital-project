package com.hospital.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.Model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	 List<Appointment> findByDoctorName(String doctorName);
}
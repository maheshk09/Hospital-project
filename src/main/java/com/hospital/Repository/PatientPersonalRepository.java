package com.hospital.Repository;

import com.hospital.Model.PatientPersonal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientPersonalRepository extends JpaRepository<PatientPersonal, Long> {
	
	List<PatientPersonal> findByDoctor_DoctorId(Long doctorId);
	PatientPersonal findByEmail(String email);

	

}

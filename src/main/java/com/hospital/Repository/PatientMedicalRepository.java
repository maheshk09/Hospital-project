package com.hospital.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.Model.PatientMedical;

import org.springframework.stereotype.Repository;

@Repository
public interface PatientMedicalRepository extends JpaRepository<PatientMedical, Long> {

    // ✅ Find medical record by patient ID
    PatientMedical findByPatient_PatientId(Long patientId);

    // ✅ Delete medical records by patient ID
    @Modifying
    @Transactional
    @Query("DELETE FROM PatientMedical pm WHERE pm.patient.patientId = :patientId")
    void deleteByPatientId(Long patientId);
}


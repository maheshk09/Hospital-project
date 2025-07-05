package com.hospital.Repository;

import com.hospital.Model.PatientLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientLoginRepository extends JpaRepository<PatientLogin, Long> {
    PatientLogin findByEmail(String email);
}

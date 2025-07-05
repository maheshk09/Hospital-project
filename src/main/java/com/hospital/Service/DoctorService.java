package com.hospital.Service;

import com.hospital.Model.Doctor;
import com.hospital.Repository.DoctorRepository;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;  // ✅ Removed @Lazy

    public DoctorService(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Doctor registerDoctor(Doctor doctor) {
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));  // ✅ Encrypt password before saving
        return doctorRepository.save(doctor);
    }

    public Doctor findByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }
    
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();  // assuming DoctorRepository extends JpaRepository
    }

    
    
}



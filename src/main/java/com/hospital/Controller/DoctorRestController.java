package com.hospital.Controller;

import com.hospital.Model.Doctor;
import com.hospital.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DoctorRestController {

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("/doctor-available-days/{id}")
    public ResponseEntity<?> getAvailableDays(@PathVariable Long id) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("availableDays", doctor.getAvailableDays()));
    }
}
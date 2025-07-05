package com.hospital.Service;

import com.hospital.Model.PatientPersonal;
import com.hospital.Model.Doctor;
import com.hospital.Model.PatientLogin;
import com.hospital.Model.PatientMedical;
import com.hospital.Repository.PatientPersonalRepository;
import com.hospital.Repository.PatientLoginRepository;
import com.hospital.Repository.PatientMedicalRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientService {

    private final PatientPersonalRepository patientPersonalRepository;
    private final PatientMedicalRepository patientMedicalRepository;
    private final PatientLoginRepository patientLoginRepository;

    public PatientService(PatientPersonalRepository patientPersonalRepository,
                          PatientMedicalRepository patientMedicalRepository,
                          PatientLoginRepository patientLoginRepository) {
        this.patientPersonalRepository = patientPersonalRepository;
        this.patientMedicalRepository = patientMedicalRepository;
        this.patientLoginRepository = patientLoginRepository;
    }

    // ✅ Self-Registration: Save only login credentials
    public void registerPatient(PatientLogin patientLogin) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(patientLogin.getPassword());
        patientLogin.setPassword(encodedPassword);
        patientLoginRepository.save(patientLogin);
    }

    // ✅ Login authentication
    public boolean authenticatePatient(String email, String rawPassword) {
        PatientLogin patientLogin = patientLoginRepository.findByEmail(email);
        if (patientLogin != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            return passwordEncoder.matches(rawPassword, patientLogin.getPassword());
        }
        return false;
    }

    // ✅ Used by Controller to match patient email with personal info
    public PatientLogin findPatientByEmail(String email) {
        return patientLoginRepository.findByEmail(email);
    }

    public PatientPersonal findPatientPersonalByEmail(String email) {
        return patientPersonalRepository.findByEmail(email);
    }
 // ✅ Fetch patient dashboard info using email
    public PatientPersonal getPatientPersonalDetailsByEmail(String email) {
        return patientPersonalRepository.findByEmail(email);
    }

    public PatientMedical getPatientMedicalDetailsByPatientId(Long patientId) {
        return patientMedicalRepository.findByPatient_PatientId(patientId);
    }

    // ✅ Doctor adds full patient record
    @Transactional
    public void addPatient(PatientPersonal patientPersonal, PatientMedical patientMedical, Doctor doctor) {
        patientPersonal.setDoctor(doctor);
        PatientPersonal savedPatient = patientPersonalRepository.save(patientPersonal);
        patientMedical.setPatient(savedPatient);
        patientMedicalRepository.save(patientMedical);
    }

    // ✅ Get patients by doctor
    public List<PatientPersonal> getPatientsByDoctor(Long doctorId) {
        return patientPersonalRepository.findByDoctor_DoctorId(doctorId);
    }

    // ✅ Get all patients (optional)
    public List<PatientPersonal> getAllPatients() {
        return patientPersonalRepository.findAll();
    }

    // ✅ Get single patient personal info
    public PatientPersonal getPatientById(Long id) {
        return patientPersonalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    // ✅ Get single patient's medical info
    public PatientMedical getPatientMedicalByPatientId(Long patientId) {
        return patientMedicalRepository.findByPatient_PatientId(patientId);
    }

    // ✅ Assign ICD-10
    @Transactional
    public void assignIcd10Code(Long patientId, String icd10Code, String diagnosis) {
        PatientMedical patientMedical = patientMedicalRepository.findByPatient_PatientId(patientId);
        if (patientMedical == null) {
            throw new RuntimeException("Medical record not found for patient ID: " + patientId);
        }
        patientMedical.setIcd10Code(icd10Code);
        patientMedical.setDiagnosis(diagnosis);
        patientMedicalRepository.save(patientMedical);
    }

    // ✅ Update patient (doctor action)
    public void updatePatient(Long id, PatientPersonal updatedPatient, PatientMedical updatedMedical) {
        PatientPersonal existingPatient = patientPersonalRepository.findById(id).orElse(null);
        PatientMedical existingMedical = patientMedicalRepository.findByPatient_PatientId(id);

        if (existingPatient != null) {
            existingPatient.setName(updatedPatient.getName());
            existingPatient.setAge(updatedPatient.getAge());
            existingPatient.setGender(updatedPatient.getGender());
            existingPatient.setContact(updatedPatient.getContact());
            existingPatient.setAddress(updatedPatient.getAddress());
            existingPatient.setEmergencyContact(updatedPatient.getEmergencyContact());
            existingPatient.setBloodGroup(updatedPatient.getBloodGroup());
            existingPatient.setAllergies(updatedPatient.getAllergies());
            patientPersonalRepository.save(existingPatient);
        }

        if (existingMedical == null) {
            existingMedical = new PatientMedical();
            existingMedical.setPatient(existingPatient);
        }

        existingMedical.setBloodPressure(updatedMedical.getBloodPressure());
        existingMedical.setSugarLevel(updatedMedical.getSugarLevel());
        existingMedical.setHemoglobin(updatedMedical.getHemoglobin());
        existingMedical.setCholesterol(updatedMedical.getCholesterol());
        existingMedical.setHeartRate(updatedMedical.getHeartRate());
        existingMedical.setOxygenLevel(updatedMedical.getOxygenLevel());
        existingMedical.setMedications(updatedMedical.getMedications());

        patientMedicalRepository.save(existingMedical);
    }

    // ✅ Delete patient (doctor action)
    public void deletePatient(Long id) {
        patientMedicalRepository.deleteByPatientId(id);
        patientPersonalRepository.deleteById(id);
    }
}

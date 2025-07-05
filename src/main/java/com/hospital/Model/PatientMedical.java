package com.hospital.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "patient_medical")
public class PatientMedical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicalId;

    @OneToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "patientId")
    private PatientPersonal patient;

    private String diagnosis;
    private String icd10Code;
    private String bloodPressure;
    private String sugarLevel;
    private String hemoglobin;
    private String cholesterol;
    private String heartRate;
    private String oxygenLevel;
    private String medications;
    private String notes;

    // Constructors
    public PatientMedical() {}

    public PatientMedical(PatientPersonal patient, String diagnosis, String icdCode, String bloodPressure, String sugarLevel, String hemoglobin, String cholesterol, String heartRate, String oxygenLevel, String medications, String notes) {
        this.patient = patient;
        this.diagnosis = diagnosis;
        this.icd10Code = icdCode;
        this.bloodPressure = bloodPressure;
        this.sugarLevel = sugarLevel;
        this.hemoglobin = hemoglobin;
        this.cholesterol = cholesterol;
        this.heartRate = heartRate;
        this.oxygenLevel = oxygenLevel;
        this.medications = medications;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(Long medicalId) {
        this.medicalId = medicalId;
    }

    public PatientPersonal getPatient() {
        return patient;
    }

    public void setPatient(PatientPersonal patient) {
        this.patient = patient;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getIcd10Code() {  // ✅ Ensure getter matches field name
        return icd10Code;
    }

    public void setIcd10Code(String icd10Code) {  // ✅ Proper setter
        this.icd10Code = icd10Code;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getSugarLevel() {
        return sugarLevel;
    }

    public void setSugarLevel(String sugarLevel) {
        this.sugarLevel = sugarLevel;
    }

    public String getHemoglobin() {
        return hemoglobin;
    }

    public void setHemoglobin(String hemoglobin) {
        this.hemoglobin = hemoglobin;
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getOxygenLevel() {
        return oxygenLevel;
    }

    public void setOxygenLevel(String oxygenLevel) {
        this.oxygenLevel = oxygenLevel;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

	
}
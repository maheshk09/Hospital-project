package com.hospital.Controller;

import com.hospital.Service.Icd10Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class Icd10Controller {

    private final Icd10Service icd10Service;

    public Icd10Controller(Icd10Service icd10Service) {
        this.icd10Service = icd10Service;
    }

    /**
     * API endpoint to fetch a single ICD-10 code for a given diagnosis.
     * @param diagnosis The diagnosis entered by the doctor.
     * @return JSON response containing the ICD-10 code.
     */
    @GetMapping("/icd10/getCode")
    public Map<String, String> getICDCode(@RequestParam String diagnosis) {
        String icd10Code = icd10Service.getIcd10Code(diagnosis);
        return Map.of("icd10Code", icd10Code != null ? icd10Code : "Not Found");
    }

    /**
     * API endpoint to search multiple ICD-10 codes for a given disease.
     * @param query The disease name entered by the user.
     * @return A list of ICD-10 codes and descriptions.
     */
    @GetMapping("/icd10/search")
    public List<Map<String, String>> searchICD10(@RequestParam String query) {
        return icd10Service.searchICD10(query);
    }
}

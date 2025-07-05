package com.hospital.Controller;

import com.hospital.Service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DiagnosisController {

    private final PatientService patientService;

    public DiagnosisController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/assign-icd10")
    public String showAssignICD10Page(@RequestParam("patientId") Long patientId, Model model) {
        model.addAttribute("patientId", patientId);
        return "assign-icd10";
    }

    @PostMapping("/assign-icd10")
    public String assignICD10(@RequestParam("patientId") Long patientId,
                              @RequestParam("diagnosis") String diagnosis,
                              @RequestParam("icd10Code") String icd10Code) {
        patientService.assignIcd10Code(patientId, diagnosis, icd10Code);
        return "redirect:/patient/details/" + patientId;  // ✅ Correct redirect
    }

}


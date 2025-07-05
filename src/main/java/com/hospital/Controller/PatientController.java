package com.hospital.Controller;

import com.hospital.Model.PatientPersonal;
import com.hospital.Model.PatientLogin;
import com.hospital.Model.PatientMedical;
import com.hospital.Model.Doctor;
import com.hospital.Service.PatientService;

import jakarta.servlet.http.HttpSession;

import com.hospital.Service.DoctorService;
import com.hospital.Service.Icd10Service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.HashMap;


@Controller
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final Icd10Service icd10Service;
    private final DoctorService doctorService;

    public PatientController(PatientService patientService, Icd10Service icd10Service, DoctorService doctorService) {
        this.patientService = patientService;
        this.icd10Service = icd10Service;
        this.doctorService = doctorService;
    }

    // ✅ Patient Registration (Self)
    @GetMapping("/self-register")
    public String showRegisterForm(Model model) {
        model.addAttribute("patientLogin", new PatientLogin());
        return "patient-register";
    }

    @PostMapping("/self-register")
    public String registerPatient(@ModelAttribute PatientLogin patientLogin, Model model) {
        try {
            patientService.registerPatient(patientLogin); // Save only email/password
            return "redirect:/patient/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error registering patient: " + e.getMessage());
            return "error-page";
        }
    }

    // ✅ Patient Login
    @GetMapping("/login")
    public String showLoginForm() {
        return "patient-login";
    }

    @PostMapping("/login")
    public String loginPatient(@RequestParam String email,
                               @RequestParam String password,
                               Model model,
                               HttpSession session) {

        boolean isAuthenticated = patientService.authenticatePatient(email, password);
        if (isAuthenticated) {
            // Fetch patient personal details
            PatientPersonal patient = patientService.findPatientPersonalByEmail(email);
            if (patient != null) {
                // Store in session
                session.setAttribute("loggedInPatient", patient);
                session.setAttribute("patientEmail", email);

                // Redirect to dashboard (do not return dashboard directly)
                return "redirect:/patient/dashboard";
            } else {
                model.addAttribute("errorMessage", "Your details have not been added by the doctor yet.");
                return "error";
            }
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "patient-login";
        }
    }

    @GetMapping("/dashboard")
    public String showPatientDashboard(HttpSession session, Model model) {
        PatientPersonal loggedInPatient = (PatientPersonal) session.getAttribute("loggedInPatient");

        if (loggedInPatient == null) {
            return "redirect:/patient/login";
        }

        // Get medical info using patientId
        PatientMedical patientMedical = patientService.getPatientMedicalByPatientId(loggedInPatient.getPatientId());

        model.addAttribute("patientPersonal", loggedInPatient);
        model.addAttribute("patientMedical", patientMedical);

        return "patient-dashboard";
    }

    @GetMapping("/logout")
    public String logoutPatient(HttpSession session) {
        session.invalidate();  // Clears the session data
        return "redirect:/patient/login?logout";  // Redirect to login with optional message
    }


    // ✅ Other methods for doctor to add/list patients

    @GetMapping("/add")
    public String showAddPatientForm(Model model) {
        model.addAttribute("patientPersonal", new PatientPersonal());
        model.addAttribute("patientMedical", new PatientMedical());
        return "add-patient";
    }

    @PostMapping("/add")
    public String addPatient(@ModelAttribute PatientPersonal patientPersonal,
                             @ModelAttribute PatientMedical patientMedical,
                             @SessionAttribute("loggedInDoctor") Doctor doctor, // Correctly use @SessionAttribute
                             Model model) {
        try {
            patientService.addPatient(patientPersonal, patientMedical, doctor);
            return "redirect:/patient/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error saving patient. " + e.getMessage());
            return "error-page";
        }
    }


    @GetMapping("/list")
    public String showPatientList(@SessionAttribute("loggedInDoctor") Doctor doctor, Model model) {
        List<PatientPersonal> patients = patientService.getPatientsByDoctor(doctor.getDoctorId());
        model.addAttribute("patients", patients);
        return "patient-list";
    }

    // ✅ ICD-10 Features
    @GetMapping("/search-icd10")
    public String searchICD10(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Map<String, String>> results = query != null ? icd10Service.searchICD10(query) : List.of();
        model.addAttribute("results", results);
        return "icd10-search";
    }

    @PostMapping("/assign-icd10")
    public String assignIcd10Code(@RequestParam Long patientId,
                                  @RequestParam String icd10Code,
                                  @RequestParam String diagnosis) {
        patientService.assignIcd10Code(patientId, icd10Code, diagnosis);
        return "redirect:/patient/details/" + patientId;
    }

    @GetMapping("/get-icd10")
    @ResponseBody
    public Map<String, String> getIcd10Code(@RequestParam String diagnosis) {
        String icd10Code = icd10Service.getIcd10Code(diagnosis);
        return Map.of("icd10Code", icd10Code != null ? icd10Code : "Not Found");
    }

    // ✅ Dashboard, Details, Edit, Delete - No change
    @GetMapping("/details/{id}")
    public String showPatientDetails(@PathVariable Long id, Model model) {
        PatientPersonal patient = patientService.getPatientById(id);
        PatientMedical patientMedical = patientService.getPatientMedicalByPatientId(id);

        if (patient == null) {
            model.addAttribute("errorMessage", "Patient not found.");
            return "error";
        }

        model.addAttribute("patientPersonal", patient);
        model.addAttribute("patientMedical", patientMedical);
        return "patient-details";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        PatientPersonal patient = patientService.getPatientById(id);
        PatientMedical patientMedical = patientService.getPatientMedicalByPatientId(id);
        if (patientMedical == null) patientMedical = new PatientMedical();

        model.addAttribute("patient", patient);
        model.addAttribute("patientMedical", patientMedical);
        return "update-patient";
    }

    @PostMapping("/update/{id}")
    public String updatePatient(@PathVariable Long id,
                                @ModelAttribute PatientPersonal patient,
                                @ModelAttribute PatientMedical patientMedical) {
        patientService.updatePatient(id, patient, patientMedical);
        return "redirect:/doctor/dashboard";
    }

    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            patientService.deletePatient(id);
            redirectAttributes.addFlashAttribute("successMessage", "Patient deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting patient.");
        }
        return "redirect:/doctor/dashboard";
    }
}

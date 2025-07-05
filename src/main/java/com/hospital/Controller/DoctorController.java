package com.hospital.Controller;

import com.hospital.Model.Appointment;
import com.hospital.Model.Doctor;
import com.hospital.Model.PatientPersonal;
import com.hospital.Repository.AppointmentRepository;
import com.hospital.Repository.DoctorRepository;
import com.hospital.Service.DoctorService;
import com.hospital.Service.PatientService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/doctor")
@SessionAttributes("loggedInDoctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public DoctorController(DoctorService doctorService, PatientService patientService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "register";
    }

    @PostMapping("/register-doctor")
    public String registerDoctor(@ModelAttribute("doctor") Doctor doctor,
                                 @RequestParam List<String> availableDays) {
        doctor.setAvailableDays(availableDays);
        doctorRepository.save(doctor);
        return "redirect:/doctor/login";
    }

    @PostMapping("/register")
    public String registerDoctor(@ModelAttribute Doctor doctor) {
        doctorService.registerDoctor(doctor);
        return "redirect:/doctor/login";
    }

    @GetMapping("/dashboard")
    public String showDashboard(@AuthenticationPrincipal UserDetails loggedInDoctor, Model model) {
        if (loggedInDoctor == null) {
            return "redirect:/doctor/login";
        }

        Doctor doctor = doctorService.findByEmail(loggedInDoctor.getUsername());
        model.addAttribute("loggedInDoctor", doctor);

        List<PatientPersonal> patients = patientService.getPatientsByDoctor(doctor.getDoctorId());
        List<Appointment> appointments = appointmentRepository.findByDoctorName(doctor.getName());

        model.addAttribute("doctorName", doctor.getName());
        model.addAttribute("patients", patients);
        model.addAttribute("appointments", appointments);

        return "doctor-dashboard";
    }

   
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // ✅ API endpoint used in JS to get available days
    @CrossOrigin // optional but helpful if CORS becomes an issue
    @GetMapping("/api/doctor-available-days/{id}")
    @ResponseBody
    public Map<String, List<String>> getDoctorAvailableDays(@PathVariable Long id) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if (doctor != null) {
            return Map.of("availableDays", doctor.getAvailableDays());
        } else {
            return Map.of("availableDays", List.of());
        }
   
    }
    
    
    
    @PostMapping("/appointments/save")
    public String saveAppointment(@ModelAttribute("appointment") Appointment appointment,
                                  RedirectAttributes redirectAttributes) {
        appointmentRepository.save(appointment);
        redirectAttributes.addFlashAttribute("successMsg", "✅ Appointment booked successfully!");
        return "redirect:/appointments/book";
    }

}

package com.hospital.Controller;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hospital.Model.Appointment;
import com.hospital.Model.Doctor;
import com.hospital.Repository.AppointmentRepository;
import com.hospital.Repository.DoctorRepository;
import com.hospital.Service.DoctorService;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DoctorRepository doctorRepository;

    // ✅ Show form for booking appointment (alternate path)
    @GetMapping("/book")
    public String showAppointmentForm(Model model) {
        List<Doctor> doctors = doctorRepository.findAll();
        model.addAttribute("doctors", doctors);
        model.addAttribute("appointment", new Appointment());
        return "book-appointment";  // make sure this matches your Thymeleaf view name
    }

    // ✅ Show form for creating a new appointment (original path)
    @GetMapping("/save")
    public String showBookAppointmentForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "appointment_form";  // existing view
    }

    // ✅ Handle form submission with doctor availability validation
    @PostMapping("/save")
    public String bookAppointment(@ModelAttribute("appointment") Appointment appointment, Model model) {
        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getDoctorId()).orElse(null);

        if (doctor != null && appointment.getAppointmentDate() != null) {
            String selectedDay = appointment.getAppointmentDate()
                                             .getDayOfWeek()
                                             .getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            if (!doctor.getAvailableDays().contains(selectedDay)) {
                model.addAttribute("errorMessage", "Doctor is not available on " + selectedDay);
                model.addAttribute("appointment", appointment);
                model.addAttribute("doctors", doctorService.getAllDoctors());
                return "appointment_form";
            }
        }

        appointmentRepository.save(appointment);
        return "redirect:/patient/dashboard";
    }
}

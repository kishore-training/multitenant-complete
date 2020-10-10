package com.udemy.multitenancy.service;


import com.udemy.multitenancy.model.Appointment;
import com.udemy.multitenancy.vo.AppointmentRequestVO;
import com.udemy.multitenancy.vo.DoctorUserRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@EnableSwagger2
@Controller
public class AppointmentController {


    @Autowired
    AppointmentControllerService appointmentControllerService;




    @PostMapping(value = "/appointment")
    public String createAppointment(@CookieValue(value = "HOSPITAL")  String hospitalName,@RequestParam String doctor, @ModelAttribute AppointmentRequestVO appointmentRequestVO, BindingResult bindingResult, Model model) {
        List<Appointment> appointmentList = null;
        ResponseEntity<List<Appointment>> resp = null;

        try {
            appointmentRequestVO.setHospitalName(hospitalName);
            appointmentRequestVO.setDoctorName(doctor);
            appointmentList = appointmentControllerService.createAppointmentDetails(appointmentRequestVO);

        }  catch (Exception e) {
            model.addAttribute("message","Exception while creeating appointment"+ e.getMessage() );
            return "failureMessage";
        }



        return "redirect:/appointment?doctor="+appointmentRequestVO.getDoctorName();
    }



    @DeleteMapping(value = "/appointment")
    public ResponseEntity<String> deleteAppointment(@RequestParam  String id, Model model) {

        appointmentControllerService.deleteAppointment(id);

        ResponseEntity<String> resp = new ResponseEntity<>("Appointment Deleted", HttpStatus.CREATED);
        return resp;
    }


    @GetMapping(value = "/appointment")
    public String listCurrentAppointmentsForStaff(@CookieValue(value = "HOSPITAL")  String hospitalName, @RequestParam String doctor, Model model) throws Exception {

        Date appointmentDate = new Date();
        List<Appointment> appointments = appointmentControllerService.getAppointmentsForDateAndStaff(appointmentDate, doctor);
        String user = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        model.addAttribute("doctor", doctor);
        model.addAttribute("hospitalName", hospitalName);
        model.addAttribute("appointments",appointments);
        model.addAttribute("user",user);
        return "listAppointments";
    }


    @GetMapping("/appointment/addAppointment")
    public String showAddAppointmentForm( @CookieValue(value = "HOSPITAL")  String hospitalName,@RequestParam String doctor,Model model) {
        String user = null;
        try {
            user = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        }catch (Exception e){
            model.addAttribute("message", "Hospital not found"+ e.getMessage());
            return "failureMessage";
        }
        model.addAttribute("appointmentRequestVO", new AppointmentRequestVO());
        model.addAttribute("hospitalName", hospitalName);
        model.addAttribute("doctor", doctor);
        model.addAttribute("user", user);
        return "addAppointment";
    }


    @GetMapping(value = "/appointment/date/{date}", produces = "application/json")
    public String listAppointmentsForStaffAndDate(@CookieValue(value = "HOSPITAL")  String hospitalName, @RequestParam String doctor,@PathVariable String date, Model model) throws Exception {
        String user = null;
        List<Appointment> appointments = null;
        try {
            user = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            SimpleDateFormat dateFormat= new SimpleDateFormat("dd.MM.yyyy");
            Date appointmentDate = dateFormat.parse(date);
            appointments = appointmentControllerService.getAppointmentsForDateAndStaff(appointmentDate, doctor);
        }catch (Exception e){
            model.addAttribute("message", "Hospital not found"+ e.getMessage());
            return "failureMessage";
        }

        model.addAttribute("hospitalName", hospitalName);
        model.addAttribute("appointments",appointments);
        model.addAttribute("doctor", doctor);
        model.addAttribute("user", user);
        return "listAppointments";
    }



}

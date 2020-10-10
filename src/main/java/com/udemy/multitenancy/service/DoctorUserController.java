package com.udemy.multitenancy.service;

import com.udemy.multitenancy.model.DoctorUser;
import com.udemy.multitenancy.model.Hospital;
import com.udemy.multitenancy.utils.HospitalNotFoundException;
import com.udemy.multitenancy.utils.UserExistsException;
import com.udemy.multitenancy.vo.DoctorUserRequestVO;
import com.udemy.multitenancy.vo.HospitalRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.logging.Logger;


@EnableSwagger2
@Controller
public class DoctorUserController {

    public Logger logger = Logger.getLogger("DoctorUserController");
    @Autowired
    DoctorUserControllerService doctorUserControllerService;


    @GetMapping(value = "/doctor")
    public String listDoctors(@CookieValue(value = "HOSPITAL") String hospitalName,  Model model) {
        System.out.println("HOSPITAL :"+hospitalName);
        List<DoctorUser> doctorUsers = null;
        String user= "";
        try {
            doctorUsers = doctorUserControllerService.listUsersForOrganization(hospitalName);
            user = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        } catch (Exception e) {
            model.addAttribute("message", "Hospital not found"+ e.getMessage());
            return "failureMessage";
        }
        model.addAttribute("hospitalName", hospitalName);
        model.addAttribute("doctorUsers",doctorUsers);
        model.addAttribute("user", user);
        return "listDoctorUsers";
    }

    @GetMapping("/doctor/addUser")
    public String showRegisterForm( @CookieValue(value = "HOSPITAL")  String hospitalName,Model model) {
        String user = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        model.addAttribute("userRequestVO", new DoctorUserRequestVO());
        model.addAttribute("hospitalName", hospitalName);
        model.addAttribute("user", user);
        return "addUser";
    }



    @PostMapping(value = "/doctor", produces = "application/json")
    public String createDoctor( @CookieValue(value = "HOSPITAL") String hospitalName, @ModelAttribute DoctorUserRequestVO doctorUserRequestVO,
                               BindingResult bindingResult, Model model) {
        logger.info("HOSPITAL :"+hospitalName);
        doctorUserRequestVO.setHospitalName(hospitalName);
        DoctorUser doctorUser = null;

        try {
            doctorUser = doctorUserControllerService.createDoctorUserDetails(doctorUserRequestVO);

        } catch (HospitalNotFoundException | UserExistsException e) {
            model.addAttribute("message", "Exception in Add Doctors"+ e.getMessage());
            return "failureMessage";
        }

        model.addAttribute("doctorUser",doctorUser);
        return "redirect:/doctor";
    }


    @DeleteMapping(value = "/doctor/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable String id) {

        doctorUserControllerService.deleteDoctorUser(id);

        ResponseEntity<String> resp = new ResponseEntity<>("Successfully Deleted", HttpStatus.CREATED);
        return resp;
    }



}

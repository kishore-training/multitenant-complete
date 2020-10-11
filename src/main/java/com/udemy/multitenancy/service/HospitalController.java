package com.udemy.multitenancy.service;

import com.udemy.multitenancy.model.Hospital;
import com.udemy.multitenancy.utils.HospitalNotFoundException;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@EnableSwagger2
@Controller
public class HospitalController {


    @Autowired
    HospitalControllerService hospitalControllerService;



    @GetMapping(value = "/hospital")
    public String listHospitals( Model model) {
        List<Hospital> hospitals = new ArrayList<>();
        hospitals = hospitalControllerService.listHospitals();
        String user = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        model.addAttribute("hospital", hospitals);
        model.addAttribute("user", user);
        return "listHospitals";
    }

    @GetMapping("/register")
    public String showRegisterForm( Model model) {
        model.addAttribute("hospitalRequestVO", new HospitalRequestVO());
        return "registerHospital";
    }

    @GetMapping("/home")
    public String showHomeForm( Model model) {
        model.addAttribute("hospitalRequestVO", new HospitalRequestVO());
        return "home";
    }
    @GetMapping("/login")
    public String showloginForm( Model model) {
        model.addAttribute("hospitalRequestVO", new HospitalRequestVO());
        return "login";
    }
    @PostMapping(value = "/hospital/register")
    public String createHospital(@ModelAttribute @Valid  HospitalRequestVO hospitalRequestVO, BindingResult result, Model model, HttpServletResponse response) {
        ResponseEntity<Hospital> resp = null;
        Hospital hospital= null;
        if (result.hasErrors()) {
            return "registerHospital";
        }
        try {
            hospital = hospitalControllerService.registerHospitalDetails(hospitalRequestVO);
        }catch(Exception e){
            model.addAttribute("message", "Failure in Register Hospital" + e.getMessage());
            return "failureMessage";
        }
        model.addAttribute("hospitalName", hospital.getHospitalName());

        return "registerSuccess";
    }


    @GetMapping(value = "/hospital/delete")
    public String deleteOrganization(@RequestParam String hospitalName, Model model) {
        List<Hospital> hospitals= null;
        try {
            hospitalControllerService.deleteHospital(hospitalName);
            hospitals = hospitalControllerService.listHospitals();
            String user = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            model.addAttribute("hospital", hospitals);
            model.addAttribute("user", user);
        } catch (HospitalNotFoundException e) {
            model.addAttribute("message", "Failure in Delete Hospital" + e.getMessage());
            return "failureMessage";
        }
        return "redirect:/hospital";

    }


}

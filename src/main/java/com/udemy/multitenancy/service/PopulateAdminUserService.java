package com.udemy.multitenancy.service;

import com.udemy.multitenancy.mapper.HospitalRequestMapper;
import com.udemy.multitenancy.model.DoctorUser;
import com.udemy.multitenancy.model.Hospital;
import com.udemy.multitenancy.model.types.UserTypeEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.logging.Logger;

@Component
public class PopulateAdminUserService implements InitializingBean {
    @Autowired
    BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    DoctorUserRepository doctorUserRepository;

    @Autowired
    HospitalRepository hospitalRepository;

    public Logger logger = Logger.getLogger("PopulateAdminUserService");

    @Override
    public void afterPropertiesSet() throws Exception {

        Hospital hospital = new Hospital();
        hospital.setHospitalName("ADMIN");
        hospital.setCountry("INDIA");
        hospital.setEmail("admin@gmail.com");
        hospital.setMobileNumber("98989898");
        hospital.setCreateDateTime(Instant.now());
        hospitalRepository.save(hospital);

        DoctorUser user = new DoctorUser();
        user.setUserName("SUPER_ADMIN");

        user.setUserTypeEnum(UserTypeEnum.SUPER_ADMIN);
        user.setMobileNumber("9898989898");
        user.setPassword(bcryptEncoder.encode("Admin123"));
        user.setEmailId("super_admin@gmail.com");
        user.setSpecialization("ADMIN");
        user.setCreateDateTime(Instant.now());
        user.setUpdateDateTime(Instant.now());
        user.setHospitalName("ADMIN");

        doctorUserRepository.save(user);
        logger.info("CREATED SUPER_ADMIN USER");




    }
}

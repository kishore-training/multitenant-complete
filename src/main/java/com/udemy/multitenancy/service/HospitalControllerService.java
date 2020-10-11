package com.udemy.multitenancy.service;

import com.udemy.multitenancy.mapper.HospitalRequestMapper;
import com.udemy.multitenancy.model.DoctorUser;
import com.udemy.multitenancy.model.Hospital;
import com.udemy.multitenancy.model.types.UserTypeEnum;
import com.udemy.multitenancy.utils.HospitalExistsException;
import com.udemy.multitenancy.utils.HospitalNotFoundException;
import com.udemy.multitenancy.vo.HospitalRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service(value = "organisationControllerService")
public class HospitalControllerService {

    public Logger logger = Logger.getLogger("HospitalControllerService");
    @Autowired
    HospitalRepository hospitalRepository;
    @Autowired
    DoctorUserRepository doctorUserRepository;

    @Autowired
    BCryptPasswordEncoder bcryptEncoder;


    public Hospital registerHospitalDetails(HospitalRequestVO requestVO) throws HospitalExistsException {
        String email = requestVO.getEmail();
        logger.info("Create Organization for email: " + email + " with logo: " + requestVO.getLogo());
        //logo is my actual check here for authentication
        List<Hospital> hospitals = hospitalRepository.findByHospitalName(requestVO.getHospitalName());
        DoctorUser user = null;
        Hospital hospital = null;
        if (hospitals == null || hospitals.isEmpty()) {//new organisation

            hospital = HospitalRequestMapper.INSTANCE.hospitalVOToModel(requestVO);
            user = HospitalRequestMapper.INSTANCE.hospitalVOToDoctorUser(requestVO);
            user.setUserTypeEnum(UserTypeEnum.HOSPITAL_TENANT_ADMIN);
            hospital.setCreateDateTime(Instant.now());
            user.setHospitalName(requestVO.getHospitalName());

            hospital.setUpdateDateTime(Instant.now());
            user.setPassword(bcryptEncoder.encode(requestVO.getPassword()));
            user.setEmailId(requestVO.getEmail());
            user.setSpecialization("ADMIN");
            user.setCreateDateTime(Instant.now());
            user.setUpdateDateTime(Instant.now());

        } else {
            throw new HospitalExistsException("Specified hospital name exists already. please enter different hospital name");
        }
        hospital = hospitalRepository.save(hospital);
        //save admin user
        user.setHospitalName(hospital.getHospitalName());

        user = doctorUserRepository.save(user);

        return hospital;
    }

    public void deleteHospital(String hospitalName) throws HospitalNotFoundException {
        List<Hospital> hospitals= hospitalRepository.findByHospitalName(hospitalName);
        if (hospitals == null || hospitals.isEmpty()) {
            throw new HospitalNotFoundException("Specified hospital not found");
        }
        hospitalRepository.deleteById(hospitals.get(0).getId());
    }

    public Hospital getHospital(String organizationId) {
        Hospital hospital = hospitalRepository.findById(organizationId).get();
        return (hospital);
    }

    public List<Hospital> listHospitals() {
        List<Hospital> hospitals = new ArrayList<>();
        hospitalRepository.findAll().forEach(a ->
                hospitals.add(a));
        return hospitals;
    }

}

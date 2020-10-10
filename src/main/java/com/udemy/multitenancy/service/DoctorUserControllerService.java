package com.udemy.multitenancy.service;

import com.udemy.multitenancy.mapper.DoctorUserRequestMapper;
import com.udemy.multitenancy.model.DoctorUser;
import com.udemy.multitenancy.model.Hospital;
import com.udemy.multitenancy.model.types.StaffStatusEnum;
import com.udemy.multitenancy.model.types.UserTypeEnum;
import com.udemy.multitenancy.utils.HospitalNotFoundException;
import com.udemy.multitenancy.utils.UserExistsException;
import com.udemy.multitenancy.vo.DoctorUserRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.time.Instant;
import java.util.List;

@Service(value = "staffControllerService")
public class DoctorUserControllerService {

    @Autowired
    DoctorUserRepository doctorUserRepository;

    @Autowired
    HospitalRepository hospitalRepository;

    @Autowired
    BCryptPasswordEncoder bcryptEncoder;
    @Autowired
    ApplicationEventPublisher eventPublisher;

    public DoctorUser createDoctorUserDetails(DoctorUserRequestVO doctorUserRequestVO) throws HospitalNotFoundException, UserExistsException {

        List<Hospital> hospitals = hospitalRepository.findByHospitalName(doctorUserRequestVO.getHospitalName());
        if (hospitals == null || hospitals.isEmpty()) {
            throw new HospitalNotFoundException("Specified hospital not found");
        }
        List<DoctorUser> doctorUsers = doctorUserRepository.findByUserName(doctorUserRequestVO.getUserName());
        if(doctorUsers!=null && !doctorUsers.isEmpty()){
            throw new UserExistsException("User exists already");
        }
        Hospital hospital = hospitals.get(0);
        DoctorUser doctorUser = DoctorUserRequestMapper.INSTANCE.organizationStaffVOToModel(doctorUserRequestVO);
        doctorUser.setCreateDateTime(Instant.now());
        doctorUser.setUpdateDateTime(Instant.now());
        doctorUser.setHospitalName(hospital.getHospitalName());
        doctorUser.setUserTypeEnum(UserTypeEnum.DOCTOR);
        doctorUser.setPassword(bcryptEncoder.encode(doctorUserRequestVO.getPassword()));
        doctorUser.setStatus(StaffStatusEnum.AVAILABLE);
        doctorUser = doctorUserRepository.save(doctorUser);
        return (doctorUser);
    }


    public DoctorUser getDoctorUser(String staffId) {
        DoctorUser doctorUser = doctorUserRepository.findById(staffId).get();

        return (doctorUser);

    }


    public void deleteDoctorUser(String userId) {
        doctorUserRepository.deleteById(userId);
    }


    public List<DoctorUser> listUsersForOrganization(String hospitalName) throws HospitalNotFoundException {
        List<Hospital> hospitals = hospitalRepository.findByHospitalName(hospitalName);
        if (hospitals == null || hospitals.isEmpty()) {
            throw new HospitalNotFoundException("Specified hospital not found");
        }
        Hospital hospital = hospitals.get(0);
        List<DoctorUser> doctorUsers = doctorUserRepository.findByHospitalName(hospitalName);

        return doctorUsers;

    }

}

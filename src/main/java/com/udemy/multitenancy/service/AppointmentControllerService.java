package com.udemy.multitenancy.service;

import com.udemy.multitenancy.mapper.AppointmentRequestMapper;
import com.udemy.multitenancy.model.Appointment;
import com.udemy.multitenancy.model.DoctorUser;
import com.udemy.multitenancy.model.Hospital;
import com.udemy.multitenancy.model.types.StatusEnum;
import com.udemy.multitenancy.utils.AppointmentExistsException;
import com.udemy.multitenancy.utils.DoctorNotFoundException;
import com.udemy.multitenancy.utils.HospitalNotFoundException;
import com.udemy.multitenancy.utils.Util;
import com.udemy.multitenancy.vo.AppointmentRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service(value = "appointmentControllerService")
public class AppointmentControllerService {

    public Logger logger = Logger.getLogger("AppointmentService");

    @Autowired
    DoctorUserRepository doctorUserRepository;

    @Autowired
    HospitalRepository organizationRepository;

    @Autowired
    AppointmentRepository appointmentRepository;
    private static final Integer APPOINTMENT_DURATION_MINUTES = 15;


    public List<Appointment> createAppointmentDetails(AppointmentRequestVO appointmentRequestVO) throws  DoctorNotFoundException, HospitalNotFoundException {

        List<Hospital> hospitals = organizationRepository.findByHospitalName(appointmentRequestVO.getHospitalName());

        if (hospitals == null || hospitals.isEmpty()) {
            throw new HospitalNotFoundException("Specified hospital not found");
        }
        Hospital hospital = hospitals.get(0);
        List<Appointment> appointmentVOList = new ArrayList<>();

        Appointment appModel = AppointmentRequestMapper.INSTANCE.appointmentVOToModel(appointmentRequestVO);
        logger.info("next appointment " + appointmentRequestVO.getAppointmentDateTime());

        List<DoctorUser> doctors = doctorUserRepository.findByUserName(appointmentRequestVO.getDoctorName());
        if (doctors == null || doctors.isEmpty()) {
            throw new DoctorNotFoundException("Specified doctor does not exist");
        }

        DoctorUser doctorUser = doctors.get(0);
        logger.info("Received create appointment request: " + appointmentRequestVO.getPatientMobileNumber() + "::" + appointmentRequestVO.getAppointmentDateTime()
                + " for organisation " + hospital.getHospitalName() + "::" + doctorUser.getUserName());

        appModel.setDoctorId(doctorUser.getId());
        appModel.setAppointmentDateTime(appointmentRequestVO.getAppointmentDateTime());
        appModel.setCreateDateTime(Instant.now());
        appModel.setUpdateDateTime(Instant.now());
        appModel.setStatus(StatusEnum.CREATED);
        appModel.setHospitalId(hospital.getId());


        appModel.setAppointmentEndDateTime(appointmentRequestVO.getAppointmentDateTime().plusMinutes(APPOINTMENT_DURATION_MINUTES));
        logger.info("Saving appointment model " + appModel);
        appModel = appointmentRepository.save(appModel);
        appointmentVOList.add(appModel);


        return appointmentVOList;
    }


    public Appointment getAppointment(String appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).get();

        return appointment;
    }


    public void deleteAppointment(String appointmentId) {
        appointmentRepository.deleteById(appointmentId);
    }

    List<Appointment> getAppointmentsForDateAndStaff(Date appointmentDateTime, String staffName) throws DoctorNotFoundException {
        logger.info("Staff getAppointmentsForDateAndStaff :" + appointmentDateTime + "," + staffName);
        appointmentDateTime = (Date) appointmentDateTime.clone();
        Date startDateTime = Util.getStartDate(appointmentDateTime);
        Date endDateTime = Util.getEndDate(appointmentDateTime);

        List<DoctorUser> doctors = doctorUserRepository.findByUserName(staffName);
        if (doctors == null || doctors.isEmpty()) {
            throw new DoctorNotFoundException("Specified doctor does not exist");
        }

        DoctorUser doctorUser = doctors.get(0);
        String doctorUserId = doctorUser.getId();
        List<Appointment> appointmentModelList = appointmentRepository.findByAppointmentDateTimeBetweenAndDoctorId(startDateTime, endDateTime, doctorUserId);

        return appointmentModelList;
    }


}


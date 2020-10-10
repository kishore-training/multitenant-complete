package com.udemy.multitenancy.service;

import com.udemy.multitenancy.model.Appointment;
import com.udemy.multitenancy.model.types.StatusEnum;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@EnableMongoRepositories
public interface AppointmentRepository extends PagingAndSortingRepository<Appointment, String> {

    public List<Appointment> findByAppointmentDateTime(Date appointmentDateTime);

    public  List<Appointment> findByAppointmentDateTimeBetweenAndDoctorId(Date from, Date to, String doctorId);

}

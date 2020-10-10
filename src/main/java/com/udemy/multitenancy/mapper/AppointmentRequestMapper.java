package com.udemy.multitenancy.mapper;

import com.udemy.multitenancy.model.Appointment;
import com.udemy.multitenancy.vo.AppointmentRequestVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AppointmentRequestMapper {

    AppointmentRequestMapper INSTANCE = Mappers.getMapper(AppointmentRequestMapper.class);



    Appointment appointmentVOToModel(AppointmentRequestVO vo);


    AppointmentRequestVO appointmentModelToVO(Appointment appointment);
}

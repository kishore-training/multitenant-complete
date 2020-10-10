package com.udemy.multitenancy.mapper;

import com.udemy.multitenancy.model.DoctorUser;
import com.udemy.multitenancy.model.Hospital;
import com.udemy.multitenancy.vo.HospitalRequestVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HospitalRequestMapper {

    HospitalRequestMapper INSTANCE = Mappers.getMapper(HospitalRequestMapper.class);


    Hospital hospitalVOToModel(HospitalRequestVO vo);

    HospitalRequestVO hospitalModelToVO(Hospital appointment);

    DoctorUser hospitalVOToDoctorUser(HospitalRequestVO vo);
}

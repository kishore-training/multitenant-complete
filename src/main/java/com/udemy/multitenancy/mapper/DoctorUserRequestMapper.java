package com.udemy.multitenancy.mapper;

import com.udemy.multitenancy.model.DoctorUser;
import com.udemy.multitenancy.vo.DoctorUserRequestVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DoctorUserRequestMapper {

    DoctorUserRequestMapper INSTANCE = Mappers.getMapper(DoctorUserRequestMapper.class);


    DoctorUser organizationStaffVOToModel(DoctorUserRequestVO vo);

    DoctorUserRequestVO organizationStaffModelToVO(DoctorUser doctorUser);
}

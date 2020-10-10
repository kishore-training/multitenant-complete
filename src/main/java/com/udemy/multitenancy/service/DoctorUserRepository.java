package com.udemy.multitenancy.service;

import com.udemy.multitenancy.model.DoctorUser;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository()
@EnableMongoRepositories
public interface DoctorUserRepository extends PagingAndSortingRepository<DoctorUser, String> {

    public List<DoctorUser> findByHospitalName(String hospitalName);

    public List<DoctorUser> findByUserNameAndHospitalName(String userName, String hospitalName);

    public List<DoctorUser> findByUserName(String userName);
}

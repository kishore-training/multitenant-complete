package com.udemy.multitenancy.service;

import com.udemy.multitenancy.model.Hospital;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableMongoRepositories
public interface HospitalRepository extends PagingAndSortingRepository<Hospital, String> {

    List<Hospital> findByEmail(String email);

    List<Hospital> findByHospitalName(String hospitalName);

}

package com.udemy.multitenancy.security;

import com.udemy.multitenancy.model.DoctorUser;
import com.udemy.multitenancy.model.Hospital;
import com.udemy.multitenancy.model.types.UserTypeEnum;
import com.udemy.multitenancy.service.DoctorUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;


import java.util.Arrays;
import java.util.List;


@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService {
	
	@Autowired
	private DoctorUserRepository doctorUserRepository;

	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		String[] usernameAndHospital = StringUtils.split(
				userName, String.valueOf(Character.LINE_SEPARATOR));
		if (usernameAndHospital == null || usernameAndHospital.length != 2) {
			throw new UsernameNotFoundException("Username and hospital must be provided");
		}
		List<DoctorUser> doctorUsers = doctorUserRepository.findByUserNameAndHospitalName(usernameAndHospital[0],usernameAndHospital[1]);
		DoctorUser doctorUser = null;
		if(doctorUsers == null || doctorUsers.isEmpty()){
			throw new UsernameNotFoundException("Invalid user email or password.");
		}else{
			doctorUser = doctorUsers.get(0);

		}

		return new org.springframework.security.core.userdetails.User(doctorUser.getUserName(),(doctorUser.getPassword()), getAuthority(doctorUser));
	}

	private List<SimpleGrantedAuthority> getAuthority(DoctorUser doctorUser) {

		return Arrays.asList(new SimpleGrantedAuthority(doctorUser.getUserTypeEnum().name()));
	}


}

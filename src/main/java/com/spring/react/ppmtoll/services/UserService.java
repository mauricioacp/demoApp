package com.spring.react.ppmtoll.services;

import com.spring.react.ppmtoll.domain.User;
import com.spring.react.ppmtoll.exceptions.UsernameAlreadyExistsException;
import com.spring.react.ppmtoll.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService  {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public User saveUser (User newUser){

		try {
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			//Username has to be unique(exception)
			newUser.setUsername(newUser.getUsername());
			newUser.setConfirmPassword("");
			return userRepository.save(newUser);

		}catch (Exception e){
			throw new UsernameAlreadyExistsException("Username " +newUser.getUsername()+" already exists");
		}

		//Make sure that password and confirm password match
		//We don't persist or show the confirm password
	}


}

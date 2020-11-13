package com.kcb.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kcb.model.User;
import com.kcb.repositories.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> getAll(){
		return (List<User>) userRepository.findAll();
	}
	
	public User saveUser(User user){
		
		User us = null;
		if(user.getId() == 0){
			String password = user.getPassword();
			user.setDateCreated(new Date());
			user.setPassword(passwordEncoder.encode(password));
			user.setAccountExpired(true);
			user.setAccountLocked(true);
			us = userRepository.save(user);
		}else{
			user.setLastUpdate(new Date());
			us = userRepository.save(user);
		}
        
		return us;
	}

	 @Override
	    public CustomUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
	        User user = userRepository.findByUsername(username);
	        if (user != null) {
	        	CustomUserDetail customUserDetail=new CustomUserDetail();
	            customUserDetail.setUser(user);
	            //customUserDetail.setAuthorities(authorities);
	            return customUserDetail;

	        }
	        throw new UsernameNotFoundException(username);
	    }

}

package com.qbi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.qbi.DAO.UserDAO;
import com.qbi.model.AuthUserDetails;
import com.qbi.model.User;

@Service
public class AuthUserDetailsController implements UserDetailsService{
	
	@Autowired	
	UserDAO userDao;	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		User user_found = userDao.findUserByUsername(username);
		
		if(user_found == null) {
			throw new UsernameNotFoundException("User "+ username +" Not Found");
		}

		return new AuthUserDetails(user_found);
	}
	
}

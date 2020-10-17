package com.qbi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.qbi.model.AuthenticationRequest;
import com.qbi.model.AuthenticationResponse;
import com.qbi.service.AuthUserDetailsController;
import com.qbi.util.TokenUtil;

@RestController
public class AuthenticationController {
	
	@Autowired
	private AuthUserDetailsController authUserDetailsController;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@PostMapping("/Authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response)
			throws Exception{
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
					);
			
		}catch(BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		
		}
		
		final UserDetails userDetails = authUserDetailsController.loadUserByUsername(authenticationRequest.getUsername());
		final String token = tokenUtil.generateToken(userDetails);
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}
	
}

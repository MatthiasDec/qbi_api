package com.qbi.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.qbi.DAO.UtilsDAO;
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
	
	@Autowired
	private UtilsDAO utilsDAO;
	
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/Authenticate/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestBody Map<String, Object> body, HttpServletRequest request){
		
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || !token.startsWith("Bearer") || token.length() <= 7) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "401");
			error.put("title", "Unauthorized");
			error.put("details", "User not properly logged in");
			return new ResponseEntity(error, HttpStatus.UNAUTHORIZED);
		}
		
		token = token.substring(7);
		String oldPassword = (String) body.get("old_password");
		String newPassword = (String) body.get("new_password");
		
		if (oldPassword.isBlank() || newPassword.isBlank()) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "400");
			error.put("title", "Bad Request");
			error.put("details", "The request body should contain a 'old_password' and a 'new_password' parameters");
			return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
		}
		
		String username = tokenUtil.extractUsername(token);
		
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, oldPassword));
			boolean response = utilsDAO.updateUserPassword(username, newPassword);
			
			if(response) {
				Map<String, String> responseRequest = new HashMap<String, String>();
				responseRequest.put("status", "200");
				responseRequest.put("title", "OK");
				responseRequest.put("details", "Password successfully updated");
				return new ResponseEntity(responseRequest, HttpStatus.OK);
			}
			
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "500");
			error.put("title", "Internal Error");
			error.put("details", "Internal Error");
			return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		catch(AuthenticationException e) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "401");
			error.put("title", "Unauthorized");
			error.put("details", "User not properly logged in");
			return new ResponseEntity(error, HttpStatus.UNAUTHORIZED);
		}
		
	}
}

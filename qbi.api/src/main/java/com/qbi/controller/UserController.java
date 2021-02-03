package com.qbi.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qbi.DAO.CompanyDAO;
import com.qbi.DAO.ProductDAO;
import com.qbi.DAO.UserDAO;
import com.qbi.DAO.UtilsDAO;
import com.qbi.model.RoleEnum;
import com.qbi.util.TokenUtil;

import io.jsonwebtoken.Claims;

@RestController
public class UserController {

	@Autowired
	UserDAO userDAO;
	
	@Autowired
	ProductDAO productDAO;
	
	@Autowired
	CompanyDAO companyDAO;
	
	@Autowired
	UtilsDAO utilsDAO;
	
	@Autowired
	TokenUtil tokenUtil;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/users/{userId}/relationship/products/{productId}")
	public ResponseEntity<?> linkUserToProduct(@PathVariable("userId") int userId, @PathVariable("productId") int productId){
		
		if(!utilsDAO.isEntryExistring(userId, "app_user")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The user " + userId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(!utilsDAO.isEntryExistring(productId, "product")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The product " + productId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(productDAO.checkIfRelationExists(userId, productId)) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "409");
			error.put("title", "Conflict");
			error.put("details", "The relationship userId:" + userId + " and productId:" + productId + " alreayd exists");
			return new ResponseEntity(error, HttpStatus.CONFLICT);
		}
		
		productDAO.linkProductAndUser(userId, productId);
		
		return new ResponseEntity(null, HttpStatus.CREATED);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/users/{userId}/relationship/products/{productId}")
	public ResponseEntity<?> unlinkUserToProduct(@PathVariable("userId") int userId, @PathVariable("productId") int productId){
		
		if(!utilsDAO.isEntryExistring(userId, "app_user")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The user " + userId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(!utilsDAO.isEntryExistring(productId, "product")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The product " + productId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(!productDAO.checkIfRelationExists(userId, productId)) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The link between the user " + userId + " and the product " + productId + " does not exists");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		boolean deleted = productDAO.unlinkProductAndUser(userId, productId);
		
		if(deleted) {
			return new ResponseEntity(null, HttpStatus.OK);
		}
		else {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "500");
			error.put("title", "Internal Error");
			error.put("details", "Internal Error");
			return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/users/{userId}/relationship/companies/{companyId}")
	public ResponseEntity<?> linkUserToCompany(@PathVariable("userId") int userId, @PathVariable("companyId") int companyId){

		if(!utilsDAO.isEntryExistring(userId, "app_user")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The user " + userId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(!utilsDAO.isEntryExistring(companyId, "company")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The company " + companyId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(companyDAO.checkIfRelationExists(userId, companyId)) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "409");
			error.put("title", "Conflict");
			error.put("details", "The relationship userId:" + userId + " and companyId:" + companyId + " alreayd exists");
			return new ResponseEntity(error, HttpStatus.CONFLICT);
		}
		
		companyDAO.linkCompanyAndUser(companyId, userId);
		
		return new ResponseEntity(null, HttpStatus.CREATED);
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/users/{userId}/relationship/companies/{companyId}")		
	public ResponseEntity<?> unlinkUserToCompany(@PathVariable("userId") int userId, @PathVariable("companyId") int companyId){

		if(!utilsDAO.isEntryExistring(userId, "app_user")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The user " + userId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(!utilsDAO.isEntryExistring(companyId, "company")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The company " + companyId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(!companyDAO.checkIfRelationExists(userId, companyId)) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The link between the user " + userId + " and the company " + companyId + " does not exists");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		boolean deleted = companyDAO.unlinkCompanyAndUser(userId, companyId);
		
		if(deleted) {
			return new ResponseEntity(null, HttpStatus.OK);
		}
		else {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "500");
			error.put("title", "Internal Error");
			error.put("details", "Internal Error");
			return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/users/role")
	public ResponseEntity<?> getUserRole(HttpServletRequest request){
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || !token.startsWith("Bearer") || token.length() <= 7) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "401");
			error.put("title", "Unauthorized");
			error.put("details", "User not properly logged in");
			return new ResponseEntity(error, HttpStatus.UNAUTHORIZED);
		}
		
		token = token.substring(7);
		int roleNumber;
		try {
			String role = tokenUtil.extractRole(token);
			roleNumber = RoleEnum.valueOf(role).getRoleNumber();
		}catch(IllegalArgumentException e) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "500");
			error.put("title", "Internal Server Error");
			error.put("details", "Unknown role");
			return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("role_number", roleNumber);
		
		return new ResponseEntity(response, HttpStatus.OK);
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/users/teapot")
	public ResponseEntity<?> isUserTeaPot(){
		Map<String, String> error = new HashMap<String, String>();
		error.put("status", "418");
		error.put("title", "I Am A Teapot");
		error.put("details", "You are a teapot");
		return new ResponseEntity(error, HttpStatus.I_AM_A_TEAPOT);
	}
}

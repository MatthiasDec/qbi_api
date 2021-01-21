package com.qbi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qbi.DAO.CompanyDAO;
import com.qbi.DAO.ProductDAO;
import com.qbi.DAO.UserDAO;
import com.qbi.DAO.UtilsDAO;

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
}

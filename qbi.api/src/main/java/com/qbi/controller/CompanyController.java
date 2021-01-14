package com.qbi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.qbi.DAO.CompanyDAO;
import com.qbi.DAO.UserDAO;
import com.qbi.DAO.UtilsDAO;
import com.qbi.model.User;
import com.qbi.util.TokenUtil;

@RestController
public class CompanyController {

	@Autowired
	UserDAO userDAO;
	
	@Autowired
	TokenUtil tokenUtil;
	
	@Autowired
	UtilsDAO utilsDAO;

	@Autowired
	CompanyDAO companyDAO;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/companies")
	public ResponseEntity<?> createCompany(@RequestBody(required=false) Map<String, Object> requestBody){
		int createdCompanyId = utilsDAO.createEntry("company", requestBody);
		
		Map<String, Object> constructedCompany = companyDAO.getCompanyById(createdCompanyId);
			
		return new ResponseEntity(constructedCompany, HttpStatus.CREATED);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@DeleteMapping("/companies/{id}")
	public ResponseEntity<?> deleteCompany(@PathVariable("id") int companyId){

		if(!utilsDAO.isEntryExistring(companyId, "company")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The company " + companyId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		boolean deleted = utilsDAO.deleteEntry("company", companyId);
		
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
	@GetMapping("/companies/user")
	public ResponseEntity<?> getCompanies(HttpServletRequest request){
		
		String authToken = request.getHeader("Authorization").substring(6, request.getHeader("Authorization").length());
		String username = tokenUtil.extractUsername(authToken);
		
		User user = userDAO.findUserByUsername(username);
		
		int userId = user.getId();
		
		if(!utilsDAO.isEntryExistring(userId, "app_user")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "403");
			error.put("title", "Unauthorized");
			error.put("details", "User not found");
			return new ResponseEntity(error, HttpStatus.UNAUTHORIZED);
		}
		
		List<Map<String, Object>> listCompanies = companyDAO.getCompaniesByUserId(userId);
		
		return new ResponseEntity(listCompanies, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/companies/{id}")
	public ResponseEntity<?> getCompany(@PathVariable("id") int companyId){
		if(!utilsDAO.isEntryExistring(companyId, "company")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The company " + companyId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		Map<String, Object> company = companyDAO.getCompanyById(companyId);
		return new ResponseEntity(company, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/companies/user/{userId}")
	public ResponseEntity<?> getUserCompanies(HttpServletRequest request, @PathVariable("userId") int userId){
		// ENDPOINT FOR ADMIN / SALES
		if(!utilsDAO.isEntryExistring(userId, "app_user")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The user " + userId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		List<Map<String, Object>> listCompanies = companyDAO.getCompaniesByUserId(userId);
		
		return new ResponseEntity(listCompanies, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PatchMapping("/companies/{id}")
	public ResponseEntity<?> modifyCompany(@PathVariable("id") int companyId, @RequestBody(required=false) Map<String, Object> requestBody){
		
		if(!utilsDAO.isEntryExistring(companyId, "company")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The company " + companyId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		if(requestBody != null) {
			utilsDAO.updateEntry("company", requestBody, companyId);
		}
		
		Map<String, Object> company = utilsDAO.getEntry("company", companyId);
		
		return new ResponseEntity(company, HttpStatus.OK);
	}
	
}

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.qbi.DAO.IssuerDAO;
import com.qbi.DAO.UtilsDAO;

@RestController
public class IssuerController {

	@Autowired
	private IssuerDAO issuerDAO;

	@Autowired
	private UtilsDAO utilsDAO;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/issuer/create")
	public ResponseEntity<?> createIssuer(@RequestBody(required = false) Map<String, Object> requestBody) {

		int createdIssuerId = issuerDAO.createIssuer(requestBody);
		Map<String, Object> createdIssuer = issuerDAO.getIssuer(createdIssuerId);

		return new ResponseEntity(createdIssuer, HttpStatus.CREATED);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/issuer/{issuerId}")
	public ResponseEntity<?> getIssuer(@PathVariable("issuerId") int issuerId){
		
		if(!utilsDAO.isEntryExistring(issuerId, "issuer")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The issuer " + issuerId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		Map<String, Object> issuer = issuerDAO.getIssuer(issuerId);
		return new ResponseEntity(issuer, HttpStatus.OK);	
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/issuer/product/{productId}")
	public ResponseEntity<?> getProductIssuer(HttpServletRequest request, @PathVariable("productId") int productId){
		
		if(!utilsDAO.isEntryExistring(productId, "product")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The product " + productId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		Map<String, Object> issuer = issuerDAO.getIssuerByProductId(productId);
		
		return new ResponseEntity(issuer, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/issuer/{issuerId}")
	public ResponseEntity<?> deleteProduct(@PathVariable("issuerId") int issuerId){
		if(!utilsDAO.isEntryExistring(issuerId, "issuer")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The issuer " + issuerId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		Map<String, Object> issuer = issuerDAO.deleteIssuer(issuerId);
		return new ResponseEntity(issuer, HttpStatus.OK);
	}

}

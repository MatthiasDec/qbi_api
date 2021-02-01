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

import com.qbi.DAO.IssuerDAO;
import com.qbi.DAO.UtilsDAO;

@RestController
public class IssuerController {

	@Autowired
	private IssuerDAO issuerDAO;

	@Autowired
	private UtilsDAO utilsDAO;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/issuers")
	public ResponseEntity<?> createIssuer(@RequestBody(required = false) Map<String, Object> requestBody) {
		
		int createdIssuerId = utilsDAO.createEntry("issuer", requestBody);
		
		Map<String, Object> issuer = issuerDAO.getIssuer(createdIssuerId);

		return new ResponseEntity(issuer, HttpStatus.CREATED);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/issuers/{issuerId}")
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
	@GetMapping("/products/{productId}/issuers")
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
	@GetMapping("/issuers")
	public ResponseEntity<?> getIssuers(HttpServletRequest request){
		List<Map<String, Object>> issuerList = issuerDAO.getIssuers();
		return new ResponseEntity(issuerList, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/issuers/{issuerId}")
	public ResponseEntity<?> deleteIssuer(@PathVariable("issuerId") int issuerId){
		if(!utilsDAO.isEntryExistring(issuerId, "issuer")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The issuer " + issuerId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		boolean deleted = utilsDAO.deleteEntry("issuer", issuerId);
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
	@PatchMapping("/issuers/{issuerId}")
	public ResponseEntity<?> updateIssuer(@PathVariable("issuerId") int issuerId, @RequestBody(required=false) Map<String, Object> requestBody){
		if(!utilsDAO.isEntryExistring(issuerId, "issuer")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The issuer " + issuerId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(requestBody != null) {
			utilsDAO.updateEntry("issuer", requestBody, issuerId);
		}
		
		Map<String, Object> issuerUpdated = utilsDAO.getEntry("issuer", issuerId);
		return new ResponseEntity(issuerUpdated, HttpStatus.OK);
	}
	
}

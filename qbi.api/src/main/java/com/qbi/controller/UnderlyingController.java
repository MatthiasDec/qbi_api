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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.qbi.DAO.UnderlyingDAO;
import com.qbi.DAO.UtilsDAO;

@RestController
public class UnderlyingController {

	@Autowired
	private UnderlyingDAO underlyingDAO;

	@Autowired
	private UtilsDAO utilsDAO;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/underlying/create")
	public ResponseEntity<?> createUnderlying(@RequestBody(required = false) Map<String, Object> requestBody) {

		int createdIssuerId = underlyingDAO.createUnderlying(requestBody); // TODO send back 1 = réussie
		Map<String, Object> createdUnderlying = underlyingDAO.getUnderlying(createdIssuerId);

		return new ResponseEntity(createdUnderlying, HttpStatus.CREATED);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/underlying/product/create")
	public ResponseEntity<?> createlinkUnderlyingProduct(@RequestBody(required = false) Map<String, Object> requestBody) {

		int createdIssuerId = underlyingDAO.createlinkUnderlyingProduct(requestBody); // TODO send back 1 = réussie
		Map<String, Object> createdUnderlying = underlyingDAO.getUnderlying(createdIssuerId);

		return new ResponseEntity(createdUnderlying, HttpStatus.CREATED);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/underlying/{underlyingId}")
	public ResponseEntity<?> getIssuer(@PathVariable("underlyingId") int underlyingId){
		
		if(!utilsDAO.isEntryExistring(underlyingId, "underlying")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The underlying " + underlyingId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		Map<String, Object> underlying = underlyingDAO.getUnderlying(underlyingId);
		return new ResponseEntity(underlying, HttpStatus.OK);	
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/underlying/product/{productId}")
	public ResponseEntity<?> getProductUnderlying(HttpServletRequest request, @PathVariable("productId") int productId){
		
		if(!utilsDAO.isEntryExistring(productId, "product")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The product " + productId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		List<Map<String, Object>> issuerList = underlyingDAO.getUnderlyingsByProductId(productId);
		return new ResponseEntity(issuerList, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/underlying/{underlyingId}")
	public ResponseEntity<?> deleteUnderlying(@PathVariable("underlyingId") int underlyingId){
		if(!utilsDAO.isEntryExistring(underlyingId, "underlying")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The underlying " + underlyingId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		Map<String, Object> underlying = underlyingDAO.deleteUnderlying(underlyingId);
		return new ResponseEntity(underlying, HttpStatus.OK);
	}

}

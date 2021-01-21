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

import com.qbi.DAO.UnderlyingDAO;
import com.qbi.DAO.UtilsDAO;
import com.qbi.util.QBIUtils;

@RestController
public class UnderlyingController {

	@Autowired
	private UnderlyingDAO underlyingDAO;

	@Autowired
	private UtilsDAO utilsDAO;

	@Autowired
	private QBIUtils qbiUtils;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/underlyings")
	public ResponseEntity<?> createUnderlying(@RequestBody(required = false) Map<String, Object> requestBody) {

		int createdUnderlyingId = utilsDAO.createEntry("underlying", requestBody);
		
		Map<String, Object> createdUnderlying = underlyingDAO.getUnderlying(createdUnderlyingId);
			
		return new ResponseEntity(createdUnderlying, HttpStatus.CREATED);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/products/{productId}/relationship/underlyings/{underlyingId}")
	public ResponseEntity<?> linkUnderlyingProduct(@RequestBody(required = false) Map<String, Object> requestBody, @PathVariable("productId") int productId, 
			@PathVariable("underlyingId") int underlyingId) {

		if(!utilsDAO.isEntryExistring(productId, "product")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The product " + productId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(!utilsDAO.isEntryExistring(underlyingId, "underlying")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The underlying " + underlyingId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(underlyingDAO.checkIfRelationExists(productId, underlyingId)) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "409");
			error.put("title", "Conflict");
			error.put("details", "The relationship productId:" + productId + " and underlyingId:" + underlyingId + " alreayd exists");
			return new ResponseEntity(error, HttpStatus.CONFLICT);
		}
		
		if(requestBody.get("fixing_value") == null || !qbiUtils.objectParsableToLong(requestBody.get("fixing_value"))) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "400");
			error.put("title", "Bad Request");
			error.put("details", "The request body should contain a numeric 'fixing_value' argument");
			return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
		}
		
		underlyingDAO.linkUnderlyingProduct(productId, underlyingId, requestBody);

		return new ResponseEntity(null, HttpStatus.CREATED);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/products/{productId}/relationship/underlyings/{underlyingId}")
	public ResponseEntity<?> unlinkUnderlyingProduct(@PathVariable("productId") int productId, 
			@PathVariable("underlyingId") int underlyingId) {

		if(!utilsDAO.isEntryExistring(productId, "product")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The product " + productId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(!utilsDAO.isEntryExistring(underlyingId, "underlying")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The underlying " + underlyingId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(!underlyingDAO.checkIfRelationExists(productId, underlyingId)) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The relationship productId:" + productId + " and underlyingId:" + underlyingId + " does not exist");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
				
		underlyingDAO.unlinkUnderlyingProduct(productId, underlyingId);
		
		return new ResponseEntity(null, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/underlyings/{underlyingId}")
	public ResponseEntity<?> getUnderlying(@PathVariable("underlyingId") int underlyingId){
		
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
	@GetMapping("/products/{productId}/underlyings")
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
	@DeleteMapping("/underlyings/{underlyingId}")
	public ResponseEntity<?> deleteUnderlying(@PathVariable("underlyingId") int underlyingId){
		if(!utilsDAO.isEntryExistring(underlyingId, "underlying")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The underlying " + underlyingId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		boolean deleted = utilsDAO.deleteEntry("underlying", underlyingId);
		
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
	@PatchMapping("/underlyings/{underlyingId}")
	public ResponseEntity<?> updateUnderlying(@PathVariable("underlyingId") int underlyingId, @RequestBody(required = false) Map<String, Object> requestBody){
		if(!utilsDAO.isEntryExistring(underlyingId, "underlying")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The underlying " + underlyingId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(requestBody != null) {
			utilsDAO.updateEntry("underlying", requestBody, underlyingId);
		}
		
		Map<String, Object> underlyingUpdated = utilsDAO.getEntry("underlying", underlyingId);
		return new ResponseEntity(underlyingUpdated, HttpStatus.OK);
	}
	
}

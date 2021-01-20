package com.qbi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.qbi.DAO.ProductHistoryDAO;
import com.qbi.DAO.UtilsDAO;

@RestController
public class ProductHistoryController {

	@Autowired
	private ProductHistoryDAO producthistoryDAO;

	@Autowired
	private UtilsDAO utilsDAO;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/producthistory")
	public ResponseEntity<?> createProductHistory(@RequestBody(required = false) Map<String, Object> requestBody) {

		int CreatedProductHistoryId = utilsDAO.createEntry("product_history",requestBody); // TODO send back 1 = réussie
		Map<String, Object> createdProducthistory = producthistoryDAO.getProductHistorybyID(CreatedProductHistoryId);

		return new ResponseEntity(createdProducthistory, HttpStatus.CREATED);
	}

	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/producthistories/{productId}")
	public ResponseEntity<?> getProductHistories(@PathVariable("productId") int productId){
		
		if(!utilsDAO.isEntryExistring(productId, "product")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The product " + productId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		List<Map<String, Object>> producthistories = producthistoryDAO.getProductHistoryFull(productId);
		return new ResponseEntity(producthistories, HttpStatus.OK);	
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping("/producthistory")
    public ResponseEntity<?> getProductHistoryOnDate(@RequestParam(value="productId",required=true) int productId,
    @RequestParam(value="valuedate",required=true) Date valuedate){
		
		if(!utilsDAO.isEntryExistring(productId, "product")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The product " + productId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		Map<String, Object> producthistoryondate = producthistoryDAO.getProductHistoryOnDate(productId,valuedate);
		return new ResponseEntity(producthistoryondate, HttpStatus.OK);	
	}



	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/producthistory/{producthistoryId}")
	public ResponseEntity<?> deleteProductHistory(@PathVariable("producthistoryId") int producthistoryId){
		if(!utilsDAO.isEntryExistring(producthistoryId, "product_history")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The product " + producthistoryId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
        boolean succes = utilsDAO.deleteEntry("product_history",producthistoryId);
        if(succes) {
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

package com.qbi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.qbi.util.QBIUtils;

@RestController
public class ProductHistoryController {

	@Autowired
	private ProductHistoryDAO producthistoryDAO;

	@Autowired
	private UtilsDAO utilsDAO;

	@Autowired
	private QBIUtils qbiUtils;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/products/history")
	public ResponseEntity<?> createProductHistory(@RequestBody(required = false) Map<String, Object> requestBody) {

		int CreatedProductHistoryId = utilsDAO.createEntry("product_history",requestBody);
		
		Map<String, Object> createdProducthistory = producthistoryDAO.getProductHistorybyID(CreatedProductHistoryId);

		return new ResponseEntity(createdProducthistory, HttpStatus.CREATED);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/products/{productId}/history")
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
    @GetMapping("/products/{productId}/history/date")
    public ResponseEntity<?> getProductHistoryOnDate(@PathVariable("productId") int productId,
    @RequestParam(value="valuedate",required=true) String valuedate) throws ParseException{
		
    	if(!qbiUtils.checkIfProperDate(valuedate)) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "400");
			error.put("title", "Bad Request");
			error.put("details", "The valuedate should be in format 'yyyy-MM-dd'");
			return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    	}
    	
		if(!utilsDAO.isEntryExistring(productId, "product")) {	
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The product " + productId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH).parse(valuedate);
		
		Map<String, Object> producthistoryondate = producthistoryDAO.getProductHistoryOnDate(productId, date);
		return new ResponseEntity(producthistoryondate, HttpStatus.OK);	
	}

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping("/products/{productId}/history/between")
    public ResponseEntity<?> getProductHistoryBetDate(@PathVariable("productId") int productId,
    @RequestParam(value="beg_date",required=true) Date beg_date,@RequestParam(value="end_date",required=true) Date end_date){
		
		if(!utilsDAO.isEntryExistring(productId, "product")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The product " + productId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		List<Map<String, Object>> producthistorybetdate = producthistoryDAO.getProductHistoryBetdate(productId,beg_date,end_date);
		return new ResponseEntity(producthistorybetdate, HttpStatus.OK);	
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/products/history/{producthistoryId}")
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

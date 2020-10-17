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

import com.qbi.DAO.ProductDAO;
import com.qbi.DAO.UserDAO;
import com.qbi.DAO.UtilsDAO;

@RestController
public class ProductController {

	@Autowired
	UserDAO userDAO;
	
	@Autowired
	UtilsDAO utilsDAO;
	
	@Autowired
	ProductDAO productDAO;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/products/{productId}")
	public ResponseEntity<?> getProduct(@PathVariable("productId") int productId){
		
		if(!utilsDAO.isEntryExistring(productId, "product")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The product " + productId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		Map<String, Object> product = productDAO.getProduct(productId);
		return new ResponseEntity(product, HttpStatus.OK);
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/products")
	public ResponseEntity<?> createProduct(@RequestBody(required=false) Map<String, Object> requestBody){
		
		Map<String, Object> createdProduct = productDAO.createProduct(requestBody);
		
		return new ResponseEntity(createdProduct, HttpStatus.CREATED);
	}
	
	@PatchMapping("/products/{productId}")
	public ResponseEntity<?> updateProduct(@PathVariable("productId") int productId, @RequestBody(required = false) Map<String, Object> requestBody){
		//TODO
		return new ResponseEntity(HttpStatus.OK, null);
	}
	
	@DeleteMapping("/products/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable("productId") int productId){
		//TODO
		return new ResponseEntity(HttpStatus.OK, null);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/products/user/{userId}")
	public ResponseEntity<?> getUserProducts(HttpServletRequest request, @PathVariable("userId") int userId){
		
		if(!utilsDAO.isEntryExistring(userId, "app_user")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The user " + userId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		List<Map<String, Object>> listProducts = productDAO.getProductsByUserId(userId);
		
		return new ResponseEntity(listProducts, HttpStatus.OK);
	}
}

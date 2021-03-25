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
import com.qbi.model.User;
import com.qbi.util.TokenUtil;

@RestController
public class ProductController {

	@Autowired
	UserDAO userDAO;
	
	@Autowired
	UtilsDAO utilsDAO;
	
	@Autowired
	TokenUtil tokenUtil;
	
	@Autowired
	ProductDAO productDAO;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/products")
	public ResponseEntity<?> createProduct(@RequestBody(required=false) Map<String, Object> requestBody){
		
		int createdProductId = utilsDAO.createEntry("product", requestBody);
		
		Map<String, Object> constructedProduct = productDAO.getProduct(createdProductId);
			
		return new ResponseEntity(constructedProduct, HttpStatus.CREATED);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/products/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable("productId") int productId){

		if(!utilsDAO.isEntryExistring(productId, "product")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The product " + productId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		boolean deleted = utilsDAO.deleteEntry("product", productId);
		
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
	@GetMapping("/products/user")
	public ResponseEntity<?> getListProducts(HttpServletRequest request){
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
		
		List<Map<String, Object>> listProducts = productDAO.getProductsByUserId(userId);
		
		return new ResponseEntity(listProducts, HttpStatus.OK);
		
	}
	
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
	@GetMapping("/products/user/{userId}")
	public ResponseEntity<?> getUserProducts(HttpServletRequest request, @PathVariable("userId") int userId){
		// ENDPOINT FOR ADMIN / SALES
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PatchMapping("/products/{productId}")
	public ResponseEntity<?> updateProduct(@PathVariable("productId") int productId, @RequestBody(required = false) Map<String, Object> requestBody){
		if(!utilsDAO.isEntryExistring(productId, "product")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The product " + productId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		if(requestBody != null) {
			utilsDAO.updateEntry("product", requestBody, productId);
		}
		
		Map<String, Object> product = utilsDAO.getEntry("product", productId);
		
		return new ResponseEntity(product, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/products/issuer/count")
	public ResponseEntity<?> getProductIssuerCount(){
		
		List<Map<String, Object>> issuerCountList = productDAO.countIssuers();
		return new ResponseEntity(issuerCountList, HttpStatus.OK);
	}
}

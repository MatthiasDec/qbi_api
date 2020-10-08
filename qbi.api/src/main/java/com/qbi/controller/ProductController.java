package com.qbi.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.qbi.DAO.ProductDAO;
import com.qbi.DAO.UserDAO;

@RestController
public class ProductController {

	@Autowired
	UserDAO userDAO;
	
	@Autowired
	ProductDAO productDAO;
	
	@GetMapping("/test")
	public void test() {
		System.out.println(userDAO.count());
		
	}
	
	@GetMapping("/products/user/{userId}")
	public ResponseEntity<?> getUserProducts(HttpServletRequest request, @PathVariable("userId") int userId){
		return new ResponseEntity(HttpStatus.OK, null);
	}
	
}

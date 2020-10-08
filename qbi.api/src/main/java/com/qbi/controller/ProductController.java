package com.qbi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qbi.DAO.UserDAO;

@RestController
public class ProductController {

	@Autowired
	UserDAO userDAO;
	
	@GetMapping("/test")
	public void test() {
		System.out.println(userDAO.count());
		
	}
	
}

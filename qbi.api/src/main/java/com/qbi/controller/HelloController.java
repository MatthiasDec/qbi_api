package com.qbi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
	
	@GetMapping("/hello")
	public ResponseEntity<?> hello(){
		return ResponseEntity.ok("Hello");
	}
	
	@GetMapping("/test/role/admin")
	public ResponseEntity<?> testRoleAdmin(){
		return ResponseEntity.ok("Ok t'es bien admin");
	}
	
	@GetMapping("/test/role/sales")
	public ResponseEntity<?> testRoleSales(){
		return ResponseEntity.ok("Ok t'es bien sales");
	}
	
	@GetMapping("/test/role/user")
	public ResponseEntity<?> testRoleUser(){
		return ResponseEntity.ok("Ok t'es bien user");
	}
	
}

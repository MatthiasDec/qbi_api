package com.qbi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KPIController {

	@GetMapping("/users/kpi/count")
	public ResponseEntity<?> getUserCount(){
		return null ; //TODO
	}
	
}

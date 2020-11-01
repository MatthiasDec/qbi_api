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

import com.qbi.DAO.EventDAO;
import com.qbi.DAO.UtilsDAO;

@RestController
public class EventController {

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private UtilsDAO utilsDAO;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/event/create")
	public ResponseEntity<?> createEvent(@RequestBody(required = false) Map<String, Object> requestBody) {
		int createdEventId = eventDAO.createEvent(requestBody);
		Map<String, Object> event = eventDAO.getEvent(createdEventId); // TODO : it sends back 1 so useless

		return new ResponseEntity(event, HttpStatus.CREATED);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/event/{eventId}")
	public ResponseEntity<?> getEvent(@PathVariable("eventId") int eventId){
		
		if(!utilsDAO.isEntryExistring(eventId, "event")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The event " + eventId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		Map<String, Object> event = eventDAO.getEvent(eventId);
		return new ResponseEntity(event, HttpStatus.OK);	
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/event/product/{productId}")
	public ResponseEntity<?> getProductEvent(HttpServletRequest request, @PathVariable("productId") int productId){
		
		if(!utilsDAO.isEntryExistring(productId, "product")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The product " + productId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		List<Map<String, Object>> eventList = eventDAO.getEventsByProductId(productId);
		
		return new ResponseEntity(eventList, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/event/{eventId}")
	public ResponseEntity<?> deleteEvent(@PathVariable("eventId") int eventId){
		if(!utilsDAO.isEntryExistring(eventId, "event")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The event " + eventId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		Map<String, Object> event = eventDAO.deleteEvent(eventId); // TODO Sends back an error but still fails
		return new ResponseEntity(event, HttpStatus.OK);
	}

}

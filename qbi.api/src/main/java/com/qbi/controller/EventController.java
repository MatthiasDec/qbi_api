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

import com.qbi.DAO.EventDAO;
import com.qbi.DAO.UtilsDAO;

@RestController
public class EventController {

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private UtilsDAO utilsDAO;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/events")
	public ResponseEntity<?> createEvent(@RequestBody(required = false) Map<String, Object> requestBody) {
		
		int createdEventId = utilsDAO.createEntry("event", requestBody);
		
		Map<String, Object> constructedEvent = eventDAO.getEvent(createdEventId);
			
		return new ResponseEntity(constructedEvent, HttpStatus.CREATED);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/eventsList")
	public ResponseEntity<?> createEvent(@RequestBody(required = false) List<Map<String, Object>> requestBody) {
		
		int createdEventId = utilsDAO.createListEntry("event", requestBody);
		
		Map<String, Object> constructedEvent = eventDAO.getEvent(createdEventId);
			
		return new ResponseEntity(constructedEvent, HttpStatus.CREATED);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/events/{eventId}")
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
	@GetMapping("/products/{productId}/events")
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
	@GetMapping("/products/{productId}/events/next")
	public ResponseEntity<?> getProductNextEvent(HttpServletRequest request, @PathVariable("productId") int productId){
		
		if(!utilsDAO.isEntryExistring(productId, "product")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The product " + productId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		Map<String, Object> event = eventDAO.getNextEvent(productId);
		
		return new ResponseEntity(event, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/events/{eventId}")
	public ResponseEntity<?> deleteEvent(@PathVariable("eventId") int eventId){
		
		if(!utilsDAO.isEntryExistring(eventId, "event")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The event " + eventId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}

		boolean deleted = utilsDAO.deleteEntry("event", eventId);
		
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
	@PatchMapping("/events/{eventId}")
	public ResponseEntity<?> updateEvent(@PathVariable("eventId") int eventId, @RequestBody(required = false) Map<String, Object> requestBody){
		if(!utilsDAO.isEntryExistring(eventId, "event")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The event " + eventId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(requestBody != null) {
			utilsDAO.updateEntry("event", requestBody, eventId);
		}
		
		Map<String, Object> eventUpdated = utilsDAO.getEntry("event", eventId);
		return new ResponseEntity(eventUpdated, HttpStatus.OK);
	}
	
}
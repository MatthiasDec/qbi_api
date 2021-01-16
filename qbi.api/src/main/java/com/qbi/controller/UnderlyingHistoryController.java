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

import com.qbi.DAO.UnderlyingHistoryDAO;
import com.qbi.DAO.UtilsDAO;

@RestController
public class UnderlyingHistoryController {

	@Autowired
	private UnderlyingHistoryDAO underlyinghistoryDAO;

	@Autowired
	private UtilsDAO utilsDAO;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/underlyinghistory")
	public ResponseEntity<?> createUnderlyingHistory(@RequestBody(required = false) Map<String, Object> requestBody) {

		int CreatedUnderlyingHistoryId = utilsDAO.createEntry("underlying_history",requestBody); // TODO send back 1 = réussie
		Map<String, Object> createdUnderlyinghistory = underlyinghistoryDAO.getUnderlyingHistorybyID(CreatedUnderlyingHistoryId);

		return new ResponseEntity(createdUnderlyinghistory, HttpStatus.CREATED);
	}

	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/underlyinghistories/{underlyingId}")
	public ResponseEntity<?> getUnderlyingHistories(@PathVariable("underlyingId") int underlyingId){
		
		if(!utilsDAO.isEntryExistring(underlyingId, "underlying")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The underlying " + underlyingId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		List<Map<String, Object>> underlyinghistories = underlyinghistoryDAO.getUnderlyingHistoryFull(underlyingId);
		return new ResponseEntity(underlyinghistories, HttpStatus.OK);	
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping("/underlyinghistory")
    public ResponseEntity<?> getUnderlyingHistoryOnDate(@RequestParam(value="underlyingId",required=true) int underlyingId,
    @RequestParam(value="valuedate",required=true) Date valuedate){
		
		if(!utilsDAO.isEntryExistring(underlyingId, "underlying")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The underlying " + underlyingId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		Map<String, Object> underlyinghistoryondate = underlyinghistoryDAO.getUnderlyingHistoryOnDate(underlyingId,valuedate);
		return new ResponseEntity(underlyinghistoryondate, HttpStatus.OK);	
	}



	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/underlyinghistory/{underlyinghistoryId}")
	public ResponseEntity<?> deleteUnderlyingHistory(@PathVariable("underlyinghistoryId") int underlyinghistoryId){
		if(!utilsDAO.isEntryExistring(underlyinghistoryId, "underlying_history")) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("status", "404");
			error.put("title", "Not Found");
			error.put("details", "The underlying " + underlyinghistoryId + " can't be found");
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
        boolean succes = utilsDAO.deleteEntry("underlying_history",underlyinghistoryId);
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

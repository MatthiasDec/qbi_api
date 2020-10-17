package com.qbi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class QBIUtils {

	public List<String> mapCreateQuery(Map<String, Object> elementToCreate){
		List<String> result = new ArrayList<String>();
		String queryColumns = "";
		String queryValues = ""; 
		
		result.add(queryColumns);
		result.add(queryValues);
		return result;
	}
	
}

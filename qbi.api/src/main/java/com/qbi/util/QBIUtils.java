package com.qbi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
	
	public boolean objectParsableToLong(Object value) {
		
		if (value instanceof Double || value instanceof Integer) {
			return true;
		}
		return false;
	}
	
	public boolean checkIfProperDate(String dateString) throws ParseException {
		boolean res = false;
		
		if(dateString != null && isValidFormat("yyyy-MM-dd", dateString)) {
			Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH).parse(dateString);
			new SimpleDateFormat("yyyy-MM-dd").format(date);
			res = true;
		}
	
		
		return res;
	}
	
    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
        	
        }
        return date != null;
    }
	
}

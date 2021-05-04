package com.qbi.DAO;

import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


/**
CREATE TABLE public.underlying_history
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    underlying_id integer NOT NULL,
    date date NOT NULL,
    value real,
    PRIMARY KEY (id),
    CONSTRAINT underlying_id FOREIGN KEY (underlying_id)
        REFERENCES public.underlying (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID
);

ALTER TABLE public.underlying_history
    OWNER to postgres;

*/


@Repository
public class UnderlyingHistoryDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getUnderlyingHistorybyID(int underlyinghistoryId){
		String query = "SELECT * FROM underlying_history WHERE id = " + underlyinghistoryId + "";	
		Map<String, Object> underlyinghistoryid = jdbcTemplate.queryForMap(query);
		return underlyinghistoryid;
    }
	
	public Map<String, Object> getUnderlyingHistoryOnDate(int underlyingId, Date valuedate){
		String query = "SELECT * FROM underlying_history WHERE underlying_id = " + underlyingId + "AND date = " + valuedate + "";	
		Map<String, Object> underlyinghistoryondate = jdbcTemplate.queryForMap(query);
		return underlyinghistoryondate;
  }
    
  public List<Map<String, Object>> getUnderlyingHistoryFull(int underlyingId){
		String query = "SELECT * FROM underlying_history WHERE underlying_id = " + underlyingId + "ORDER BY date DESC";	
		List<Map<String, Object>> underlyinghistoryfull = jdbcTemplate.queryForList(query);
		return underlyinghistoryfull;
  }
  
  public List<List<Object>> getUnderlyingHistoryBetDate(int underlyingId, Map<String, Date> object){
    String startDateFormated= new SimpleDateFormat("yyyy-MM-dd").format(object.get("startDate"));
    String endDateFormated= new SimpleDateFormat("yyyy-MM-dd").format(object.get("endDate"));
		String query = "SELECT date, value FROM underlying_history WHERE underlying_id = " + underlyingId 
    + " AND date >= '" + startDateFormated.toString() + "' AND date <= '" + endDateFormated.toString() + "' ORDER BY date ASC";	
		List<Map<String, Object>> underlyinghistoryfull = jdbcTemplate.queryForList(query);

    List<List<Object>> result = new ArrayList<List<Object>>();
    for (Map<String, Object> object2 : underlyinghistoryfull) {
      List<Object> tmp = new ArrayList<Object>(object2.values());
      result.add(tmp);
    }

		return result;
	}

  public List<Map<String, Object>> getUnderlyingsHistoryBetDateByProductId(int productId, Map<String, Date> object){
    String startDateFormated= new SimpleDateFormat("yyyy-MM-dd").format(object.get("startDate"));
    String endDateFormated= new SimpleDateFormat("yyyy-MM-dd").format(object.get("endDate"));
		String query = "SELECT underlying_history.underlying_id, underlying_history.date, underlying_history.value FROM underlying_history " 
    + " INNER JOIN link_product_underlying on link_product_underlying.product_id =" + productId + " and link_product_underlying.Underlying_id = underlying_history.underlying_id " 
    + " AND date >= '" + startDateFormated.toString() + "' AND date <= '" + endDateFormated.toString() + "' ORDER BY date ASC";	
		List<Map<String, Object>> underlyinghistoryfull = jdbcTemplate.queryForList(query);
		return underlyinghistoryfull;
	}

	
}

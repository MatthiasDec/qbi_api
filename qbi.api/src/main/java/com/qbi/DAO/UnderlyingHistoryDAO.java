package com.qbi.DAO;

import java.util.List;
import java.util.Map;
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
    
    public List<Map<String, Object>> getUnderlyingHistoryBetDate(int underlyingId, Date beg_date, Date end_date){
		String query = "SELECT * FROM underlying_history WHERE underlying_id = " + underlyingId + "AND date >=" + beg_date + "AND date <=" + end_date + "ORDER BY date DESC";	
		List<Map<String, Object>> underlyinghistoryfull = jdbcTemplate.queryForList(query);
		return underlyinghistoryfull;
	}

	
}

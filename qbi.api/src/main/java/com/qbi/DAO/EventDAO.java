package com.qbi.DAO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
-- Table: public.event

-- DROP TABLE public.event;

CREATE TABLE public.event
(
    id integer NOT NULL DEFAULT nextval('event_id_seq'::regclass),
    product_id integer NOT NULL DEFAULT nextval('event_product_id_seq'::regclass),
    type character varying COLLATE pg_catalog."default" NOT NULL,
    date date NOT NULL,
    CONSTRAINT event_pkey PRIMARY KEY (id),
    CONSTRAINT product_id FOREIGN KEY (product_id)
        REFERENCES public.product (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE public.event
    OWNER to postgres;

*/


@Repository
public class EventDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	public Map<String, Object> getEvent(int eventId){
		String query = "SELECT * FROM event WHERE id = ?";	
		Map<String, Object> event = jdbcTemplate.queryForMap(query, new Object[] {eventId});
		return event;
	}
	

	public Map<String, Object> getNextEvent(int productId){
		
		Date currentDate = new Date();
		String currentDateFormated= new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
		
		String query = "SELECT * FROM event WHERE product_id = ? AND date >= '" + currentDateFormated.toString() + "' ORDER BY date ASC LIMIT 1";	
		Map<String, Object> event = jdbcTemplate.queryForMap(query, new Object[] {productId});
		return event;
	}
	
	
	public List<Map<String, Object>> getEventsByProductId(int productId){
		String query = "SELECT * FROM event WHERE product_id =  " + productId + "";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}
	
}

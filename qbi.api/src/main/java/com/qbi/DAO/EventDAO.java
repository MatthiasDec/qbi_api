package com.qbi.DAO;

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

	public int createEvent(Map<String, Object> event) {
		String query = "INSERT INTO event(id, product_id, type, date) VALUES(DEFAULT, '"
				+ event.get("product_id") + "', '" + event.get("type") + "', '" + event.get("date") + "')";
		
		int eventMap = jdbcTemplate.update(query);
		return eventMap;
	}
	
	public Map<String, Object> getEvent(int eventId){
		String query = "SELECT * FROM event WHERE id = " + eventId + "";	
		Map<String, Object> event = jdbcTemplate.queryForMap(query);
		return event;
	}

	public Map<String, Object> getNextEvent(int productId){
		String query = "SELECT * FROM event WHERE product_id =  " + productId + " ORDER BY date ASC LIMIT 1";	
		Map<String, Object> event = jdbcTemplate.queryForMap(query);
		return event;
	}
	
	public List<Map<String, Object>> getEventsByProductId(int productId){
		String query = "SELECT * FROM event WHERE product_id =  " + productId + "";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	

	public Map<String, Object> patchEvent(int eventId){
		// TODO
		String query = "SELECT * FROM event WHERE id = ? ";
		
		Map<String, Object> event = jdbcTemplate.queryForMap(query, new Object[] {eventId});
		return event;
	}

	public Map<String, Object> deleteEvent(int eventId){
		String query = "DELETE FROM event WHERE id = " + eventId;
		Map<String, Object> event = jdbcTemplate.queryForMap(query);
		return event;
	}
	
}

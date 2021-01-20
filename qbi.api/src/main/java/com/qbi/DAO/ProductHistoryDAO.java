package com.qbi.DAO;

import java.util.List;
import java.util.Map;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


/**
CREATE TABLE public.product_history
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    product_id integer NOT NULL,
    date date NOT NULL,
    bid real,
	ask real
    PRIMARY KEY (id),
    CONSTRAINT underlying_id FOREIGN KEY (product_id)
        REFERENCES public.product (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID
);


*/


@Repository
public class ProductHistoryDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getProductHistorybyID(int producthistoryId){
		String query = "SELECT * FROM product_history WHERE id = " + producthistoryId + "";	
		Map<String, Object> producthistoryid = jdbcTemplate.queryForMap(query);
		return producthistoryid;
    }
	
	public Map<String, Object> getProductHistoryOnDate(int productId, Date valuedate){
		String query = "SELECT * FROM product_history WHERE product_id = " + productId + "AND date = " + valuedate + "";	
		Map<String, Object> producthistoryondate = jdbcTemplate.queryForMap(query);
		return producthistoryondate;
    }
    
    public List<Map<String, Object>> getProductHistoryFull(int productId){
		String query = "SELECT * FROM product_history WHERE product_id = " + productId + "ORDER BY date DESC";	
		List<Map<String, Object>> producthistoryfull = jdbcTemplate.queryForList(query);
		return producthistoryfull;
	}

	
}

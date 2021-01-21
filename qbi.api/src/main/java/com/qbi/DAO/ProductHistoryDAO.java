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
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    product_id integer NOT NULL,
    date date NOT NULL,
    bid real,
	ask real,
    CONSTRAINT product_history_pkey PRIMARY KEY (id),
    CONSTRAINT product_id FOREIGN KEY (product_id)
        REFERENCES public.product (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
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
		String query = "SELECT * FROM product_history WHERE product_id = ? AND date = '" + valuedate.toString() + "'";	
		Map<String, Object> producthistoryondate = jdbcTemplate.queryForMap(query, new Object[] {productId});
		return producthistoryondate;
    }
    
    public List<Map<String, Object>> getProductHistoryFull(int productId){
		String query = "SELECT * FROM product_history WHERE product_id = " + productId + "ORDER BY date DESC";	
		List<Map<String, Object>> producthistoryfull = jdbcTemplate.queryForList(query);
		return producthistoryfull;
    }
    
    public List<Map<String, Object>> getProductHistoryBetdate(int productid, Date beg_date, Date end_date){
		String query = "SELECT * FROM product_history WHERE product_id = " + productid + " AND date >= " + beg_date + " AND date <= " + end_date + " ORDER BY date DESC";	
		List<Map<String, Object>> producthistoryfull = jdbcTemplate.queryForList(query);
		return producthistoryfull;
	}

	
}

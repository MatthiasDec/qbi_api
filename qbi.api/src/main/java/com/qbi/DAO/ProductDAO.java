package com.qbi.DAO;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<Map<String, Object>> getProductsByUserId(int userId){
		String query = "";
		
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}
	
}

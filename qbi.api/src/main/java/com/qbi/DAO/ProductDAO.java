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
	
	public Map<String, Object> getProduct(int productId){
		String query = "SELECT * FROM product WHERE id = ? ";
		
		Map<String, Object> product = jdbcTemplate.queryForMap(query, new Object[] {productId});
		return product;
	}
	
	public Map<String, Object> createProduct(Map<String, Object> product){
		//TODO
		return null;
	}
	
	public List<Map<String, Object>> getProductsByUserId(int userId){
		String query = "SELECT * FROM product JOIN TABLE  ";
		
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}
	
	public void linkProductAndUser(int userId, int productId){
		String query = "INSERT INTO link_user_product(product_id, user_id) VALUES(? , ?)";
		
		jdbcTemplate.update(query, new Object[] {productId, userId});
	}
	
	public boolean checkIfRelationExists(int userId, int productId) {
		String query = "SELECT COUNT(*) FROM link_user_product WHERE product_id = ? AND user_id = ?";
		
		int count = jdbcTemplate.queryForObject(query, new Object[] {productId, userId}, Integer.class);
		return (count>0);
	}
	
}

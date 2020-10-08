package com.qbi.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.qbi.model.User;

@Repository
public class UserDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public User findUserByUsername(String username) {
		String query = "SELECT * FROM app_user WHERE username = '" + username + "'";
		
		User foundUser = jdbcTemplate.queryForObject(query, (rs, rowNum) ->
		new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getBoolean("active"),
                rs.getString("role")));
		
		return foundUser;
	}
	
	public int count() {
		String query = "SELECT COUNT(*) FROM company";
		int count = jdbcTemplate.queryForObject(query, Integer.class);
		return count;
	}
	
}
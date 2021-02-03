package com.qbi.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

	public List<Map<String, Object>> getAllClients() {
		String query = "SELECT id, username FROM app_user WHERE role = 'client' AND active=true";
		
		List<Map<String, Object>> foundUser = jdbcTemplate.queryForList(query);
		return foundUser;
	}

	public List<Map<String, Object>> getAllUsers() {
		String query = "SELECT id, username FROM app_user";
		
		List<Map<String, Object>> foundUser = jdbcTemplate.queryForList(query);
		return foundUser;
	}
}

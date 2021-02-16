package com.qbi.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qbi.model.RoleEnum;
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

	public List<Map<String, Object>> getUsersByCompany(int companyId){
		String query = "SELECT * FROM app_user JOIN link_user_company as link ON app_user.id = link.user_id WHERE link.company_id = ?";
		
		List<Map<String, Object>> result = jdbcTemplate.query(query, new Object[] {companyId}, 
				
				new RowMapper<Map<String, Object>>() {

            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNumber) throws SQLException {
            	Map<String, Object> user = new HashMap<>();
            	user.put("id", rs.getInt("id"));
            	user.put("username", rs.getString("username"));
            	user.put("role", RoleEnum.valueOf(rs.getString("role").toUpperCase()).getRoleNumber());
            	user.put("active", rs.getBoolean("active"));
                return user;
            }
        });
		return result;
	}
}

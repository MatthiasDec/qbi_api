package com.qbi.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UtilsDAO {


	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public boolean isEntryExistring(int entryId, String tableName) {
		String query = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ? ";
		int count = jdbcTemplate.queryForObject(query, new Object[] {entryId}, Integer.class);
		if(count>0) {
			return true;
		}
		else {
			return false;
		}
	}
}

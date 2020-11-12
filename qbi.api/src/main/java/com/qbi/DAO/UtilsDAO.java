package com.qbi.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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

	public List<Map<String, String>> getBasesStructure(String tableName){
		String query = "SELECT * FROM " + tableName + " LIMIT 1";
		List<Map<String, String>> result = jdbcTemplate.query(query, new ResultSetExtractor<List<Map<String, String>>>() {
			
			@Override
			public List<Map<String, String>> extractData(ResultSet rs) throws SQLException {				
				ResultSetMetaData rsmd = rs.getMetaData();
				
				int count = rsmd.getColumnCount();
				List<Map<String, String>> columnsLists = new ArrayList<>();
				
				for (int i=1; i< count + 1; i++) {
					Map<String, String> columnInfo = new HashMap<String, String>();
					columnInfo.put("column_name", rsmd.getColumnName(i));
					columnInfo.put("column_type", rsmd.getColumnTypeName(i));
					columnsLists.add(columnInfo);
				}
			return columnsLists;
			}
		});
		
		return result;
	}
	
	public List<String> getColumnsTable(String tableName){
		String query = "SELECT * FROM " + tableName + " LIMIT 1";
		List<String> result = jdbcTemplate.query(query, new ResultSetExtractor<List<String>>() {
			
			@Override
			public List< String> extractData(ResultSet rs) throws SQLException {				
				ResultSetMetaData rsmd = rs.getMetaData();
				
				int count = rsmd.getColumnCount();
				List<String> columnsLists = new ArrayList<>();
				
				for (int i=1; i< count + 1; i++) {
					columnsLists.add(rsmd.getColumnName(i));
				}
			return columnsLists;
			}
		});
		
		return result;
	}
	
	public int createEntry(String tableName, Map<String, Object> object) {
		
		List<Map<String, String>> tableInfos = getBasesStructure(tableName);
		
		String query = "INSERT INTO " + tableName + " ";
		String queryParams = "(";
		StringBuffer queryValues = new StringBuffer("");
		
		for (Map<String, String> tableInfo: tableInfos) {
			String columnName = tableInfo.get("column_name");
			String columnType = tableInfo.get("column_type");
			if(!columnName.equalsIgnoreCase("id")) {
				queryParams += columnName + ",";
			}
			Object objectValue = object.get(columnName);
			if (columnName.equalsIgnoreCase("id")) {
				// DO NOTHING
			}
			else if (columnName.equals("created_on") || columnName.equals("modified_on")) {
				Long currentTimeStamp = System.currentTimeMillis() / 1000;
				queryValues.append(currentTimeStamp + ", ");
			}
			else if(objectValue != null) {
				// redo switch
				queryValues.append("'" + objectValue.toString() + "', ");
			}
			else {
				switch(columnType) {
				case("serial"):
					queryValues.append("'', ");
					break;
				case("date"):
					queryValues.append("'', ");
					break;
				case("varchar"):
					queryValues.append("'', ");
					break;
				case("integer"):
					queryValues.append("0, ");
					break;
				case("int4"):
					queryValues.append("0, ");
					break;
				case("float4"):
					queryValues.append("0, ");
					break;
				case("numeric"):
					queryValues.append("0, ");
					break;
				case("text"):
					queryValues.append("'', ");
					break;
				case("bool"):
					queryValues.append("false, ");
					break;
				default:
					System.out.println("Type " + columnType + " not supported");
				
				}
				
			}
			
		}
		queryParams = queryParams.substring(0, queryParams.length() - 1) + ") VALUES(";
		
		String queryFinal = query + queryParams + queryValues.substring(0, queryValues.length() - 2) + ")";		
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		    new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps =
		                connection.prepareStatement(queryFinal, Statement.RETURN_GENERATED_KEYS);
		            return ps;
		        }
		    },
		    keyHolder);
		
		return (int) keyHolder.getKeys().get("id");
	}

	public boolean deleteEntry(String tableName, int entryId) {
		String query = "DELETE FROM " + tableName + " WHERE id = ?";
		
		return jdbcTemplate.update(query, new Object[] {entryId}) == 1;
	}
	
	public void updateEntry(String tableName, Map<String, Object> object, int entryId) {
		String query = "UPDATE " + tableName + " SET ";
		
		List<String> columnsTable = getColumnsTable(tableName);
		for (Map.Entry<String, Object> entry : object.entrySet()) {
			String columnName = entry.getKey();
			if(columnsTable.contains(columnName)) {
				if(columnName.equalsIgnoreCase("id"));
				else if(columnName.equalsIgnoreCase("modified_on")) {
					Long currentTimeStamp = System.currentTimeMillis() / 1000;
					query += "modified_on=" + currentTimeStamp + ", ";
				}
				else {
					query += columnName + "='" + entry.getValue().toString() + "', ";
				}
			}
			
		}
		query = query.substring(0, query.length() - 2);
		query += " WHERE id=?";
		jdbcTemplate.update(query, new Object[] {entryId});
	}
	
	public Map<String, Object> getEntry(String tableName, int entryId){
		String query = "SELECT * FROM " + tableName + " WHERE id = ?";
		Map<String, Object> entry = jdbcTemplate.queryForMap(query, new Object[] {entryId});
		return entry;
	}
	
}

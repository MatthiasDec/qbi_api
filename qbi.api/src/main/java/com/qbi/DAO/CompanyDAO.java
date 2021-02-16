package com.qbi.DAO;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<Map<String, Object>> getCompaniesByUserId(int userId){
	String query = "SELECT * FROM company JOIN link_user_company as link ON company.id = link.company_id WHERE link.user_id = ?";
	
	List<Map<String, Object>> result = jdbcTemplate.queryForList(query, new Object[] {userId});
	return result;
	}
	
	// QUERY ONLY FOR ADMINS
	public List<Map<String, Object>> getAllCompanies(){
		String query = "SELECT * FROM company";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}
	
	public Map<String, Object> getCompanyById(int companyId){
		String query = "SELECT * FROM company WHERE id = ?";
		
		Map<String, Object> result = jdbcTemplate.queryForMap(query, new Object[] {companyId});
		return result;
	}
	
	public void linkCompanyAndUser(int companyId, int userId) {
		String query = "INSERT INTO link_user_company(company_id, user_id) VALUES(? , ?)";
		
		jdbcTemplate.update(query, new Object[] {companyId, userId});
	}
	
	public boolean unlinkCompanyAndUser(int userId, int companyId) {
		String query = "DELETE FROM  link_user_company WHERE user_id = ? AND company_id = ?";
		return jdbcTemplate.update(query, new Object[] {userId, companyId}) == 1;
	}
	
	public boolean checkIfRelationExists(int userId, int companyId) {
		String query = "SELECT COUNT(*) FROM link_user_company WHERE company_id = ? AND user_id = ?";
		
		int count = jdbcTemplate.queryForObject(query, new Object[] {companyId, userId}, Integer.class);
		return (count>0);
	}
	
}

package com.qbi.DAO;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
CREATE TABLE public.issuer
(
    id serial NOT NULL,
    name character varying NOT NULL,
    logo bytea,
    "Moody_rating" character varying,
    "S_and_P_rating" character varying,
    "Fitch_rating" character varying,
    PRIMARY KEY (id)
);

ALTER TABLE public.issuer
	OWNER to postgres;
	
*/


@Repository
public class IssuerDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int createIssuer(Map<String, Object> issuer) {
		String query = "INSERT INTO issuer(id, name, logo, \"Moody_rating\", \"S_and_P_rating\", \"Fitch_rating\") VALUES(DEFAULT, '"
				+ issuer.get("name") + "', " + issuer.get("logo") + ", '" + issuer.get("Moody_rating")
				+ "', '" + issuer.get("S_and_P_rating") + "', '" + issuer.get("Fitch_rating")  + "')";
		
		int affected_rows = jdbcTemplate.update(query);
		return affected_rows;
	}
	
	public Map<String, Object> getIssuer(int issuerId){
		String query = "SELECT * FROM issuer WHERE id = " + issuerId + "";	
		Map<String, Object> issuer = jdbcTemplate.queryForMap(query);
		return issuer;
	}
	
	public Map<String, Object> getIssuerByProductId(int productId){
		String query = "SELECT issuer.id, issuer.name, issuer.logo, issuer.\"Moody_rating\" as \"MoodyRating\", issuer.\"S_and_P_rating\" as \"SandPRating\", issuer.\"Fitch_rating\" as \"FitchRating\" FROM issuer INNER JOIN product on product.id =  " 
				+ productId + " and product.issuer_id = issuer.id";
		Map<String, Object> result = jdbcTemplate.queryForMap(query);
		return result;
	}

	public Map<String, Object> patchIssuer(int issuerId){
		// TODO
		String query = "SELECT * FROM issuer WHERE id = ? ";
		
		Map<String, Object> issuer = jdbcTemplate.queryForMap(query, new Object[] {issuerId});
		return issuer;
	}

	public Map<String, Object> deleteIssuer(int issuerId){
		String query = "DELETE FROM issuer WHERE id = " + issuerId;
		Map<String, Object> issuer = jdbcTemplate.queryForMap(query);
		return issuer;
	}
	
}

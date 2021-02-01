package com.qbi.DAO;

import java.util.List;
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

	public Map<String, Object> getIssuer(int issuerId){
		String query = "SELECT * FROM issuer WHERE id = " + issuerId + "";	
		Map<String, Object> issuer = jdbcTemplate.queryForMap(query);
		return issuer;
	}

	public List<Map<String, Object>> getIssuers(){
		String query = "SELECT id, name FROM issuer";	
		List<Map<String, Object>> issuerList = jdbcTemplate.queryForList(query);
		return issuerList;
	}
	
	public Map<String, Object> getIssuerByProductId(int productId){
		String query = "SELECT * FROM issuer INNER JOIN product on product.id = ? and product.issuer_id = issuer.id";
		Map<String, Object> result = jdbcTemplate.queryForMap(query, new Object[] {productId});
		return result;
	}

}

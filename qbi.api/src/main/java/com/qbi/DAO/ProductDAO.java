package com.qbi.DAO;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


/**
-- Table: public.product

-- DROP TABLE public.product;

CREATE TABLE public.product
(
    isin text COLLATE pg_catalog."default" NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    maturity_date integer NOT NULL,
    final_valuation_date integer NOT NULL,
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    initial_valuation_date integer NOT NULL,
    issue_date integer NOT NULL,
    issuer_id integer NOT NULL,
    autocall_level integer NOT NULL,
    coupon_level integer NOT NULL,
    barrier_level integer NOT NULL,
    currency text COLLATE pg_catalog."default" NOT NULL,
    potential_coupon text COLLATE pg_catalog."default" NOT NULL,
    frequency text COLLATE pg_catalog."default" NOT NULL,
    memory_effect boolean NOT NULL,
    created_on integer NOT NULL,
    CONSTRAINT product_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.product
    OWNER to postgres;
    
INSERT INTO public.product OVERRIDING SYSTEM VALUE VALUES ('', 'product 1', 0, 0, 1, 0, 0, 0, 0, 0, 0, 'EUR', '', '', true, 0);
INSERT INTO public.product OVERRIDING SYSTEM VALUE VALUES ('', 'product 2', 0, 0, 2, 0, 0, 0, 0, 0, 0, 'EUR', '', '', true, 0);
INSERT INTO public.product OVERRIDING SYSTEM VALUE VALUES ('', 'product 3', 0, 0, 3, 0, 0, 0, 0, 0, 0, 'EUR', '', '', true, 0);
INSERT INTO public.product OVERRIDING SYSTEM VALUE VALUES ('', 'product 4', 0, 0, 4, 0, 0, 0, 0, 0, 0, 'EUR', '', '', true, 0);
INSERT INTO public.product OVERRIDING SYSTEM VALUE VALUES ('', 'product 5', 0, 0, 5, 0, 0, 0, 0, 0, 0, 'EUR', '', '', true, 0);
INSERT INTO public.product OVERRIDING SYSTEM VALUE VALUES ('', 'product 6', 0, 0, 6, 0, 0, 0, 0, 0, 0, 'EUR', '', '', true, 0);
INSERT INTO public.product OVERRIDING SYSTEM VALUE VALUES ('', 'product 7', 0, 0, 7, 0, 0, 0, 0, 0, 0, 'EUR', '', '', true, 0);
INSERT INTO public.product OVERRIDING SYSTEM VALUE VALUES ('', 'product nouveau', 0, 0, 8, 0, 0, 0, 0, 0, 0, '', '', '', false, 0);
INSERT INTO public.product OVERRIDING SYSTEM VALUE VALUES ('', 'product nouveau', 0, 0, 9, 0, 0, 0, 0, 0, 0, '', '', '', false, 1604867542);
INSERT INTO public.product OVERRIDING SYSTEM VALUE VALUES ('', 'product nouveau 2', 0, 0, 10, 0, 0, 0, 0, 0, 0, '', '', '', false, 1604867562);
INSERT INTO public.product OVERRIDING SYSTEM VALUE VALUES ('', 'product nouveau 2', 0, 0, 11, 0, 0, 0, 0, 0, 0, '', '', '', false, 1604867874);
INSERT INTO public.product OVERRIDING SYSTEM VALUE VALUES ('', 'product nouveau 2', 0, 0, 12, 0, 0, 0, 0, 0, 0, '', '', '', false, 1604867934);
INSERT INTO public.product OVERRIDING SYSTEM VALUE VALUES ('', 'product nouveau 2', 0, 0, 13, 0, 0, 0, 0, 0, 0, '', '', '', false, 1604867997);
INSERT INTO public.product OVERRIDING SYSTEM VALUE VALUES ('', 'product nouveau 2', 0, 0, 14, 0, 0, 0, 0, 0, 0, '', '', '', false, 1604868001);
INSERT INTO public.product OVERRIDING SYSTEM VALUE VALUES ('', 'product nouveau 2', 0, 0, 15, 0, 0, 0, 0, 0, 0, '', '', '', false, 1604868024);
INSERT INTO public.product OVERRIDING SYSTEM VALUE VALUES ('', 'product nouveau 2', 0, 0, 17, 0, 0, 0, 0, 0, 0, '', '', '', false, 1604868052);
INSERT INTO public.product OVERRIDING SYSTEM VALUE VALUES ('', 'it works !', 0, 0, 16, 0, 0, 15, 0, 0, 14, 'LIVRE STERLING', '', '', false, 1604868025);


 */

@Repository
public class ProductDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public Map<String, Object> getProduct(int productId){
		String query = "SELECT * FROM product WHERE id = ? ";
		
		Map<String, Object> product = jdbcTemplate.queryForMap(query, new Object[] {productId});
		return product;
	}
	
	public List<Map<String, Object>> getProductsByUserId(int userId){
		String query = "SELECT * FROM product JOIN link_user_product as link ON product.id = link.product_id WHERE link.user_id = ?";
		
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query, new Object[] {userId});
		return result;
	}
	
	public void linkProductAndUser(int userId, int productId, long position){
		String query = "INSERT INTO link_user_product(product_id, user_id, position) VALUES(? , ?, ?)";
		
		jdbcTemplate.update(query, new Object[] {productId, userId, position});
	}
	
	public boolean unlinkProductAndUser(int userId, int productId) {
		String query = "DELETE FROM  link_user_product WHERE user_id = ? AND product_id = ?";
		return jdbcTemplate.update(query, new Object[] {userId, productId}) == 1;
	}
	
	public boolean checkIfRelationExists(int userId, int productId) {
		String query = "SELECT COUNT(*) FROM link_user_product WHERE product_id = ? AND user_id = ?";
		
		int count = jdbcTemplate.queryForObject(query, new Object[] {productId, userId}, Integer.class);
		return (count>0);
	}

	public List<Map<String, Object>> countIssuers() {
		String query = "SELECT issuer.name, COUNT(*) as count FROM product " +
		"JOIN issuer ON product.issuer_id = issuer.id " + 
		"GROUP BY issuer.name ORDER BY count DESC";
		
		return jdbcTemplate.queryForList(query);
	}
	
}

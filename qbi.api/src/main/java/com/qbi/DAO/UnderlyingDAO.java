package com.qbi.DAO;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
-- Table: public.underlying

-- DROP TABLE public.underlying;

CREATE TABLE public.underlying
(
    id integer NOT NULL DEFAULT nextval('underlying_id_seq'::regclass),
    name character varying COLLATE pg_catalog."default" NOT NULL,
    ticker character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT underlying_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.underlying
	OWNER to postgres;
	
CREATE TABLE public.link_product_underlying
(
    product_id serial NOT NULL,
    underlying_id serial NOT NULL,
    fixing_level integer NOT NULL,
    CONSTRAINT product_id FOREIGN KEY (product_id)
        REFERENCES None (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
        NOT VALID,
    CONSTRAINT underlying_id FOREIGN KEY (underlying_id)
        REFERENCES None (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
        NOT VALID
);

ALTER TABLE public.link_product_underlying
    OWNER to postgres;



New 06/11/2020
ALTER TABLE public.underlying
	ADD COLUMN current_value real NOT NULL,;

UPDATE public.underlying SET
current_value = '10549.31'::real WHERE
id = 1;
UPDATE public.underlying SET
current_value = '4990.50'::real WHERE
id = 2;
UPDATE public.underlying SET
current_value = '2290.48'::real WHERE
id = 3;

ALTER TABLE public.link_product_underlying
    RENAME fixing_level TO fixing_value;
*/


@Repository
public class UnderlyingDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int createUnderlying(Map<String, Object> underlying) {
		String query = "INSERT INTO underlying(id, name, ticker, current_value) VALUES(DEFAULT, '"
				+ underlying.get("name") + "', '" + underlying.get("ticker") + "', " + underlying.get("current_value") + ")";
		
		int UnderlyingMap = jdbcTemplate.update(query);
		return UnderlyingMap;
	}
	
	public Map<String, Object> getUnderlying(int underlyingId){
		String query = "SELECT * FROM underlying WHERE id = " + underlyingId + "";	
		Map<String, Object> underlying = jdbcTemplate.queryForMap(query);
		return underlying;
	}
	
	public List<Map<String, Object>> getUnderlyingsByProductId(int productId){
		String query = "SELECT underlying.id, underlying.name, underlying.ticker, link_product_underlying.fixing_value as \"fixingValue\", underlying.current_value as \"currentValue\" FROM underlying " +
				"INNER JOIN link_product_underlying on link_product_underlying.product_id =  " 
				+ productId + " and link_product_underlying.Underlying_id = Underlying.id";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	public int createlinkUnderlyingProduct(Map<String, Object> couple) {
		String query = "INSERT INTO link_product_underlying(product_id, underlying_id, fixing_value) VALUES('"
				+ couple.get("product_id") + "', '" + couple.get("underlying_id") + "', " + couple.get("fixing_value") + ")";
		
		int UnderlyingMap = jdbcTemplate.update(query);
		return UnderlyingMap;
	}

	public Map<String, Object> patchUnderlying(int UnderlyingId){
		// TODO
		String query = "SELECT * FROM underlying WHERE id = ? ";
		
		Map<String, Object> Underlying = jdbcTemplate.queryForMap(query, new Object[] {UnderlyingId});
		return Underlying;
	}

	public Map<String, Object> deleteUnderlying(int UnderlyingId){
		String query = "DELETE FROM underlying WHERE id = " + UnderlyingId;
		Map<String, Object> Underlying = jdbcTemplate.queryForMap(query);
		return Underlying;
	}
	
}
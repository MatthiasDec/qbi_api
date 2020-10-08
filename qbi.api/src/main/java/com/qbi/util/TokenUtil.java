package com.qbi.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.qbi.config.AppConfiguration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenUtil {

	private String ENCODE_KEY = AppConfiguration.ENCODE_KEY;
	private int EXPIRE_TIME = AppConfiguration.EXPIRE_TIME;
	
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}
	
	private String createToken(Map<String, Object> claims, String username) {
		return Jwts.builder().setClaims(claims).setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
				.signWith(SignatureAlgorithm.HS256, ENCODE_KEY)
				.compact();
	}
	
	public <T> T extractClaim(String token, Function<Claims, T>claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);	
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(ENCODE_KEY).parseClaimsJws(token).getBody();
	}
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public Boolean valideToken(String token, UserDetails userDetail) {
		final String username = extractUsername(token);
		return (username.equals(userDetail.getUsername()) && !isTokenExpired(token));
	}
	
}

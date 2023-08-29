package com.xantrix.webapp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xantrix.webapp.exception.JwtTokenMalformedException;
import com.xantrix.webapp.exception.JwtTokenMissingException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;


@Component
public class JwtUtil 
{
	@Value("${sicurezza.secret}")
	private String jwtSecret;
	
	public Claims getClaims(final String token) 
	{
		try 
		{
			Claims body = Jwts
					.parser()
					.setSigningKey(jwtSecret.getBytes())
					.parseClaimsJws(token)
					.getBody();
			
			return body;
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage() + " => " + e);
		}
		return null;
	}
	
	public List<String> getAuthFromToken(String token)
	{
		List<String> retVal = new ArrayList<String>();
		
		Claims claims  = this.getAllClaimsFromToken(token);
		String[] authorities = String.valueOf(claims.get("authorities")).replace("[", "").replace("]", "").split(",");
		
		for (String authority : authorities)
		{
			retVal.add(authority.trim());
		}
		
		return retVal;
	}
	
	public String getUsernameFromToken(String token) 
	{
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) 
	{
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) 
	{
		return Jwts
				.parser()
				.setSigningKey(jwtSecret.getBytes())
				.parseClaimsJws(token)
				.getBody();
	}

	public void validateToken(final String token) 
			throws JwtTokenMalformedException, JwtTokenMissingException 
	{
		
		try 
		{
			Jwts
				.parser()
				.setSigningKey(jwtSecret.getBytes())
				.parseClaimsJws(token);
		} 
		catch (SignatureException ex) 
		{
			throw new JwtTokenMalformedException("Firma JWT Invalida");
		} 
		catch (MalformedJwtException ex) 
		{
			throw new JwtTokenMalformedException("Token JWT Invalido ");
		} 
		catch (ExpiredJwtException ex) 
		{
			throw new JwtTokenMalformedException("Token JWT scaduto");
		} 
		catch (UnsupportedJwtException ex) 
		{
			throw new JwtTokenMalformedException("Unsupported JWT token");
		} 
		catch (IllegalArgumentException ex) 
		{
			throw new JwtTokenMissingException("JWT claims string is empty.");
		}
	}
}

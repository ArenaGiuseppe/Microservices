package com.xantrix.webapp.filter;


import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.xantrix.webapp.exception.JwtTokenMalformedException;
import com.xantrix.webapp.exception.JwtTokenMissingException;
import com.xantrix.webapp.util.JwtUtil;
import lombok.extern.java.Log;

import reactor.core.publisher.Mono;

@Component
@Log
public class JwtAuthenticationFilter implements GatewayFilter 
{

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) 
	{
		ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();

		final List<String> adminEndpoints = List.of("/api/prezzi/elimina", "/api/listino/inserisci", "/api/listino/elimina",
				"/api/articoli/inserisci","/api/articoli/modifica","/api/articoli/elimina", "api/promo/inserisci", "api/promo/elimina");
		
		Predicate<ServerHttpRequest> isAdminEndPoints = r -> adminEndpoints.stream()
				.noneMatch(uri -> r.getURI().getPath().contains(uri));
		
		boolean isAdmin = !isAdminEndPoints.test(request);
		
		if (!request.getHeaders().containsKey("Authorization")) 
		{
			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.UNAUTHORIZED);

			return response.setComplete();
		}

		final String token = request.getHeaders().getOrEmpty("Authorization").get(0).replace("Bearer ", "");

		try 
		{
			jwtUtil.validateToken(token);
		} 
		catch (JwtTokenMalformedException | JwtTokenMissingException e) 
		{
			// e.printStackTrace();

			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.BAD_REQUEST);

			return response.setComplete();
		}

		String user = jwtUtil.getUsernameFromToken(token);
		log.info(user);
		
		List<String> authorities = jwtUtil.getAuthFromToken(token);
		
		if (isAdmin && !authorities.contains("ROLE_ADMIN"))
		{
			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.FORBIDDEN);

			return response.setComplete();
		}
		
		
		/*
		exchange.getRequest().mutate().header("id", String.valueOf(claims.get("id"))).build();
		*/
		

		return chain.filter(exchange);
	}
	
	

}

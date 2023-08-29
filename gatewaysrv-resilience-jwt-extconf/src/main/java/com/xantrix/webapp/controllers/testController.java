package com.xantrix.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.xantrix.webapp.exception.JwtTokenMalformedException;
import com.xantrix.webapp.exception.JwtTokenMissingException;
import com.xantrix.webapp.util.JwtUtil;

import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("test")
@Log
public class testController 
{
	@Autowired
	private JwtUtil jwtUtil;
	
	@GetMapping(value = "/auth")
	public  Mono<String> testAuth(ServerWebExchange exchange)
	{
		ServerHttpResponse response = exchange.getResponse();
		ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        
        if(headers.get("Authorization") == null) 
        {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);

			return Mono.just("Token assente");
        }
        else
        {
        	final String token = headers.get("Authorization").get(0).replace("Bearer ", "");
        	
        	try 
    		{
    			jwtUtil.validateToken(token);
    		} 
    		catch (JwtTokenMalformedException | JwtTokenMissingException e) 
    		{
    			log.warning(e.getMessage());

    			response.setStatusCode(HttpStatus.BAD_REQUEST);

    			return Mono.just("Token errato o scaduto");
    		}
        }
        
		return Mono.just("Test Connessione Ok");
	}
}

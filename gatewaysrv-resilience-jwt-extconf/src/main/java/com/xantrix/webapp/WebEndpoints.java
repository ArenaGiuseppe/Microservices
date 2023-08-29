package com.xantrix.webapp;

import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class WebEndpoints 
{
	@Bean
	public RouterFunction<ServerResponse> routerFunction() 
	{
	    return RouterFunctions.route()
	      .GET("/articolo-fallback", request ->
	        ServerResponse.ok().body(Mono.just("fallback articolo attivato"), String.class))
	      .GET("/prezzo-fallback", request ->
	      	ServerResponse.ok().body(Mono.just("fallback prezzo attivato"), String.class))
	      .GET("/prezzo-promo-fallback", request ->
	      	ServerResponse.ok().body(Mono.just("0"), String.class))
	      .build();
	}
	
	/*
	public Flux<Double> findPrezzoPromo()
	{
		WebClient webClient = WebClient.create("http://localhost:3000");
		
		return webClient.get()
			.uri("/employees")
			.retrieve()
			.bodyToFlux(Double.class);
	}
	*/
}

package com.xantrix.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xantrix.webapp.filter.JwtAuthenticationFilter;

import static org.springframework.cloud.gateway.support.RouteMetadataUtils.CONNECT_TIMEOUT_ATTR;
import static org.springframework.cloud.gateway.support.RouteMetadataUtils.RESPONSE_TIMEOUT_ATTR;


@Configuration
public class GatewayConfig 
{
	@Autowired
	private JwtAuthenticationFilter jwtAuthFilter;

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) 
	{
		return builder
				.routes()
					//ARTICOLI
					.route("articoliModule", r -> r
							.path("/api/articoli/**")
								//.and().method("GET")
							.filters(f -> f
								.circuitBreaker(config -> config
					                .setName("articoliCircuitBreaker")
					                .setFallbackUri("forward:/articolo-fallback"))
								.filter(jwtAuthFilter))
							.uri("lb://ArticoliWebService"))
					//PREZZI
					.route("prezziModule", r -> r
							.path("/api/prezzi/**")
								//.and().method("GET,POST,DELETE")
							.filters(f -> f.filter(jwtAuthFilter))
							.uri("lb://PriceArtService"))
					//LISTINI
					.route("listinoModule", r -> r
							.path("/api/listino/**")
								//.and().method("GET")
							.filters(f -> f.filter(jwtAuthFilter))
							.uri("lb://PriceArtService"))
					//PROMO
					.route("promoModule", r -> r
							.path("/api/promo/**")
								//.and().method("GET")
							.filters(f -> f.filter(jwtAuthFilter))
							.uri("lb://PromoWebService"))
					//PREZZI PROMO
					.route("prezziPromoModule", r -> r
							.path("api/promo/prezzo/{codart}")
								//.and().method("GET")
							.filters(f -> f
								.circuitBreaker(config -> config
					                .setName("prezziPromoCircuitBreaker")
					                .setFallbackUri("forward:/prezzo-promo-fallback"))
								.filter(jwtAuthFilter))
							.uri("lb://PromoWebService"))
					//AUTH SERVER JWT
					.route("authJwtModule", r -> r
							.path("/auth","/refresh")
								.and().method("POST","GET")
							//.filters(f -> f.filter(jwtAuthFilter))
							.uri("lb://AuthServerJwt"))
					.build();
								
	}
}

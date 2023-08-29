package com.xantrix.webapp.config;

import reactor.core.publisher.Mono;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig 
{
	@Bean
	KeyResolver userKeyResolver() {
	    return exchange -> Mono.just("1");
	}
}

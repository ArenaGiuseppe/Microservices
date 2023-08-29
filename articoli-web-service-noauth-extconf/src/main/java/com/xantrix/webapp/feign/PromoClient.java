package com.xantrix.webapp.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "PromoWebService", configuration = OpenFeignConfig.class)
public interface PromoClient 
{
	@GetMapping(value = "/api/promo/prezzo/{codart}")
	public double getPromoPrice(@RequestHeader("Authorization") String AuthHeader, 
			@PathVariable("codart") String CodArt);
}

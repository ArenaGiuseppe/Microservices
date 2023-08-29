package com.xantrix.webapp.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.xantrix.webapp.dtos.ArticoliDto;

@FeignClient(name ="ArticoliWebService", configuration = OpenFeignConfig.class) 
public interface ArticoliClient 
{
	@GetMapping(value = "api/articoli/cerca/codice/{codart}")
    public ArticoliDto getArticolo(@RequestHeader("Authorization") String AuthHeader, 
    		@PathVariable("codart") String CodArt);
	
}

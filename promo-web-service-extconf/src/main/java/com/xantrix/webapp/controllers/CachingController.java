package com.xantrix.webapp.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xantrix.webapp.dtos.InfoMsg;
import com.xantrix.webapp.services.PromoService;

@RestController
@RequestMapping(value = "api/promo")
public class CachingController 
{
	@Autowired
	private PromoService promoService;
	
	@GetMapping(value = "clearAllCaches", produces = "application/json")
	public ResponseEntity<InfoMsg> clearAllCaches() 
	{
		this.promoService.CleanCaches();
		
		return new ResponseEntity<InfoMsg>(new InfoMsg(LocalDate.now(), 
				"Eliminazione della cache eseguita con successo!"), HttpStatus.OK);
	}
	
}


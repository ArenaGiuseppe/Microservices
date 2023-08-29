package com.xantrix.webapp.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xantrix.webapp.dtos.InfoMsg;
import com.xantrix.webapp.services.ArticoliService;

@RestController
@RequestMapping(value = "api/articoli")
public class CachingController 
{
	@Autowired
	private ArticoliService articoliService;
	
	@GetMapping(value = "clearAllCaches", produces = "application/json")
	public ResponseEntity<InfoMsg> clearAllCaches() 
	{
		this.articoliService.CleanCaches();
		
		return new ResponseEntity<InfoMsg>(new InfoMsg(LocalDate.now(), 
				"Eliminazione della cache eseguita con successo!"), HttpStatus.OK);
	}
	
}

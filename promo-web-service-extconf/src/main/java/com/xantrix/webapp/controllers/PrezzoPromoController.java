package com.xantrix.webapp.controllers;

 
import java.util.Optional;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xantrix.webapp.services.PrezzoPromoService;

import lombok.extern.java.Log;

 
@RestController
@RequestMapping(value = "api/promo/prezzo")
@Log
public class PrezzoPromoController 
{
	@Autowired
	private PrezzoPromoService promoService;
	
	// ------------------- SELEZIONE PREZZO PROMO ------------------------------------
	@GetMapping(value = {"/{codart}/{codfid}", "/{codart}"})
	public double getPricePromo(@PathVariable("codart") String CodArt, @PathVariable("codfid") Optional<String> optCodFid)  
	{
		double retVal = 0;
		
		if (optCodFid.isPresent())
		{
			
			log.info(String.format("Cerchiamo promo riservata alla fidelity %s dell'articolo %s ",optCodFid,CodArt));
			
			retVal = promoService.SelByCodArtAndCodFid(CodArt, optCodFid.get());
		}
		else 
		{
			log.info(String.format("Cerchiamo Prezzo in promo articolo %s ",CodArt));
			
			retVal = promoService.SelPromoByCodArt(CodArt);
		}
		
		return retVal;
	}
	
	// ------------------- SELEZIONE PREZZO PROMO FIDELITY ------------------------------------
	@GetMapping(value = {"/fidelity/{codart}"})
	public double getPricePromoFid(@PathVariable("codart") String CodArt)  
	{
		double retVal = 0;
		
		log.info(String.format("Cerchiamo promo fidelity articolo %s ",CodArt));
			
		retVal = promoService.SelPromoByCodArtAndFid(CodArt);
		
		return retVal;
	}
	
	
}

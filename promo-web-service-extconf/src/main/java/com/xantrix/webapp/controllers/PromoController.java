package com.xantrix.webapp.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
 
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.dtos.InfoMsg;
import com.xantrix.webapp.dtos.PromoDto;
import com.xantrix.webapp.entities.Promo;
import com.xantrix.webapp.exceptions.BindingException;
import com.xantrix.webapp.exceptions.DuplicateException;
import com.xantrix.webapp.exceptions.NotFoundException;
import com.xantrix.webapp.feign.ArticoliClient;
import com.xantrix.webapp.services.PromoService;

import lombok.SneakyThrows;
import lombok.extern.java.Log;

@RestController
@RequestMapping(value = "api/promo")
@Log
public class PromoController 
{
	
	@Autowired
	private PromoService promoService;
	
	@Autowired
	ArticoliClient articoliClient;
	
	@Autowired
	private ResourceBundleMessageSource errMessage;
	
		
	@GetMapping(produces = "application/json")
	@SneakyThrows
	public ResponseEntity<List<PromoDto>> listAllPromo()
	{
		log.info("****** Otteniamo tutte le promozioni *******");
		
		List<PromoDto> promo = promoService.SelTutti();
		
		if (promo.isEmpty())
		{
			throw new NotFoundException("Non è presente alcune promozione in anagrafica");
		}
		
		log.info("Numero dei record: " + promo.size());
		
		return new ResponseEntity<List<PromoDto>>(promo, HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/id/{idPromo}", produces = "application/json")
	@SneakyThrows
	public ResponseEntity<PromoDto> listPromoById(@PathVariable("idPromo") String IdPromo) 
	{
		log.info("****** Otteniamo la promozione con Id: " + IdPromo + "*******");
		
		PromoDto promo = promoService.SelByIdPromo(IdPromo);
		
		if (promo == null)
		{
			throw new NotFoundException("Promozione Assente o Id Errato");
		}
		 
		return new ResponseEntity<PromoDto>(promo, HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/codice", produces = "application/json")
	@SneakyThrows
	public ResponseEntity<PromoDto> listPromoByCodice(@RequestParam("anno") String Anno,
			@RequestParam("codice") String Codice) 
	{
		log.info("****** Otteniamo la promozione con Codice: " + Codice + "*******");
		
		PromoDto promo = promoService.SelByAnnoCodice(Anno, Codice);
		
		if (promo == null)
		{
			throw new NotFoundException("Promozione Assente o Codice Errato");
		}
		
		return new ResponseEntity<PromoDto>(promo, HttpStatus.OK);
	}
	
	@GetMapping(value = "/active", produces = "application/json")
	@SneakyThrows
	public ResponseEntity<List<PromoDto>> listPromoActive()
	{
		log.info("****** Otteniamo la Promozione Attive*******");
		
		List<PromoDto> promo = promoService.SelPromoActive();
		
		if (promo == null)
		{
			throw new NotFoundException("Non è presente alcuna promozione attiva");
		}
		
		return new ResponseEntity<List<PromoDto>>(promo, HttpStatus.OK);
	}
	
	@PostMapping(value = "/inserisci")
	@SneakyThrows
	public ResponseEntity<InfoMsg> createPromo( @RequestBody @Valid Promo promo, BindingResult bindingResult)
	{
		 String GUID = promo.getIdPromo();
		 
		//controllo validità dati articolo
		if (bindingResult.hasErrors())
		{
			String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
			
			log.warning(MsgErr);
			 
			throw new BindingException(MsgErr);
		}
		
		if (GUID.length() == 0)
		{
			String anno = String.valueOf(promo.getAnno());
			String codice = promo.getCodice();
			
			if (promoService.SelByAnnoCodice(anno, codice) != null)
			{
				String MsgErr = String.format("Una promozione con codice %s e anno %s è presente in anagrafica! "
						+ "Impossibile proseguire!", codice, anno );
				
				log.warning(MsgErr);
				
				throw new DuplicateException(MsgErr);
			}
				
			UUID uuid = UUID.randomUUID();
		    GUID = uuid.toString();
		    
		    log.info(String.format("***** Creiamo una Promo con id %s *****", GUID) );
		    
		    promo.setIdPromo(GUID);
		}
		/*
		else
		{
			 log.warning("Impossibile modificare con il metodo POST ");
			 
			 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		*/
		
		promoService.InsPromo(promo);
		
		return new ResponseEntity<InfoMsg>(new InfoMsg(LocalDate.now(), 
				String.format("Creata promozione con id %s", GUID)), HttpStatus.CREATED);
	}
	
	/*
	@PutMapping(value = "/modifica")
	public ResponseEntity<Promo> updatePromo(@RequestBody Promo promo)
	{
		 log.info("***** Modifichiamo la Promo con id " + promo.getIdPromo() + " *****");
		 
		 if (promo.getIdPromo().length() > 0)
		 {
			 promoService.InsPromo(promo);
			 
			 return new ResponseEntity<Promo>(new HttpHeaders(), HttpStatus.CREATED);
		 }
		 else
		 {
			 log.warning("Impossibile modificare una promozione priva di id! ");
			 
			 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		 }
	}
	*/
	
	@DeleteMapping(value = "/elimina/{idPromo}")
	public ResponseEntity<?> deletePromo(@PathVariable("idPromo") String IdPromo) 
	{
		log.info("Eliminiamo la promo con id " + IdPromo);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode responseNode = mapper.createObjectNode();
		
		Promo promo = promoService.SelByIdPromo2(IdPromo);
		
		if (promo == null)
		{
			log.warning("Impossibile trovare la promo con id " + IdPromo);
			
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		promoService.DelPromo(promo);
		
		responseNode.put("code", HttpStatus.OK.toString());
		responseNode.put("message", "Eliminazione Promozione " + IdPromo + " Eseguita Con Successo!");
		
		return new ResponseEntity<>(responseNode, headers, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/elimina/all")
	public ResponseEntity<?> deleteAllPromo() 
	{
		log.info("Eliminiamo Tutte le promozioni presenti in anagrafica");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode responseNode = mapper.createObjectNode();
		
		promoService.DelAllPromo();
		
		responseNode.put("code", HttpStatus.OK.toString());
		responseNode.put("message", "Eliminazione generale eseguita con successo!");
		
		return new ResponseEntity<>(responseNode, headers, HttpStatus.OK);
	}
	
	
}

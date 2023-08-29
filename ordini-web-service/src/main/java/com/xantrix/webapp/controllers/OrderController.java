package com.xantrix.webapp.controllers;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.dtos.OrderDto;
import com.xantrix.webapp.entities.Ordini;
import com.xantrix.webapp.exceptions.BindingException;
import com.xantrix.webapp.exceptions.NotFoundException;
import com.xantrix.webapp.services.OrderMessagingService;
import com.xantrix.webapp.services.OrdiniService;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
 

@RestController
@RequestMapping("/api/ordini")
@Log
public class OrderController 
{
	
	@Autowired 
	OrderMessagingService orderService;
	
	@Autowired 
	OrdiniService ordineSerice;
	
	@Autowired
	private ResourceBundleMessageSource errMessage;
	
	@SneakyThrows
	@PostMapping(value = {"/invia/{idordine}","/invia/{idordine}/{fonte}"},produces = "application/json") 
	public ResponseEntity<?> sendOrder(
			@PathVariable("idordine") String IdOrdine, 
			@PathVariable("fonte") Optional<String> Fonte,
			HttpServletRequest httpRequest) 
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String fonte = (Fonte.isEmpty()) ? "Web" : Fonte.get();
		
		OrderDto order = new OrderDto();
		Ordini ordine = ordineSerice.SelById(IdOrdine).get();
		
		if (ordine == null)
		{
			String ErrMsg = String.format("L'ordine %s non è stato trovato!", IdOrdine);
			
			log.warning(ErrMsg);
			
			throw new NotFoundException(ErrMsg);
		}
		else
		{
			order.setId(ordine.getId());
			order.setIdcliente(ordine.getCodfid());
			order.setData(java.sql.Date.valueOf(ordine.getData()));
			order.setTotale(ordineSerice.SelValTot(IdOrdine));
			order.setFonte(fonte);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode responseNode = mapper.createObjectNode();
		
		orderService.sendOrder(order);
		
		responseNode.put("code", HttpStatus.OK.toString());
		responseNode.put("message", String.format("Inviato Ordine (%s) al gestore dei bollini",order.getId()));
		
		return new ResponseEntity<>(responseNode, headers, HttpStatus.OK);
	}
	
	// ------------------- Ricerca Per Codice ------------------------------------
	@SneakyThrows
	@RequestMapping(value = "/cerca/codice/{idordine}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Ordini> listOrdByCode(
			@PathVariable("idordine") String IdOrdine,
			HttpServletRequest httpRequest)	 
	{
		log.info("****** Otteniamo l'ordine con codice " + IdOrdine + " *******");
		
		//String AuthHeader = httpRequest.getHeader("Authorization");

		Optional<Ordini> ordine = ordineSerice.SelById(IdOrdine);
		
		if (ordine == null)
		{
			String ErrMsg = String.format("L'ordine %s non è stato trovato!", IdOrdine);
			
			log.warning(ErrMsg);
			
			throw new NotFoundException(ErrMsg);
		}
		
		return new ResponseEntity<Ordini>(ordine.get(), HttpStatus.OK);
	}
	
	// ------------------- INSERIMENTO ORDINE ------------------------------------
	@SneakyThrows
	@RequestMapping(value = "/inserisci", method = RequestMethod.POST)
	public ResponseEntity<?> createOrd(@Valid @RequestBody Ordini ordine, BindingResult bindingResult) 
	{
		log.info("Salviamo l'ordine con id " + ordine.getId());
		
		if (bindingResult.hasErrors())
		{
			String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
			
			log.warning(MsgErr);

			throw new BindingException(MsgErr);
		}
		 
		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();
		
		headers.setContentType(MediaType.APPLICATION_JSON);

		ObjectNode responseNode = mapper.createObjectNode();
		
		if (ordine.getId() == "")
		{
			UUID uuid = UUID.randomUUID();
			ordine.setId(uuid.toString());
		}
		
		ordine.setData(LocalDate.now()); 
		ordineSerice.InsOrdine(ordine);
		
		responseNode.put("code", HttpStatus.OK.toString());
		responseNode.put("message", ordine.getId());

		return new ResponseEntity<>(responseNode, headers, HttpStatus.CREATED);
	}
	
	// ------------------- ELIMINAZIONE ORDINE ------------------------------------
	@SneakyThrows
	@RequestMapping(value = "/elimina/{idordine}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteOrd(@PathVariable("idordine") String IdOrdine) 
	{
		log.info("Eliminiamo l'ordine con id " + IdOrdine);

		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ObjectNode responseNode = mapper.createObjectNode();

		Ordini ordine = ordineSerice.SelById(IdOrdine).get();

		if (ordine == null)
		{
			String MsgErr = String.format("Ordine %s non presente in anagrafica!",IdOrdine);
			
			log.warning(MsgErr);
			
			throw new NotFoundException(MsgErr);
		}

		ordineSerice.DelOrdine(ordine);

		responseNode.put("code", HttpStatus.OK.toString());
		responseNode.put("message", String.format("Eliminazione Ordine %s Eseguita Con Successo",IdOrdine));

		return new ResponseEntity<>(responseNode, headers, HttpStatus.OK);
	}
}

package com.xantrix.webapp.controllers;

 
import java.util.List;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.dtos.ClientiDto;
import com.xantrix.webapp.entities.Clienti;
import com.xantrix.webapp.exceptions.BindingException;
import com.xantrix.webapp.exceptions.NotFoundException;
import com.xantrix.webapp.services.ClientiService;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
 

@RestController
@RequestMapping("/api/clienti")
@Log
public class ClientiController 
{
	 
	@Autowired
	ClientiService clientiService;
	
	@Autowired
	private ResourceBundleMessageSource errMessage;
	
	
	// ------------------- Ricerca Per Codice ------------------------------------
	@RequestMapping(value = "/cerca/codice/{idcliente}", method = RequestMethod.GET, produces = "application/json")
	@SneakyThrows
	public ResponseEntity<ClientiDto> listCliByCode(@PathVariable("idcliente") String idCliente, 
			HttpServletRequest httpRequest)	 
	{
		log.info("****** Otteniamo il cliente con codice " + idCliente + " *******");
		
		//String AuthHeader = httpRequest.getHeader("Authorization");

		ClientiDto clienteDto = clientiService.SelByIdCliente(idCliente);
		
		if (clienteDto == null)
		{
			String ErrMsg = String.format("Il cliente %s non è stato trovato!", idCliente);
			
			log.warning(ErrMsg);
			
			throw new NotFoundException(ErrMsg);
		}
		 
		return new ResponseEntity<ClientiDto>(clienteDto, HttpStatus.OK);
	}
	
	// ------------------- Ricerca Per Cognome ------------------------------------
	@RequestMapping(value = "/cerca/cognome/{filter}", method = RequestMethod.GET, produces = "application/json")
	@SneakyThrows
	public ResponseEntity<List<ClientiDto>> listCliByCognome(@PathVariable("filter") String filter, 
			HttpServletRequest httpRequest)	 
	{
		log.info("****** Otteniamo il cliente con codice " + filter + " *******");
		
		//String AuthHeader = httpRequest.getHeader("Authorization");

		List<ClientiDto> clientiDto = clientiService.SelByCognome(filter.toUpperCase().replace("\\", ""));
		
		if (clientiDto.isEmpty())
		{
			String ErrMsg = String.format("Non è stato trovato alcun cliente con il cognome %s!", filter);
			
			log.warning(ErrMsg);
			
			throw new NotFoundException(ErrMsg);
		}
		 
		return new ResponseEntity<List<ClientiDto>>(clientiDto, HttpStatus.OK);
	}
	
	// ------------------- INSERIMENTO / MODIFICA CLIENTE ------------------------------------
	@SneakyThrows
	@RequestMapping(value = "/inserisci", method = RequestMethod.POST)
	public ResponseEntity<?> createOrd(@Valid @RequestBody Clienti cliente, BindingResult bindingResult) 
	{
		log.info(String.format("Salviamo il cliente %s", cliente.getCognome()));
		
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
		
		clientiService.SaveCliente(cliente);
		
		responseNode.put("code", HttpStatus.OK.toString());
		responseNode.put("message", "Inserimento cliente avvenuto con successo!");

		return new ResponseEntity<>(responseNode, headers, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/cards/modifica/{idcliente}/{bollini}")
	public ResponseEntity<?> UpdBollini(@PathVariable("idcliente") String IdCliente, @PathVariable("bollini") int Bollini)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		if (IdCliente.length() > 0 && Bollini != 0)
		{
		    log.info(String.format("***** Modifica Monte Bollini Cliente %s *****", IdCliente));
		}
		else
		{
			 log.warning("Impossibile modificare con il metodo POST ");
			 
			 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode responseNode = mapper.createObjectNode();
		
		clientiService.UpdMonteBollini(IdCliente, Bollini);
		
		responseNode.put("code", HttpStatus.OK.toString());
		responseNode.put("message", String.format("Monte bollini cliente %s modificato di (%s) Bollini",IdCliente,Bollini));
		
		return new ResponseEntity<>(responseNode, headers, HttpStatus.OK);
	}
	
}

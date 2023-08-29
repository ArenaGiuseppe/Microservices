package com.xantrix.webapp.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xantrix.webapp.dtos.ClientiDto;
import com.xantrix.webapp.entities.Clienti;
import com.xantrix.webapp.repository.ClientiRepository;

@Service
@Transactional(readOnly = true)
public class ClientiServiceImpl implements ClientiService
{

	@Autowired
	ClientiRepository clientiRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	@Cacheable(value = "CacheSelByIdCli",key = "#idcliente",unless="#result == null")
	public ClientiDto SelByIdCliente(String idcliente) 
	{
		Clienti cliente = clientiRepository.findByCodice(idcliente);
		
		return this.ConvertToDto(cliente);
	}
	
	@Override
	@Cacheable(value = "CacheSelByCognome",key = "#cognome",sync = true)
	public List<ClientiDto> SelByCognome(String cognome) 
	{
		List<ClientiDto> clientiDto = this.ConvertToDto(clientiRepository.selByCognomeLike(cognome));
		
		return clientiDto;
	}

	@Override
	@Transactional
	@Caching(evict = { 
			@CacheEvict(cacheNames="CacheSelByCognome", allEntries = true),
			@CacheEvict(cacheNames="CacheSelByIdCli",key = "#idcliente") 
			})
	public void UpdMonteBollini(String idcliente, int bollini) 
	{
		clientiRepository.updMonteBollini(idcliente, bollini);
	}
	
	@Override
	@Transactional
	@Caching(evict = { 
			@CacheEvict(cacheNames="CacheSelByCognome", allEntries = true),
			@CacheEvict(cacheNames="CacheSelByIdCliente",key = "#cliente.codice") 
			})
	public void SaveCliente(Clienti cliente) 
	{
		clientiRepository.saveAndFlush(cliente);
	}
	
	private ClientiDto ConvertToDto(Clienti cliente)
	{
		ClientiDto clienteDto = null;
		
		if (cliente != null)
		{
			clienteDto =  modelMapper.map(cliente, ClientiDto.class);
		}
		
		return clienteDto;
	}
	
	private List<ClientiDto> ConvertToDto(List<Clienti> clienti)
	{
		List<ClientiDto> retVal = clienti
		        .stream()
		        .map(source -> modelMapper.map(source, ClientiDto.class))
		        .collect(Collectors.toList());
		
		return retVal;
	}

	
	
}

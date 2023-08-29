package com.xantrix.webapp.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xantrix.webapp.entities.Ordini;
import com.xantrix.webapp.repository.OrdiniRepository;

@Service
@Transactional()
public class OrdiniServiceImpl implements OrdiniService
{

	@Autowired
	OrdiniRepository ordiniRepository;
	
	@Override
	public Optional<Ordini> SelById(String Id) 
	{
		return ordiniRepository.findById(Id);
	}

	@Override
	public void InsOrdine(Ordini Ordine) 
	{
		ordiniRepository.save(Ordine);
	}

	@Override
	public void DelOrdine(Ordini Ordine) 
	{
		ordiniRepository.delete(Ordine); 
	}

	@Override
	public double SelValTot(String Id) 
	{
		return ordiniRepository.GetValOrdine(Id);
	}

	 

}

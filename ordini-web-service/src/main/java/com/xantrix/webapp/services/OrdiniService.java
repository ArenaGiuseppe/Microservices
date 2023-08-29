package com.xantrix.webapp.services;

import java.util.Optional;

import com.xantrix.webapp.entities.Ordini;

public interface OrdiniService 
{
	public Optional<Ordini> SelById(String Id);
	
	public void InsOrdine(Ordini Ordine);
	
	public void DelOrdine(Ordini Ordine);
	
	public double SelValTot(String Id);
}

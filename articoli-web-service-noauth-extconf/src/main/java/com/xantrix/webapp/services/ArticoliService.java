package com.xantrix.webapp.services;

import java.util.List;

import com.xantrix.webapp.dtos.ArticoliDto;
import com.xantrix.webapp.entities.Articoli;

import org.springframework.data.domain.Pageable;

public interface ArticoliService 
{
	public List<ArticoliDto> SelByDescrizione(String descrizione,String idList, String authHeader);
	
	public List<ArticoliDto> SelByDescrizione(String descrizione, Pageable pageable);
	
	public ArticoliDto SelByCodArt(String codart, String idList, String authHeader); 
	
	public Articoli SelByCodArt2(String codart);
	
	public ArticoliDto SelByBarcode(String barcode, String idList, String authHeader); 
	
	public void DelArticolo(Articoli articolo);
	
	public void InsArticolo(Articoli articolo);
	
	public void CleanCaches();
}
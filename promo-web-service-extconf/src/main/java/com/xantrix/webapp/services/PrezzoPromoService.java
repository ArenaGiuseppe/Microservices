package com.xantrix.webapp.services;

public interface PrezzoPromoService 
{
	Double SelPromoByCodArt(String CodArt);
	
	Double SelPromoByCodArtAndFid(String CodArt);
	
	Double SelByCodArtAndCodFid(String CodArt, String CodFid);
	
	void UpdOggettoPromo(String Oggetto, Long Id);
}

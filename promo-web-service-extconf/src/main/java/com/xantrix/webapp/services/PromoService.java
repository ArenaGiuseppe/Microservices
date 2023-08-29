package com.xantrix.webapp.services;
 
import java.util.List;

import com.xantrix.webapp.dtos.PromoDto;
import com.xantrix.webapp.entities.Promo;

public interface PromoService 
{
	public List<PromoDto> SelTutti();
	
	public Promo SelByIdPromo2(String IdPromo);
	
	public PromoDto SelByIdPromo(String IdPromo);
	
	public PromoDto SelByAnnoCodice(String Anno, String Codice);
	
	List<PromoDto> SelPromoActive();
	
	public void InsPromo(Promo promo);
	
	public void DelPromo(Promo promo);
	
	public void DelAllPromo();
	
	public void CleanCaches(); 
}

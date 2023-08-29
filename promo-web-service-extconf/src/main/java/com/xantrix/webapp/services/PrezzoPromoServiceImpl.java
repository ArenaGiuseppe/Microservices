package com.xantrix.webapp.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import com.xantrix.webapp.entities.DettPromo;
import com.xantrix.webapp.repository.PrezziPromoRepository;

import lombok.extern.java.Log;
 
@Service
@Transactional
@Log
//@CacheConfig(cacheNames={"prezzi"})
public class PrezzoPromoServiceImpl implements PrezzoPromoService
{
	@Autowired
	PrezziPromoRepository prezziPromoRep;
	
	@Override
	@Cacheable(value = "CacheSelPrzPromo",key = "#CodArt",sync = true)
	public Double SelPromoByCodArt(String CodArt) 
	{
		double retVal = 0;

		List<DettPromo> Dettpromo =  prezziPromoRep.SelByCodArt(CodArt);
		
		if (!Dettpromo.isEmpty())
		{
			for (DettPromo promo : Dettpromo)
			{
				if (promo.getTipoPromo().getIdTipoPromo() == 1)
				{
					try
					{
						retVal = Double.parseDouble(promo.getOggetto().replace(",", "."));
					}
					catch(NumberFormatException ex)
					{
						log.warning(ex.getMessage());
					}
				}
				else  //TODO Gestire gli altri tipi di promozione
				{
					retVal = 0;
				}
			}
			
			
		}
		else
		{
			log.warning("Promo Articolo Assente!!");
		}

		return retVal;
	}

	@Override
	//@Cacheable
	public Double SelPromoByCodArtAndFid(String CodArt) 
	{
		double retVal = 0;

		List<DettPromo> Dettpromo =  prezziPromoRep.SelByCodArtAndFid(CodArt);
		
		if (!Dettpromo.isEmpty())
		{
			for (DettPromo promo : Dettpromo)
			{
				if (promo.getTipoPromo().getIdTipoPromo() == 1)
				{
					try
					{
						retVal = Double.parseDouble(promo.getOggetto().replace(",", "."));
					}
					catch(NumberFormatException ex)
					{
						log.warning(ex.getMessage());
					}
				}
				else  //TODO Gestire gli altri tipi di promozione
				{
					retVal = 0;
				}
			}
		}
		else
		{
			log.warning("Promo Articolo Fidelity Assente!!");
		}

		return retVal;
	}
	
	@Override
	@Cacheable(value = "CacheSelPrzPromo2",key = "#CodArt.concat('-').concat(#CodFid)",sync = true)
	public Double SelByCodArtAndCodFid(String CodArt, String CodFid) 
	{
		double retVal = 0;

		List<DettPromo> Dettpromo =  prezziPromoRep.SelByCodArtAndCodFid(CodArt, CodFid);
		
		if (!Dettpromo.isEmpty())
		{
			for (DettPromo promo : Dettpromo)
			{
				if (promo.getTipoPromo().getIdTipoPromo() == 1)
				{
					try
					{
						retVal = Double.parseDouble(promo.getOggetto().replace(",", "."));
					}
					catch(NumberFormatException ex)
					{
						log.warning(ex.getMessage());
					}
				}
				else  //TODO Gestire gli altri tipi di promozione
				{
					retVal = 0;
				}
			}
		}
		else
		{
			log.warning(String.format("Promo Riservata Fidelity %s Assente!!", CodFid) );
		}

		return retVal;
	}
	
	@Override
	public void UpdOggettoPromo(String Oggetto, Long Id) 
	{
		// TODO Auto-generated method stub
	}
	
}

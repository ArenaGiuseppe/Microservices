package com.xantrix.webapp.services;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xantrix.webapp.dtos.ArticoliDto;
import com.xantrix.webapp.dtos.PromoDto;
import com.xantrix.webapp.entities.Promo;
import com.xantrix.webapp.feign.ArticoliClient;
import com.xantrix.webapp.repository.PromoRepository;

import feign.FeignException;
import lombok.extern.java.Log;

@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames={"CachePromoAttive"})
@Log
public class PromoServiceImpl implements PromoService
{
	@Autowired
	PromoRepository promoRepository;
	
	@Autowired
	private ArticoliClient articoliClient;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	CacheManager cacheManager;

	@Override
	@Cacheable
	public List<PromoDto> SelTutti() 
	{
		List<Promo> Promo = promoRepository.findAll();
		
		return this.ConvertToDto(Promo);  
	}

	@Override
	@Cacheable(value = "CacheSelPromoById",key = "#idPromo",unless="#result == null")
	public PromoDto SelByIdPromo(String idPromo) 
	{
		return  this.ConvertToDto(promoRepository.findByIdPromo(idPromo));
	}
	
	@Override
	public Promo SelByIdPromo2(String idPromo) 
	{
		return promoRepository.findByIdPromo(idPromo);
	}
	
	@Override
	@Cacheable(value = "CacheSelPromoByAnnoCodice",key = "#anno.concat('-').concat(#codice)",unless="#result == null")
	public PromoDto SelByAnnoCodice(String anno, String codice) 
	{
		return this.ConvertToDto(promoRepository.findByAnnoAndCodice(Integer.parseInt(anno), codice));
	}

	@Override
	@Cacheable
	public List<PromoDto> SelPromoActive() 
	{
		return this.ConvertToDto(promoRepository.SelPromoActive());
	}

	@Override
	@Transactional
	@Caching(evict = { 
			@CacheEvict(cacheNames="CachePromoAttive", allEntries = true),
			@CacheEvict(cacheNames="CacheSelPrzPromo", allEntries = true),
			@CacheEvict(cacheNames="CacheSelPrzPromo2", allEntries = true),
			@CacheEvict(cacheNames="CacheSelPromoById",key = "#promo.idPromo"),
			@CacheEvict(cacheNames="CacheSelPromoByAnnoCodice",key = "#promo.anno.toString().concat('-').concat(#promo.codice)")
			})
	public void InsPromo(Promo promo) 
	{
		promoRepository.saveAndFlush(promo);
	}

	@Override
	@Transactional
	@Caching(evict = { 
			@CacheEvict(cacheNames="CachePromoAttive", allEntries = true),
			@CacheEvict(cacheNames="CacheSelPrzPromo", allEntries = true),
			@CacheEvict(cacheNames="CacheSelPrzPromo2", allEntries = true),
			@CacheEvict(cacheNames="CacheSelPromoById",key = "#promo.idPromo"),
			@CacheEvict(cacheNames="CacheSelPromoByAnnoCodice",key = "#promo.anno.toString().concat('-').concat(#promo.codice)")
			})
	public void DelPromo(Promo promo) 
	{
		promoRepository.delete(promo);
	}
	
	@Override
	@Transactional
	public void DelAllPromo() 
	{
		promoRepository.deleteAll();
		this.CleanCaches();
	}
	
	@Override
	public void CleanCaches() 
	{
		Collection<String> items = cacheManager.getCacheNames();
		
		items.forEach((item) -> {
			log.info(String.format("Eliminazione cache %s", item));
			
			cacheManager.getCache(item).clear();
		});
		
	}
	
	private PromoDto ConvertToDto(Promo promo) 
	{
		PromoDto promoDto = null;
		
		if (promo != null)
		{
			promoDto =  modelMapper.map(promo, PromoDto.class);
			
			promoDto.getDettPromo().forEach(f -> f.setDesart(this.getDatiArt(f.getCodart(), "").getDescrizione()));
			promoDto.getDettPromo().forEach(f -> f.setPrezzo(this.getDatiArt(f.getCodart(), "").getPrezzo()));
		}
		
		/*
		Collections.sort(dettPromo, Comparator.comparingInt(DettPromoDto :: getRiga));
		*/
		
		return promoDto;
	}
	
	private List<PromoDto> ConvertToDto(List<Promo> promo) 
	{
		List<PromoDto> promoDto = promo
		        .stream()
		        .map(source -> modelMapper.map(source, PromoDto.class))
		        .collect(Collectors.toList());
		
		promoDto.forEach(b -> b.getDettPromo().forEach(f -> f.setDesart(this.getDatiArt(f.getCodart(), "").getDescrizione())));
		promoDto.forEach(b -> b.getDettPromo().forEach(f -> f.setPrezzo(this.getDatiArt(f.getCodart(), "").getPrezzo())));
		
		return promoDto;
	}

	private ArticoliDto getDatiArt(String CodArt, String Header)
	{
		ArticoliDto articoloDto = new ArticoliDto();
		articoloDto.setDescrizione("");
		articoloDto.setPrezzo(0);
		
		try
		{ 
			log.info(String.format("Ricerca dati articolo codice %s", CodArt));
			articoloDto = articoliClient.getArticolo(Header, CodArt);
			 
			log.info(String.format("Trovato Articolo %s", articoloDto.getDescrizione()));
			
		}
		catch(FeignException ex)
		{
			log.warning(String.format("Errore: %s", ex.getLocalizedMessage()));
		}
		
		return articoloDto;
	}
	

	
	
}

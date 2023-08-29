package com.xantrix.webapp.services;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.xantrix.webapp.dtos.ArticoliDto;
import com.xantrix.webapp.dtos.PrezzoDto;
import com.xantrix.webapp.entities.Articoli;
import com.xantrix.webapp.entities.Barcode;
import com.xantrix.webapp.feign.PriceClient;
import com.xantrix.webapp.feign.PromoClient;
import com.xantrix.webapp.repository.ArticoliRepository;

import feign.FeignException;
import lombok.extern.java.Log;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
//@CacheConfig(cacheNames={"articoli"})
@Log
public class ArticoliServiceImpl implements ArticoliService
{
	@Autowired
	ArticoliRepository articoliRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	CacheManager cacheManager;
	
	@Autowired
	private PriceClient priceClient;
	
	@Autowired
	private PromoClient promoClient;

	@Override
	@Cacheable(value = "CacheSelByDes",key = "#descrizione.concat('-').concat(#idList)",sync = true)
	public List<ArticoliDto> SelByDescrizione(String descrizione,String idList, String authHeader) 
	{
		String filter = "%" + descrizione.toUpperCase() + "%";
		
		List<ArticoliDto> articoli = this.ConvertToDto(articoliRepository.selByDescrizioneLike(filter));
		
		if (!articoli.isEmpty())
		{
			articoli.forEach(f -> f.setPrezzo(this.getPriceArt(f.getCodArt(), idList, authHeader)));
			articoli.forEach(f -> f.setPrezzoPromo(this.getPricePromo(f.getCodArt(), authHeader)));
		}
		
		return articoli;
	}

	
	@Override
	//@Cacheable
	public List<ArticoliDto> SelByDescrizione(String descrizione, Pageable pageable) 
	{
		String filter = "%" + descrizione.toUpperCase() + "%";
		
		List<Articoli> articoli = articoliRepository.findByDescrizioneLike(filter, pageable);
		
		return ConvertToDto(articoli);
		
		
	}
	
	
	@Override
	public Articoli SelByCodArt2(String codart) 
	{	 
		return  articoliRepository.findByCodArt(codart);
	}
	
	@Override
	@Cacheable(value = "CacheSelByCodArt",key = "#codart.concat('-').concat(#idList)", unless="#result == null")
	public ArticoliDto SelByCodArt(String codart, String idList, String authHeader) 
	{
		ArticoliDto articolo = this.ConvertToDto(this.SelByCodArt2(codart));
		
		if (articolo != null)
		{
			articolo.setPrezzo(this.getPriceArt(articolo.getCodArt(), idList, authHeader));
			articolo.setPrezzoPromo(this.getPricePromo(articolo.getCodArt(), authHeader));
		}
		
		return articolo;
	}

	@Override
	@Cacheable(value = "CacheSelByBarcode",key = "#barcode.concat('-').concat(#idList)", unless="#result == null")
	public ArticoliDto SelByBarcode(String barcode, String idList, String authHeader) 
	{
		ArticoliDto articolo = this.ConvertToDto(articoliRepository.selByEan(barcode));
		
		if (articolo != null)
		{
			articolo.setPrezzo(this.getPriceArt(articolo.getCodArt(), idList, authHeader));
			articolo.setPrezzoPromo(this.getPricePromo(articolo.getCodArt(), authHeader));
		}
		
		return articolo;
	}
	
	@Override
	@Transactional
	@Caching(evict = { 
			@CacheEvict(cacheNames="CacheSelByDes", allEntries = true),
			@CacheEvict(cacheNames="CacheSelByCodArt",key = "#articolo.codArt.concat('-').concat('1')"),
			@CacheEvict(cacheNames="CacheSelByCodArt",key = "#articolo.codArt.concat('-').concat('2')") 
			})
	public void DelArticolo(Articoli articolo) 
	{
		articoliRepository.delete(articolo);
		
		this.EvictCache(articolo.getBarcode());
	}

	@Override
	@Transactional
	@Caching(evict = { 
			@CacheEvict(cacheNames="CacheSelByDes", allEntries = true),
			@CacheEvict(cacheNames="CacheSelByCodArt",key = "#articolo.codArt.concat('-').concat('1')"),
			@CacheEvict(cacheNames="CacheSelByCodArt",key = "#articolo.codArt.concat('-').concat('2')")
			})
	public void InsArticolo(Articoli articolo) 
	{
		articolo.setDescrizione(articolo.getDescrizione().toUpperCase());
		
		articoliRepository.save(articolo);
		
		this.EvictCache(articolo.getBarcode());
	}
	
	private void EvictCache(Set<Barcode> Ean) 
	{
		 Ean.forEach((Barcode barcode) -> 
		 {
			 log.info("Eliminazione cache barcode " + barcode.getBarcode());
			 cacheManager.getCache("CacheSelByBarcode").evict(barcode.getBarcode().concat("-").concat("1"));
			 cacheManager.getCache("CacheSelByBarcode").evict(barcode.getBarcode().concat("-").concat("2"));
	     });
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
	
	private ArticoliDto ConvertToDto(Articoli articoli)
	{
		ArticoliDto articoliDto = null;
		
		
		if (articoli != null)
		{
			articoliDto =  modelMapper.map(articoli, ArticoliDto.class);
		}
		
		return articoliDto;
	}
	
	private List<ArticoliDto> ConvertToDto(List<Articoli> articoli)
	{
		List<ArticoliDto> retVal = articoli
		        .stream()
		        .map(source -> modelMapper.map(source, ArticoliDto.class))
		        .collect(Collectors.toList());
		
		return retVal;
	}
	
	private double getPricePromo(String CodArt, String Header)
	{
		double prezzoPromo = 0;
		
		log.info(String.format("Ottenimento Prezzo Promozionale Codice %s", CodArt));
		
		try
		{
			double prezzo = this.promoClient.getPromoPrice(Header, CodArt);
			
			if (prezzo > 0)
				log.info(String.format("Prezzo Promozionale %s", prezzo));
			
			prezzoPromo = prezzo;
		}
		catch(FeignException ex)
		{
			log.warning(String.format("Errore: %s", ex.getLocalizedMessage()));
		}
		
		return prezzoPromo;
	}

	private double getPriceArt(String CodArt, String IdList, String Header)
	{
		
		double prezzo = 0;
		
		try
		{
			 
			PrezzoDto prezzoDto = (IdList != null) ? 
					priceClient.getPriceArt2(Header, CodArt, IdList) : 
					priceClient.getDefPriceArt2(Header, CodArt);
			 
			log.info("Prezzo Articolo " + prezzoDto.getCodArt() + ": " + prezzoDto.getPrezzo());
			
			if (prezzoDto.getSconto() > 0)
			{
				log.info("Attivato Sconto: " + prezzoDto.getSconto() + "%");
				
				prezzo = prezzoDto.getPrezzo() * (1 - ( prezzoDto.getSconto() / 100));
				prezzo *= 100; 
				prezzo = Math.round(prezzo);
				prezzo /= 100;
			}
			else 
			{
				prezzo = prezzoDto.getPrezzo();
			}
			
			/*
			prezzo = (IdList != null) ? 
					priceClient.getPriceArt(Header, CodArt, IdList) : 
					priceClient.getDefPriceArt(Header, CodArt);
			 
			log.info("Prezzo Articolo " + CodArt + ": " + prezzo);
			*/

		}
		catch(FeignException ex)
		{
			log.warning(String.format("Errore: %s", ex.getLocalizedMessage()));
		}
		
		return prezzo;
	}
	
	


}

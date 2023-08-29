package com.xantrix.webapp.services;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.xantrix.webapp.entities.DettListini;
import com.xantrix.webapp.repository.PrezziRepository;

import lombok.extern.java.Log;

@Service
@Transactional(readOnly = true)
@Log
public class PrezziServiceImpl implements PrezziService
{
	@Autowired
	PrezziRepository prezziRepository;
	
	@Autowired
	CacheManager cacheManager;

	@Override
	@Cacheable(value = "CacheSelPrezzo",key = "#CodArt.concat('-').concat(#Listino)", sync = true)
	public DettListini SelPrezzo(String CodArt, String Listino)
	{
		return prezziRepository.SelByCodArtAndList(CodArt, Listino);
	}
	
	@Override
	@Transactional
	@Caching(evict = { 
			@CacheEvict(cacheNames="CacheSelPrezzo",key = "#CodArt.concat('-').concat(#IdList)")
			})
	public void DelPrezzo(String CodArt, String IdList) 
	{
		prezziRepository.DelRowDettList(CodArt, IdList);
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

}

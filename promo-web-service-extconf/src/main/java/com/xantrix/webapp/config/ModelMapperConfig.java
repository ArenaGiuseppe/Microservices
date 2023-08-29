package com.xantrix.webapp.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;



@Configuration
public class ModelMapperConfig 
{
	@Bean
    public ModelMapper modelMapper() 
	{
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		modelMapper.addConverter(promoConverter);
		
		return modelMapper;
	}

	Converter<String, String> promoConverter = new Converter<String, String>() 
	{
		  @Override
		  public String convert(MappingContext<String, String> context) 
		  {
			  return context.getSource() == null ? "" : context.getSource().trim();
		  }
	};
}

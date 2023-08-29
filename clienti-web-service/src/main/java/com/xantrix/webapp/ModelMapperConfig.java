package com.xantrix.webapp;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xantrix.webapp.dtos.ClientiDto;
import com.xantrix.webapp.entities.Clienti;

@Configuration
public class ModelMapperConfig 
{
	@Bean
    public ModelMapper modelMapper() 
	{
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		modelMapper.addMappings(nominativoMapping);
		modelMapper.addMappings(new PropertyMap<Clienti, ClientiDto>() 
		{
			    @Override
			    protected void configure() 
			    {
			    	map().setUltimaSpesa(source.getCard().getUltimaspesa());
			    }
		});
		
		modelMapper.addMappings(new PropertyMap<Clienti, ClientiDto>() 
		{
			    @Override
			    protected void configure() 
			    {
			    	map().setBollini(source.getCard().getBollini());
			    }
		});
		
		
		modelMapper.addConverter(clientiConverter);
		
        return modelMapper;
    }
	
	Converter<Clienti, String> c = new Converter<Clienti, String>() 
	{
		  public String convert(MappingContext<Clienti, String> context) 
		  {
				Clienti cliente = context.getSource();
				
				String nome = (cliente.getNome() == null) ? "Sconosciuto" : cliente.getNome();
		    	String cognome = (cliente.getCognome() == null) ? "" : cliente.getCognome();
		    	
		    	return nome.concat(" ").concat(cognome);
		  }
	};
	
	PropertyMap<Clienti, ClientiDto> nominativoMapping = new PropertyMap<Clienti,ClientiDto>() 
	{
	      protected void configure() 
	      {
	         using(c).map(source).setNominativo(null);
	      }
	};

	Converter<String, String> clientiConverter = new Converter<String, String>() 
	{
		  @Override
		  public String convert(MappingContext<String, String> context) 
		  {
			  return context.getSource() == null ? "" : context.getSource().trim();
		  }
	};
	
	
}


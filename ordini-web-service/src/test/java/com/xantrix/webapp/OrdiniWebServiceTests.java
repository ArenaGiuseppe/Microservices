package com.xantrix.webapp;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.UUID;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.xantrix.webapp.entities.Ordini;
import com.xantrix.webapp.repository.OrdiniRepository;

@SpringBootTest()
@TestPropertySource(properties = {"profilo = test", "seq = 1", "ramo = master"}) 
@ContextConfiguration(classes = Application.class)
@TestMethodOrder(OrderAnnotation.class)

class OrdiniWebServiceTests 
{
	private MockMvc mockMvc;
	private static String IdOrdine = "";
	
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private OrdiniRepository ordiniRepository;
	
	private String ApiBaseUrl = "/api/ordini";
	
	@BeforeEach
	public void setup() throws JSONException, IOException
	{
		mockMvc = MockMvcBuilders
				.webAppContextSetup(wac)
				.build();	
	}
	
	private String getJsonData()
	{
		UUID uuid = UUID.randomUUID();
		IdOrdine = (IdOrdine.length() == 0) ? uuid.toString() : IdOrdine;
		
		String JsonData = 
				"{\n" + 
				"    \"id\": \"" + IdOrdine + "\",\n" + 
				"    \"idpdv\": 525,\n" + 
				"    \"codfid\": \"65000000\",\n" + 
				"    \"stato\": 1,\n" + 
				"    \"dettOrdine\": [\n" + 
				"        {\n" + 
				"            \"id\": -1,\n" + 
				"            \"codart\": \"000020026\",\n" + 
				"            \"qta\": 24.0,\n" + 
				"            \"prezzo\": 0.29\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"id\": -1,\n" + 
				"            \"codart\": \"000022601\",\n" + 
				"            \"qta\": 2.0,\n" + 
				"            \"prezzo\": 0.49\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"id\": -1,\n" + 
				"            \"codart\": \"000035110\",\n" + 
				"            \"qta\": 1.0,\n" + 
				"            \"prezzo\": 6.49\n" + 
				"        }\n" + 
				"    ],\n" + 
				"    \"data\": \"2019-11-25\"\n" + 
				"}";
		
		return JsonData;
	}
	
	

	@Test
	@Order(1)
	public void testInsOrder() throws Exception
	{
		
		mockMvc.perform(MockMvcRequestBuilders.post(ApiBaseUrl + "/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getJsonData())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.code").value("200 OK"))
			
				.andDo(print());
		
		assertThat(ordiniRepository.findById(IdOrdine).get())
		.extracting(Ordini::getCodfid)
		.isEqualTo("65000000");
		
	}
	
	@Test
	@Order(2)
	public void listOrdByCode() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/cerca/codice/" + IdOrdine)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				 //ordine
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.id").value(IdOrdine))
				.andExpect(jsonPath("$.idpdv").exists())
				.andExpect(jsonPath("$.idpdv").value("525"))
				.andExpect(jsonPath("$.codfid").exists())
				.andExpect(jsonPath("$.codfid").value("65000000"))
				.andExpect(jsonPath("$.stato").exists())
				.andExpect(jsonPath("$.stato").value(1))
				.andExpect(jsonPath("$.data").exists())
				.andExpect(jsonPath("$.data").value("2019-11-25"))
				 //dettaglio ordine
				.andExpect(jsonPath("$.dettOrdine[0].id").exists())
				.andExpect(jsonPath("$.dettOrdine[0].codart").exists())
				.andExpect(jsonPath("$.dettOrdine[0].codart").value("000035110")) 
				.andExpect(jsonPath("$.dettOrdine[0].qta").exists())
				.andExpect(jsonPath("$.dettOrdine[0].qta").value(1.0)) 
				.andExpect(jsonPath("$.dettOrdine[0].prezzo").exists())
				.andExpect(jsonPath("$.dettOrdine[0].prezzo").value(6.49)) 
				
				.andExpect(jsonPath("$.dettOrdine[1].id").exists())
				.andExpect(jsonPath("$.dettOrdine[1].codart").exists())
				.andExpect(jsonPath("$.dettOrdine[1].codart").value("000022601")) 
				.andExpect(jsonPath("$.dettOrdine[1].qta").exists())
				.andExpect(jsonPath("$.dettOrdine[1].qta").value(2.0)) 
				.andExpect(jsonPath("$.dettOrdine[1].prezzo").exists())
				.andExpect(jsonPath("$.dettOrdine[1].prezzo").value(0.49)) 
				
				.andExpect(jsonPath("$.dettOrdine[2].id").exists())
				.andExpect(jsonPath("$.dettOrdine[2].codart").exists())
				.andExpect(jsonPath("$.dettOrdine[2].codart").value("000020026")) 
				.andExpect(jsonPath("$.dettOrdine[2].qta").exists())
				.andExpect(jsonPath("$.dettOrdine[2].qta").value(24.0)) 
				.andExpect(jsonPath("$.dettOrdine[2].prezzo").exists())
				.andExpect(jsonPath("$.dettOrdine[2].prezzo").value(0.29)) 
				  
				.andDo(print());
	}
	
	@Test
	@Order(3)
	public void testDelOrd() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.delete(ApiBaseUrl + "/elimina/" + IdOrdine)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200 OK"))
				.andExpect(jsonPath("$.message").value(String.format("Eliminazione Ordine %s Eseguita Con Successo", IdOrdine)))
				.andDo(print());
	}

}

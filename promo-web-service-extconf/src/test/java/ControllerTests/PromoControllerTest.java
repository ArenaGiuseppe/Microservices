package ControllerTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Year;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import com.xantrix.webapp.Application;
import com.xantrix.webapp.entities.Promo;
import com.xantrix.webapp.repository.PromoRepository;

import lombok.extern.java.Log;


@ContextConfiguration(classes = Application.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Test Classe Controller Promo")
@TestPropertySource(properties = {"profilo = test", "seq = 1", "ramo = master"})
@Log
public class PromoControllerTest
{
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private PromoRepository promoRepository;
	
	private static boolean isInitialized = false;
	
	int Anno = Year.now().getValue();
	 
	String Codice = "TEST01";
	String Inizio = String.valueOf(Anno) + "-01-01";
	String Fine = String.valueOf(Anno) + "-12-31";
	
	String InizioScad = String.valueOf(Anno - 1) + "-01-01";
	String FineScad = String.valueOf(Anno - 1) + "-12-31";
	
	
	
		
	@BeforeEach
	public void setup()
	{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		
		if (isInitialized) return;
		
		//Eliminiamo tutte le promozioni eventualmente esistenti
		promoRepository.deleteAll();
		
		log.info(String.format("****** Numero Promozioni Esistenti: %s *******", promoRepository.findAll().size()));
		
		isInitialized = true;
	}
	
	String JsonData =
			"{\r\n"
			+ "	\"idPromo\": \"\",\r\n"
			+ "	\"anno\": " + Anno + ",\r\n"
			+ "	\"codice\": \"" + Codice + "\",\r\n"
			+ "	\"descrizione\": \"PROMO TEST01\",\r\n"
			+ "	\"dettPromo\": [\r\n"
			+ "		{\r\n"
			+ "			\"id\": -1,\r\n"
			+ "			\"riga\": 1,\r\n"
			+ "			\"codart\": \"049477701\",\r\n"
			+ "			\"codfid\": \"\",\r\n"
			+ "			\"inizio\": \"" + Inizio + "\",\r\n"
			+ "			\"fine\": \"" + Fine + "\",\r\n"
			+ "			\"oggetto\": \"1.10\",\r\n"
			+ "			\"isfid\": \"No\",\r\n"
			+ "			\"tipoPromo\": {\r\n"
			+ "				\"idTipoPromo\": \"1\"\r\n"
			+ "			}\r\n"
			+ "		},\r\n"
			+ "		{\r\n"
			+ "			\"id\": -1,\r\n"
			+ "			\"riga\": 2,\r\n"
			+ "			\"codart\": \"004590201\",\r\n"
			+ "			\"codfid\": \"\",\r\n"
			+ "			\"inizio\": \"" + Inizio + "\",\r\n"
			+ "			\"fine\": \"" + Fine + "\",\r\n"
			+ "			\"oggetto\": \"1.99\",\r\n"
			+ "			\"isfid\": \"Si\",\r\n"
			+ "			\"tipoPromo\": {\r\n"
			+ "				\"idTipoPromo\": \"1\"\r\n"
			+ "			}\r\n"
			+ "		},\r\n"
			+ "		{\r\n"
			+ "			\"id\": -1,\r\n"
			+ "			\"riga\": 3,\r\n"
			+ "			\"codart\": \"008071001\",\r\n"
			+ "			\"codfid\": \"67000076\",\r\n"
			+ "			\"inizio\": \"" + Inizio + "\",\r\n"
			+ "			\"fine\": \"" + Fine + "\",\r\n"
			+ "			\"oggetto\": \"2.19\",\r\n"
			+ "			\"isfid\": \"Si\",\r\n"
			+ "			\"tipoPromo\": {\r\n"
			+ "				\"idTipoPromo\": \"1\"\r\n"
			+ "			}\r\n"
			+ "		},\r\n"
			+ "		{\r\n"
			+ "			\"id\": -1,\r\n"
			+ "			\"riga\": 4,\r\n"
			+ "			\"codart\": \"002001601\",\r\n"
			+ "			\"codfid\": \"\",\r\n"
			+ "			\"inizio\": \"" + InizioScad + "\",\r\n"
			+ "			\"fine\": \"" + FineScad + "\",\r\n"
			+ "			\"oggetto\": \"0.99\",\r\n"
			+ "			\"isfid\": \"No\",\r\n"
			+ "			\"tipoPromo\": {\r\n"
			+ "				\"idTipoPromo\": \"1\"\r\n"
			+ "			}\r\n"
			+ "		}\r\n"
			+ "	],\r\n"
			+ "	\"depRifPromo\": [\r\n"
			+ "		{\r\n"
			+ "			\"id\": -1,\r\n"
			+ "			\"idDeposito\": 526\r\n"
			+ "		},\r\n"
			+ "		{\r\n"
			+ "			\"id\": -1,\r\n"
			+ "			\"idDeposito\": 525\r\n"
			+ "		}\r\n"
			+ "	]\r\n"
			+ "}";
	
	
	@Test
	@Order(1)
	@DisplayName("TEST CREAZIONE PROMO TEST01")
	public void testCreatePromo() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.post("/api/promo/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
		
		assertThat(promoRepository.findAll().size()).isEqualTo(1);
		
		log.info(String.format("****** Numero Promozioni Esistenti: %s *******", promoRepository.findAll().size()));
	}
	
	@Test
	@Order(2)
	@DisplayName("TEST SELEZIONE DI TUTTE LE PROMO")
	public void testlistAllPromo() throws Exception
	{
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/promo/")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(promoRepository.findAll().size())))
				
				//Verifichiamo la terza riga della promozione
				.andExpect(jsonPath("$[0].dettPromo[2].id").exists())
				.andExpect(jsonPath("$[0].dettPromo[2].riga").value("3"))
				.andExpect(jsonPath("$[0].dettPromo[2].codart").value("008071001"))
				.andExpect(jsonPath("$[0].dettPromo[2].codfid").value("67000076"))
				.andExpect(jsonPath("$[0].dettPromo[2].isfid").value("Si"))
				.andExpect(jsonPath("$[0].dettPromo[2].inizio").value(Inizio))
				.andExpect(jsonPath("$[0].dettPromo[2].fine").value(Fine))
				.andExpect(jsonPath("$[0].dettPromo[2].idTipoPromo").value("1")) 
				.andExpect(jsonPath("$[0].dettPromo[2].oggetto").value("2.19")) 
				
				.andDo(print());
		
		
	}
	
	@Test
	@Order(3)
	@DisplayName("TEST SELEZIONE PROMO PER ID")
	public void testlistPromoById() throws Exception
	{
		Promo promo = promoRepository.findByAnnoAndCodice(Anno, Codice);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/promo/id/" + promo.getIdPromo())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				
				.andExpect(jsonPath("$.idPromo").exists())
				.andExpect(jsonPath("$.idPromo").value(promo.getIdPromo()))
				
				 //Verifichiamo la prima riga della promozione
				.andExpect(jsonPath("$.dettPromo[0].id").exists())
				.andExpect(jsonPath("$.dettPromo[0].riga").value("1"))
				.andExpect(jsonPath("$.dettPromo[0].codart").value("049477701"))
				.andExpect(jsonPath("$.dettPromo[0].codfid").value(""))
				.andExpect(jsonPath("$.dettPromo[0].isfid").value("No"))
				.andExpect(jsonPath("$.dettPromo[0].inizio").value(Inizio))
				.andExpect(jsonPath("$.dettPromo[0].fine").value(Fine))
				.andExpect(jsonPath("$.dettPromo[0].idTipoPromo").value("1")) 
				.andExpect(jsonPath("$.dettPromo[0].oggetto").value("1.10")) 
				 
				//Verifichiamo la quarta riga della promozione
				.andExpect(jsonPath("$.dettPromo[3].id").exists())
				.andExpect(jsonPath("$.dettPromo[3].riga").value("4"))
				.andExpect(jsonPath("$.dettPromo[3].codart").value("002001601"))
				.andExpect(jsonPath("$.dettPromo[3].codfid").value(""))
				.andExpect(jsonPath("$.dettPromo[3].isfid").value("No"))
				.andExpect(jsonPath("$.dettPromo[3].inizio").value(InizioScad))
				.andExpect(jsonPath("$.dettPromo[3].fine").value(FineScad))
				.andExpect(jsonPath("$.dettPromo[3].idTipoPromo").value("1")) 
				.andExpect(jsonPath("$.dettPromo[3].oggetto").value("0.99")) 
				
				.andReturn();
	}
	
	private String GetNewData()
	{
		String IdPromo =  promoRepository.findByAnnoAndCodice(Anno, Codice).getIdPromo();
		
		String retVal = 
				"{\r\n"
				+ "	\"idPromo\": \"" + IdPromo + "\",\r\n"
				+ "	\"anno\": " + Anno + ",\r\n"
				+ "	\"codice\": \"" + Codice + "\",\r\n"
				+ "	\"descrizione\": \"PROMO TEST01 MODIFICA\",\r\n" //Modificata intestazione
				+ "	\"dettPromo\": [\r\n"
				+ "		{\r\n"
				+ "			\"id\": -1,\r\n"
				+ "			\"riga\": 1,\r\n"
				+ "			\"codart\": \"049477701\",\r\n"
				+ "			\"codfid\": \"\",\r\n"
				+ "			\"inizio\": \"" + Inizio + "\",\r\n"
				+ "			\"fine\": \"" + Fine + "\",\r\n"
				+ "			\"oggetto\": \"1.19\",\r\n" //Modificato prezzo promo
				+ "			\"isfid\": \"No\",\r\n"
				+ "			\"tipoPromo\": {\r\n"
				+ "				\"idTipoPromo\": \"1\"\r\n"
				+ "			}\r\n"
				+ "		},\r\n"
				+ "		{\r\n"
				+ "			\"id\": -1,\r\n"
				+ "			\"riga\": 2,\r\n"
				+ "			\"codart\": \"004590201\",\r\n"
				+ "			\"codfid\": \"\",\r\n"
				+ "			\"inizio\": \"" + Inizio + "\",\r\n"
				+ "			\"fine\": \"" + Fine + "\",\r\n"
				+ "			\"oggetto\": \"1.99\",\r\n"
				+ "			\"isfid\": \"No\",\r\n" //Modificato stato isfid
				+ "			\"tipoPromo\": {\r\n"
				+ "				\"idTipoPromo\": \"1\"\r\n"
				+ "			}\r\n"
				+ "		},\r\n"
				+ "		{\r\n"
				+ "			\"id\": -1,\r\n"
				+ "			\"riga\": 3,\r\n"
				+ "			\"codart\": \"008071001\",\r\n"
				+ "			\"codfid\": \"67000076\",\r\n"
				+ "			\"inizio\": \"" + Inizio + "\",\r\n"
				+ "			\"fine\": \"" + Fine + "\",\r\n"
				+ "			\"oggetto\": \"2.19\",\r\n"
				+ "			\"isfid\": \"Si\",\r\n"
				+ "			\"tipoPromo\": {\r\n"
				+ "				\"idTipoPromo\": \"1\"\r\n"
				+ "			}\r\n"
				+ "		},\r\n"
				+ "		{\r\n"
				+ "			\"id\": -1,\r\n"
				+ "			\"riga\": 4,\r\n"
				+ "			\"codart\": \"002001601\",\r\n"
				+ "			\"codfid\": \"\",\r\n"
				+ "			\"inizio\": \"" + InizioScad + "\",\r\n"
				+ "			\"fine\": \"" + FineScad + "\",\r\n"
				+ "			\"oggetto\": \"0.99\",\r\n"
				+ "			\"isfid\": \"No\",\r\n"
				+ "			\"tipoPromo\": {\r\n"
				+ "				\"idTipoPromo\": \"1\"\r\n"
				+ "			}\r\n"
				+ "		}\r\n"
				+ "	],\r\n"
				+ "	\"depRifPromo\": [\r\n"
				+ "		{\r\n"
				+ "			\"id\": -1,\r\n"
				+ "			\"idDeposito\": 526\r\n"
				+ "		},\r\n"
				+ "		{\r\n"
				+ "			\"id\": -1,\r\n"
				+ "			\"idDeposito\": 525\r\n"
				+ "		}\r\n"
				+ "	]\r\n"
				+ "}";
				
		return retVal;
	}
	
	@Test
	@Order(4)
	@DisplayName("TEST MODIFICA PROMO TEST01")
	public void testUpdatePromo() throws Exception
	{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/promo/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(GetNewData())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
		
		assertThat(promoRepository.findByAnnoAndCodice(Anno, Codice))
			.extracting(Promo::getDescrizione)
			.isEqualTo("PROMO TEST01 MODIFICA");
	}
	
	@Test
	@Order(5)
	@DisplayName("TEST SELEZIONE PROMO PER ANNO E CODICE")
	public void listPromoByCodice() throws Exception
	{
		String Url = String.format("/api/promo/codice?anno=%s&codice=%s", Anno,Codice);
		
		mockMvc.perform(MockMvcRequestBuilders.get(Url)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				
				.andExpect(jsonPath("$.idPromo").value(promoRepository.findByAnnoAndCodice(Anno, Codice).getIdPromo()))
				
				
				.andExpect(jsonPath("$.dettPromo[0].riga").value("1"))
				.andExpect(jsonPath("$.dettPromo[0].codart").value("049477701"))
				.andExpect(jsonPath("$.dettPromo[0].oggetto").value("1.19")) 
				.andExpect(jsonPath("$.dettPromo[0].isfid").value("No")) 
				
				
				.andExpect(jsonPath("$.dettPromo[1].riga").value("2"))
				.andExpect(jsonPath("$.dettPromo[1].codart").value("004590201"))
				.andExpect(jsonPath("$.dettPromo[1].oggetto").value("1.99")) 
				.andExpect(jsonPath("$.dettPromo[1].isfid").value("No")) 
				
				.andDo(print());
				
				
	}
	
	
	String JsonData2 = 
			"{\r\n"
			+ "	\"idPromo\": \"\",\r\n"
			+ "	\"anno\": " + Anno + ",\r\n"
			+ "	\"codice\": \"TEST02\",\r\n"
			+ "	\"descrizione\": \"PROMO TEST02 SCADUTA\",\r\n"
			+ "	\"dettPromo\": [\r\n"
			+ "		{\r\n"
			+ "			\"id\": -1,\r\n"
			+ "			\"riga\": 1,\r\n"
			+ "			\"codart\": \"049477701\",\r\n"
			+ "			\"codfid\": \"\",\r\n"
			+ "			\"inizio\": \"" + InizioScad + "\",\r\n"
			+ "			\"fine\": \"" + FineScad + "\",\r\n"
			+ "			\"oggetto\": \"1.19\",\r\n"
			+ "			\"isfid\": \"No\",\r\n"
			+ "			\"tipoPromo\": {\r\n"
			+ "				\"idTipoPromo\": \"1\"\r\n"
			+ "			}\r\n"
			+ "		},\r\n"
			+ "		{\r\n"
			+ "			\"id\": -1,\r\n"
			+ "			\"riga\": 2,\r\n"
			+ "			\"codart\": \"004590201\",\r\n"
			+ "			\"codfid\": \"\",\r\n"
			+ "			\"inizio\": \"" + InizioScad + "\",\r\n"
			+ "			\"fine\": \"" + FineScad + "\",\r\n"
			+ "			\"oggetto\": \"1.99\",\r\n"
			+ "			\"isfid\": \"No\",\r\n"
			+ "			\"tipoPromo\": {\r\n"
			+ "				\"idTipoPromo\": \"1\"\r\n"
			+ "			}\r\n"
			+ "		}\r\n"
			+ "	]\r\n"
			+ "}";
			
			
	@Test
	@Order(6)
	@DisplayName("TEST CREAZIONE PROMO TEST02 SCADUTA")
	public void testCreatePromo2() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.post("/api/promo/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData2)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
	}
	
	@Test
	@Order(7)
	@DisplayName("TEST SELEZIONE PROMO ATTIVE")
	public void testListPromoActive() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/promo/active")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1)))
				.andDo(print());
	}
	
	@Test
	@Order(8)
	@DisplayName("TEST ELIMINAZIONE PROMO")
	public void testDeletePromo() throws Exception
	{
		Promo promo = promoRepository.findByAnnoAndCodice(Anno, Codice);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/promo/elimina/" + promo.getIdPromo())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andDo(print());
	}
	
	
	@Test
	@Order(9)
	@DisplayName("TEST ERRORE RICERCA PROMO")
	public void ErrListPromoById() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/promo/id/ABC")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value(404))
				.andExpect(jsonPath("$.message").value("Promozione Assente o Id Errato"))
				.andDo(print());
	}
	
	
	@Test
	@Order(10)
	@DisplayName("TEST ELIMINAZIONE GENERALE PROMO")
	public void testDelAllPromo() throws Exception
	{
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/promo/elimina/all")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andDo(print());
		
		assertThat(promoRepository.findAll()).isEmpty();
	}
	
	
	
}

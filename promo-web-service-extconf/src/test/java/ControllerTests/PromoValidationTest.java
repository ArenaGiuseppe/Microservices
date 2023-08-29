package ControllerTests;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import com.xantrix.webapp.repository.PromoRepository;


@ContextConfiguration(classes = Application.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Test Validazione Inserimento Promo")
@TestPropertySource(properties = {"profilo = test", "seq = 1", "ramo = master"})
public class PromoValidationTest 
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
	
	@BeforeEach
	public void setup()
	{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		
		if (isInitialized) return;
		
		//Eliminiamo tutte le promozioni eventualmente esistenti
		promoRepository.deleteAll();
		
		isInitialized = true;
	}
	
	String JsonData = 
			"{\r\n"
			+ "	\"idPromo\": \"\",\r\n"
			+ "	\"anno\": 2022,\r\n"
			+ "	\"codice\": \"\",\r\n" //codice promo mancante
			+ "	\"descrizione\": \"PROMO TEST01\",\r\n"
			+ "	\"dettPromo\": [\r\n"
			+ "		{\r\n"
			+ "			\"id\": -1,\r\n"
			+ "			\"riga\": 1,\r\n"
			+ "			\"codart\": \"049477701\",\r\n"
			+ "			\"codfid\": \"\",\r\n"
			+ "			\"inizio\": \"2022-01-01\",\r\n"
			+ "			\"fine\": \"2022-12-31\",\r\n"
			+ "			\"oggetto\": \"1.59\",\r\n"
			+ "			\"isfid\": \"No\",\r\n"
			+ "			\"tipoPromo\": {\r\n"
			+ "				\"idTipoPromo\": \"1\"\r\n"
			+ "			}\r\n"
			+ "		}\r\n"
			+ "	]\r\n"
			+ "}";
	
	@Test
	@Order(1)
	@DisplayName("TEST ERRORE VALIDAZIONE CODICE")
	public void ErrInsPromoById1() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.post("/api/promo/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400))
				.andExpect(jsonPath("$.message").value("Il codice della promozione deve avere un numero di caratteri compreso fra 3 e 10"))
				.andDo(print());
	}
	
	String JsonData2 = "{\r\n"
			+ "	\"idPromo\": \"\",\r\n"
			+ "	\"anno\": 2022,\r\n"
			+ "	\"codice\": \"TEST01\",\r\n"
			+ "	\"descrizione\": \"\",\r\n" //DESCRIZIONE MANCANTE
			+ "	\"dettPromo\": [\r\n"
			+ "		{\r\n"
			+ "			\"id\": -1,\r\n"
			+ "			\"riga\": 1,\r\n"
			+ "			\"codart\": \"049477701\",\r\n"
			+ "			\"codfid\": \"\",\r\n"
			+ "			\"inizio\": \"2022-01-01\",\r\n"
			+ "			\"fine\": \"2022-12-31\",\r\n"
			+ "			\"oggetto\": \"1.59\",\r\n"
			+ "			\"isfid\": \"No\",\r\n"
			+ "			\"tipoPromo\": {\r\n"
			+ "				\"idTipoPromo\": \"1\"\r\n"
			+ "			}\r\n"
			+ "		}\r\n"
			+ "	]\r\n"
			+ "}";
	
	@Test
	@Order(2)
	@DisplayName("TEST ERRORE VALIDAZIONE DESCRIZIONE")
	public void ErrInsPromoById2() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.post("/api/promo/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData2)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400))
				.andExpect(jsonPath("$.message").value("E' necessario inserire la descrizione della promozione"))
				.andDo(print());
	}
	
	String JsonData3 = "{\r\n"
			+ "	\"idPromo\": \"\",\r\n"
			+ "	\"anno\": 2022,\r\n"
			+ "	\"codice\": \"TEST01\",\r\n"
			+ "	\"descrizione\": \"PROMO TEST01\",\r\n"
			+ "	\"dettPromo\": [\r\n"
			+ "		{\r\n"
			+ "			\"id\": -1,\r\n"
			+ "			\"riga\": 1,\r\n"
			+ "			\"codart\": \"\",\r\n" //CODICE ARTICOLO MANCANTE
			+ "			\"codfid\": \"\",\r\n"
			+ "			\"inizio\": \"2022-01-01\",\r\n"
			+ "			\"fine\": \"2022-12-31\",\r\n"
			+ "			\"oggetto\": \"1.59\",\r\n"
			+ "			\"isfid\": \"No\",\r\n"
			+ "			\"tipoPromo\": {\r\n"
			+ "				\"idTipoPromo\": \"1\"\r\n"
			+ "			}\r\n"
			+ "		}\r\n"
			+ "	]\r\n"
			+ "}";
			
	@Test
	@Order(3)
	@DisplayName("TEST ERRORE VALIDAZIONE CODICE ARTICOLO")
	public void ErrInsPromoById3() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.post("/api/promo/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData3)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400))
				.andExpect(jsonPath("$.message").value("Il codice articolo deve avere un numero di caratteri compreso tra 5 e 20"))
				.andDo(print());
	}
	
	String JsonData4 = "{\r\n"
			+ "	\"idPromo\": \"\",\r\n"
			+ "	\"anno\": 2022,\r\n"
			+ "	\"codice\": \"TEST01\",\r\n"
			+ "	\"descrizione\": \"PROMO TEST01\",\r\n"
			+ "	\"dettPromo\": [\r\n"
			+ "		{\r\n"
			+ "			\"id\": -1,\r\n"
			+ "			\"riga\": 1,\r\n"
			+ "			\"codart\": \"049477701\",\r\n"
			+ "			\"codfid\": \"\",\r\n"
			+ "			\"inizio\": \"2022-01-01\",\r\n"
			+ "			\"fine\": \"2022-12-31\",\r\n"
			+ "			\"oggetto\": \"\",\r\n"  //OGGETTO MANCANTE
			+ "			\"isfid\": \"No\",\r\n"
			+ "			\"tipoPromo\": {\r\n"
			+ "				\"idTipoPromo\": \"1\"\r\n"
			+ "			}\r\n"
			+ "		}\r\n"
			+ "	]\r\n"
			+ "}";
			
	@Test
	@Order(4)
	@DisplayName("TEST ERRORE VALIDAZIONE OGGETTO")
	public void ErrInsPromo() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.post("/api/promo/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData4)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400))
				.andExpect(jsonPath("$.message").value("E' necessario inserire l'oggetto della promozione"))
				.andDo(print());
	}
	
	String JsonData5 = "{\r\n"
			+ "	\"idPromo\": \"\",\r\n"
			+ "	\"anno\": 2022,\r\n"
			+ "	\"codice\": \"TEST01\",\r\n"
			+ "	\"descrizione\": \"PROMO TEST01\",\r\n"
			+ "	\"dettPromo\": [\r\n"
			+ "		{\r\n"
			+ "			\"id\": -1,\r\n"
			+ "			\"riga\": 1,\r\n"
			+ "			\"codart\": \"049477701\",\r\n"
			+ "			\"codfid\": \"\",\r\n"
			+ "			\"inizio\": \"2022-01-01\",\r\n"
			+ "			\"fine\": \"2022-12-31\",\r\n"
			+ "			\"oggetto\": \"1.89\",\r\n"
			+ "			\"isfid\": \"No\" \r\n"
			+ "		}\r\n"
			+ "	]\r\n"
			+ "}";
			
	@Test
	@Order(4)
	@DisplayName("TEST ERRORE VALIDAZIONE TIPO PROMOZIONE")
	public void ErrInsPromo2() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.post("/api/promo/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData5)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400))
				.andExpect(jsonPath("$.message").value("E' necessario inserire l'oggetto della promozione"))
				.andDo(print());
	}
	
	
}

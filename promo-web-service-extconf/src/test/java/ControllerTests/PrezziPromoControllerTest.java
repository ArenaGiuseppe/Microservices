package ControllerTests;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
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
import com.xantrix.webapp.entities.DettPromo;
import com.xantrix.webapp.entities.Promo;
import com.xantrix.webapp.entities.TipoPromo;
import com.xantrix.webapp.repository.PrezziPromoRepository;
import com.xantrix.webapp.repository.PromoRepository;
import com.xantrix.webapp.services.PromoService;


@ContextConfiguration(classes = Application.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Test Classe Controller PrezziPromo")
@TestPropertySource(properties = {"profilo = test", "seq = 1", "ramo = master"})
public class PrezziPromoControllerTest 
{
	private MockMvc mockMvc;

    @Autowired
	private WebApplicationContext wac;
    
    @Autowired
	private PromoRepository promoRepository;
    
    @Autowired
   	private PromoService promoService;
    
    @Autowired
	private PrezziPromoRepository prezziPromoRepository;
	
    
    int Anno = Year.now().getValue();
	
	String IdPromo = "";
	String Codice = "TEST01";
	String Descrizione = "PROMO TEST1";
	
	private static boolean isInitialized = false;
	private static boolean isTerminated = false;
	
	@BeforeEach
	public void setup() 
			throws ParseException
	{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		
		if (isInitialized) return;
		
		//Nota: Tutte le promo vengono eliminate
		promoRepository.deleteAll();
		
		UUID uuid = UUID.randomUUID();
		IdPromo = uuid.toString();
		
		Promo promo = new Promo(IdPromo,Anno,Codice,Descrizione);
		promoRepository.save(promo);
		
		//La promo sarà valida l'intero anno corrente
		Date Inizio = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(Anno) + "-01-01");  
		Date Fine = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(Anno) + "-12-31");
		
		DettPromo dettPromo = new DettPromo();
		
		promo = promoRepository.findByAnnoAndCodice(Anno, Codice);
		
		//riga 1 promozione standard
		dettPromo.setId(-1);
		dettPromo.setInizio(Inizio);
		dettPromo.setFine(Fine);
		dettPromo.setCodart("049477701");
		dettPromo.setOggetto("1.10");
		dettPromo.setIsfid("No");
		dettPromo.setRiga(1);
		dettPromo.setTipoPromo(new TipoPromo(1));
		
		dettPromo.setPromo(promo);
		
		prezziPromoRepository.save(dettPromo);
		
		//riga 2 promozione fidelity
		dettPromo.setId(-1);
		dettPromo.setInizio(Inizio);
		dettPromo.setFine(Fine);
		dettPromo.setCodart("004590201");
		dettPromo.setOggetto("1.99");
		dettPromo.setIsfid("Si");
		dettPromo.setRiga(2);
		dettPromo.setTipoPromo(new TipoPromo(1));
		
		dettPromo.setPromo(promo);
		
		prezziPromoRepository.save(dettPromo);
		
		//riga 3 promozione fidelity Only You
		dettPromo.setId(-1);
		dettPromo.setInizio(Inizio);
		dettPromo.setFine(Fine);
		dettPromo.setCodart("008071001");
		dettPromo.setOggetto("2.19");
		dettPromo.setIsfid("Si");
		dettPromo.setCodfid("67000076");
		dettPromo.setRiga(3);
		dettPromo.setTipoPromo(new TipoPromo(1));
		
		dettPromo.setPromo(promo);
		
		prezziPromoRepository.save(dettPromo);
		
		Anno = Anno - 1; //assicuriamoci che la promo sia scaduta
		
		Inizio = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(Anno) + "-01-01");  
		Fine = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(Anno) + "-12-31");
		
		//riga 4 promozione standard scaduta
		dettPromo.setId(-1);
		dettPromo.setInizio(Inizio);
		dettPromo.setFine(Fine);
		dettPromo.setCodart("002001601");
		dettPromo.setOggetto("0.99");
		dettPromo.setIsfid("No");
		dettPromo.setRiga(4);
		dettPromo.setTipoPromo(new TipoPromo(1));
		
		dettPromo.setPromo(promo);
		
		prezziPromoRepository.save(dettPromo);
		
		isInitialized = true;
		
	}
	
	private void InsertPromo2() throws ParseException
	{
		String Codice = "TEST02";
		String Descrizione = "PROMO TEST2";
		
		Promo promo = new Promo(IdPromo,Anno,Codice,Descrizione);
		promoRepository.save(promo);
		
		//La promo sarà valida l'intero anno corrente
		Date Inizio = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(Anno) + "-01-01");  
		Date Fine = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(Anno) + "-12-31");
		
		DettPromo dettPromo = new DettPromo();
		
		promo = promoRepository.findByAnnoAndCodice(Anno, Codice);
		
		//riga 1 promozione standard
		dettPromo.setId(-1);
		dettPromo.setInizio(Inizio);
		dettPromo.setFine(Fine);
		dettPromo.setCodart("049477701");
		dettPromo.setOggetto("0.89"); //Prezzo inferiore a parità di condizioni
		dettPromo.setIsfid("No");
		dettPromo.setRiga(1);
		dettPromo.setTipoPromo(new TipoPromo(1));
		
		dettPromo.setPromo(promo);
		
		prezziPromoRepository.save(dettPromo);
	}
	
	@Test
	@Order(1)
	@DisplayName("Test Prezzo per Codice Articolo")
	public void testGetPromoCodArt() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/promo/prezzo/049477701")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").value("1.1")) 
			.andReturn();
	}
	
	@Test
	@Order(2)
	@DisplayName("Test Prezzo per Codice Articolo riservato a fidelity")
	public void testGetPromoCodArtFid() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/promo/prezzo/fidelity/004590201")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").value("1.99")) 
			.andReturn();
	}
	
	@Test
	@Order(3)
	@DisplayName("Test Prezzo per Codice Articolo e fidelity")
	public void testGetPromoCodArtCodFid() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/promo/prezzo/008071001/67000076")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").value("2.19")) 
			.andReturn();
	}
	
	@Test
	@Order(4)
	@DisplayName("Test Prezzo Promo Scaduta")
	public void testGetPromoScad() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/promo/prezzo/002001601")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").value("0.0")) 
			.andReturn();
	}
	
	
	
	@Test
	@Order(5)
	@DisplayName("Test Prezzo per Codice Articolo con due promo attive")
	public void testGetPromoCodArt2() throws Exception
	{
		
		//Eliminiamo la cache
		promoService.CleanCaches();
				
		this.InsertPromo2();
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/promo/prezzo/049477701")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").value("0.89")) //E' necessario ottenere il prezzo minore a parità di condizioni
			.andReturn();
		
		isTerminated = true;
	}
	
	
	
	@AfterEach
	public void DelPromo()
	{
		
		if (isTerminated)
			promoRepository.deleteAll();
			
	}
}

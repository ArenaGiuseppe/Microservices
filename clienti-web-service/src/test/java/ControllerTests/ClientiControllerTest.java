package ControllerTests;

 
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import com.xantrix.webapp.entities.Cards;
import com.xantrix.webapp.entities.Clienti;
import com.xantrix.webapp.repository.ClientiRepository;

@SpringBootTest
@TestPropertySource(properties = {"profilo = test", "seq = 1", "ramo = master"})
@ContextConfiguration(classes = Application.class)
@TestMethodOrder(OrderAnnotation.class)
public class ClientiControllerTest 
{
	
	private MockMvc mockMvc;
 
	private static boolean isInitialized = false;
	
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Date data = DateTime.now().toDate();

	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private ClientiRepository clientiRepository;
	
	@BeforeEach
	public void setup() throws JSONException, IOException
	{
		
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(wac)
				.build();
		
		if (isInitialized)
			return;
		
		Clienti cliente = new Clienti();
		cliente.setCodice("65000000");
		cliente.setNome("Nicola");
		cliente.setCognome("La Rocca");
		cliente.setIndirizzo("Via dei Mille, 53  ");
		cliente.setCap("07100");
		cliente.setTelefono("334-123456789");
		cliente.setMail("prova@tiscali.it");
		cliente.setComune("Sassari");
		cliente.setStato("1");
		
		Cards card = new Cards();
		card.setCodice("65000000");
		card.setBollini(0);
		card.setUltimaspesa(data);
		card.setObsoleto("No");
		
		cliente.setCard(card);
		
		clientiRepository.save(cliente);
		
	}
	
	/*
	private LocalDate convertToLocalDate(Date dateToConvert) 
	{
	    return LocalDate.ofInstant(dateToConvert.toInstant(), 
	    		ZoneId.systemDefault());
	}
	*/
	
	private String ApiBaseUrl = "/api/clienti";
	
	String JsonData = 
			"{\n"
			+ "    \"codice\": \"65000000\",\n"
			+ "    \"nominativo\": \"Nicola La Rocca\",\n"
			+ "    \"indirizzo\": \"Via dei Mille, 53\",\n"
			+ "    \"comune\": \"Sassari\",\n"
			+ "    \"cap\": \"07100\",\n"
			+ "    \"telefono\": \"334-123456789\",\n"
			+ "    \"mail\": \"prova@tiscali.it\",\n"
			+ "    \"stato\": \"1\",\n"
			+ "    \"bollini\": 0,\n"
			+ "    \"ultimaSpesa\": \"" + dateFormat.format(data) + "\"\n"
			+ "}";
	
	@Test
	@Order(1)
	public void testListCliByCode() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/cerca/codice/65000000")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(JsonData)) 
				.andReturn();
		
		isInitialized = true;
	}
	
	@Test
	@Order(2)
	public void testModMonteBollAum() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.put(ApiBaseUrl + "/cards/modifica/65000000/500")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();
		
		Cards card = clientiRepository.findByCodice("65000000").getCard();
		
		assertThat(card.getBollini())
		.isEqualTo(500);
	}
	
	@Test
	@Order(3)
	//@Disabled
	public void testModMonteBollDim() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.put(ApiBaseUrl + "/cards/modifica/65000000/-500")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();
		
		Cards card = clientiRepository.findByCodice("65000000").getCard();
		
		assertThat(card.getBollini())
		.isEqualTo(0);
		
		//clientiRepository.delete(clientiRepository.findByCodice("65000000"));
	}
	
}

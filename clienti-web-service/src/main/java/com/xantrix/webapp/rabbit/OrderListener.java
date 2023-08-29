package com.xantrix.webapp.rabbit;
 
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

 
import com.xantrix.webapp.dtos.OrderDto;
import com.xantrix.webapp.services.ClientiService;

import lombok.extern.java.Log;


@Component
@Log
public class OrderListener 
{
	@Autowired
	ClientiService clientiService;
	
	@Autowired
	FidelityProperties fidelityProperties;
	
	@RabbitListener(queues = "alphashop.order.queue") 
	public void receiveOrder(OrderDto ordine, @Header("X_ORDER_SOURCE") String Source) 
	{
		int bollini = 0;
		
		log.info(String.format("Ricevuto Ordine %s di euro %s", ordine.getId(), ordine.getTotale()));
		
		if (ordine.getIdcliente().length() > 0 && ordine.getTotale() >= fidelityProperties.getValminimo())
		{
			log.info(String.format("***** Calcolo Bollini Cliente %s *****", ordine.getIdcliente()));
		   
		    bollini = (int) ordine.getTotale() / fidelityProperties.getValbollino();
		    
		    if (Source.toUpperCase().equals("MOBILE"))
		    {	log.info(String.format("Fonte %s. Eseguo Bonus Bollini!", Source));
		    	bollini *= fidelityProperties.getBonusmobile();
		    }
		    
		    clientiService.UpdMonteBollini(ordine.getIdcliente(), bollini);
		    
		    log.info(String.format("Modificato monte bollini fidelity %s di %s bollini", ordine.getIdcliente(), bollini));
		}
		else
		{
			log.warning("Impossibile modificare il monte bollini ");
		}
	}	
}

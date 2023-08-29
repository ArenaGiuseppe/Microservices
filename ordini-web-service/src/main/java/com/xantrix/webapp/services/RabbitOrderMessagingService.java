package com.xantrix.webapp.services;

import org.springframework.stereotype.Service;

import com.xantrix.webapp.dtos.OrderDto;
import com.xantrix.webapp.rabbit.RabbitMQProperties;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class RabbitOrderMessagingService implements OrderMessagingService
{
	private RabbitTemplate rabbit;
	
	@Autowired
	private RabbitMQProperties rabbitMQProperties;
	
	@Autowired
	public RabbitOrderMessagingService(RabbitTemplate rabbit) 
	{
		this.rabbit = rabbit;
	}
	
	/*
	public void sendOrder(OrderDTO ordine) 
	{
		MessageConverter converter = rabbit.getMessageConverter();
		MessageProperties props = new MessageProperties();
		Message message = converter.toMessage(ordine, props);
		
		rabbit.send("alphashop.orders", message);
	}
	**/
	
	public void sendOrder(OrderDto ordine) 
	{
	    rabbit.convertAndSend(rabbitMQProperties.getQueueName(), ordine,
	        new MessagePostProcessor() 
	    	{
		          @Override
		          public Message postProcessMessage(Message message)
		              throws AmqpException 
		          {
			            MessageProperties props = message.getMessageProperties();
			            props.setHeader("X_ORDER_SOURCE", ordine.getFonte());
			            
			            return message;
		          } 
	        });
	  }
}

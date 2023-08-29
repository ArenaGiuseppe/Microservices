package com.xantrix.webapp.rabbit;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class QueueConfig 
{
	@Autowired
	private RabbitMQProperties rabbitMQProperties;
	
	@Bean
    public Jackson2JsonMessageConverter converter() 
	{
        return new Jackson2JsonMessageConverter();
    }
	
	private AmqpAdmin amqpAdmin;

    public QueueConfig(AmqpAdmin amqpAdmin) 
    {
        this.amqpAdmin = amqpAdmin;
    }

    @PostConstruct
    public void createQueues() 
    {
        amqpAdmin.declareQueue(new Queue(rabbitMQProperties.getQueueName(), true));
    }
}

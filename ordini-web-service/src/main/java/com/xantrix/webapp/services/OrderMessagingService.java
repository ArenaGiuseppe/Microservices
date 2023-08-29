package com.xantrix.webapp.services;

import com.xantrix.webapp.dtos.OrderDto;

public interface OrderMessagingService 
{
	public void sendOrder(OrderDto ordine); 
}

package com.xantrix.webapp.dtos;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class OrderDto implements Serializable
{
	private static final long serialVersionUID = 1407680853764474369L;
	
	public String id;
	public Date data;
	public double totale;
	public String idcliente;
	public String fonte;
		
}

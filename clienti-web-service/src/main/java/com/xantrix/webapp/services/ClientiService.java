package com.xantrix.webapp.services;

import java.util.List;

import com.xantrix.webapp.dtos.ClientiDto;
import com.xantrix.webapp.entities.Clienti;

 
public interface ClientiService 
{
	public ClientiDto SelByIdCliente(String idcliente);
	
	public List<ClientiDto> SelByCognome(String cognome);
	
	public void UpdMonteBollini(String codfid, int bollini);
	
	public void SaveCliente(Clienti cliente);
	
	
}

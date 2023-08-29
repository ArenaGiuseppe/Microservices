package com.xantrix.webapp.dtos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import lombok.Data;

@Data
public class ClientiDto implements Serializable
{
	private static final long serialVersionUID = 7035348227991650532L;
	
	private String codice;
	private String nominativo;
	private String indirizzo;
	private String comune;
	private String cap;
	private String telefono;
	private String mail;
	private String stato;
	
	private int bollini;
	private Date ultimaSpesa;
}

package com.xantrix.webapp.dtos;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class DettPromoDto  implements Serializable
{
	 
	private static final long serialVersionUID = -154594774962553053L;
	
	private long id;
	private int riga;
	private String codart;
	private String desart;
	private double prezzo;
	private String codfid = "";
	private String isfid;
	private Date inizio;
	private Date fine;
	private int idTipoPromo;
	private String oggetto;
}

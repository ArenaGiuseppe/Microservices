package com.xantrix.webapp.dtos;


import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class PromoDto  implements Serializable
{
	 
	private static final long serialVersionUID = 7793131791757500569L;
	
	private String idPromo;
	private int anno;
	private String codice;
	private String descrizione;
	
	private List<DettPromoDto> dettPromo;
	private Set<DepRifPromoDto> depRifPromo = new HashSet<>();
}

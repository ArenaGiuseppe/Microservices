package com.xantrix.webapp.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "promo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Promo  implements Serializable
{
	private static final long serialVersionUID = -2077445225617424877L;

	@Id
	@Column(name = "IDPROMO")
	private String idPromo;
	
	private int anno;
	
	//@NotBlank(message = "{NotBlank.Promo.codice.Validation}")
	@Size(min = 3, max = 10, message = "{Size.Articoli.descrizione.Validation}")
	private String codice;
	
	@NotBlank(message = "{NotBlank.Promo.descrizione.Validation}")
	private String descrizione;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  mappedBy = "promo",  orphanRemoval = true)
	@JsonManagedReference
	@OrderBy("riga ASC") 
	@Valid
	private Set<DettPromo> dettPromo = new HashSet<>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  mappedBy = "promo", orphanRemoval = true)
	@JsonManagedReference
	private Set<DepRifPromo> depRifPromo = new HashSet<>();
	
 
	public Promo(String IdPromo, int Anno, String Codice, String Descrizione)
	{
		this.idPromo = IdPromo;
		this.anno = Anno;
		this.codice = Codice;
		this.descrizione = Descrizione;
	}
}

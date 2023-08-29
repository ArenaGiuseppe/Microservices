package com.xantrix.webapp.entities;

import java.io.Serializable;
import java.util.Date;
 
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CLIENTI")
@Getter
@Setter
public class Clienti implements Serializable
{
	private static final long serialVersionUID = -4995740052474979576L;

	@Id
	@Column(name = "codfidelity")
	private String codice;
	
	//@Max(value = 50, message = "{Max.Clienti.nome.Validation}")
	@NotBlank(message = "{@NotBlank.Clienti.nome.Validation}")
	private String nome;
	
	//@Max(value = 60, message = "{Max.Clienti.cognome.Validation}")
	@NotBlank(message = "{@NotBlank.Clienti.cognome.Validation}")
	private String cognome;
	
	@Basic
	private String indirizzo;
	
	@Basic
	private String comune;
	
	@Basic
	private String cap;
	
	@Basic
	private String telefono;
	
	@Basic
	private String mail;
	
	@Basic
	private String stato = "1";
	
	@Temporal(TemporalType.DATE)
	@Basic
	private Date datacreaz;
	
	@OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	@Valid
	private Cards card;
	
	
}

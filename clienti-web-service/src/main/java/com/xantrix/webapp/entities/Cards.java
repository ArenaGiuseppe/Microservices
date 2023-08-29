package com.xantrix.webapp.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CARDS")
@Getter
@Setter
public class Cards implements Serializable
{
	private static final long serialVersionUID = -2044872204293485177L;

	@Id
	@Column(name = "codfidelity")
	private String codice;
	
	@Basic
	@Min(value = 0, message = "{Min.Cards.bollini.Validation}")
	private int bollini = 0;
	
	@Temporal(TemporalType.DATE)
	private Date ultimaspesa;
	
	@Basic
	private String obsoleto = "No";
	
	@OneToOne
	@PrimaryKeyJoinColumn 
	@JsonBackReference
	private Clienti cliente;
	
	
}

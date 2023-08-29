package com.xantrix.webapp.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
 

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "DETTORDINI")
@Getter
@Setter
public class DettOrdini 
{
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotBlank(message = "{NotBlank.DettOrdini.codart.Validation}")
	private String codart;
	
	@Min(value = (long) 0.01, message = "{Min.DettOrdini.qta.Validation}")
	private double qta;
	
	private double prezzo;
	
	@ManyToOne
	@JoinColumn(name = "IDORDINE", referencedColumnName = "id")
	@JsonBackReference 
	private Ordini ordine;
}

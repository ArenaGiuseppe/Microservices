package com.xantrix.webapp.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ORDINI")
@Getter
@Setter
public class Ordini implements Serializable
{
	private static final long serialVersionUID = 3127983680192346940L;
	
	@Id
	@NotNull(message = "{NotNull.Ordini.id.Validation}")
	private String id;
	
	//@Temporal(TemporalType.DATE)
	private LocalDate data;
	 
	private int idpdv;
	 
	private String codfid;
	
	private int stato;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  mappedBy = "ordine",  orphanRemoval = true)
	@OrderBy("id desc") 
	@JsonManagedReference
	@Valid
	private List<DettOrdini> dettOrdine;

}

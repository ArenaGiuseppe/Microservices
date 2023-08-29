package com.xantrix.webapp.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;


import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dettpromo")
@Getter
@Setter
public class DettPromo implements Serializable
{
	private static final long serialVersionUID = 7444232363326102441L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Min(value = 0,  message = "{Min.DettPromo.riga.Validation}")
	private int riga;
	
	@Size(min = 5, max = 20, message = "{Size.DettPromo.codart.Validation}")
	@NotNull(message = "{NotNull.DettPromo.codart.Validation}")
	private String codart;
	
	private String codfid = "";
		
	@Column(name = "INIZIO")
	@Temporal(TemporalType.DATE)
	private Date inizio;
	
	@Column(name = "FINE")
	@Temporal(TemporalType.DATE)
	private Date fine;
	
	@NotBlank(message = "{NotBlank.DettPromo.oggetto.Validation}")
	private String oggetto;
	
	@Basic
	private String isfid = "No";
	
	@ManyToOne
	@JoinColumn(name = "IDPROMO", referencedColumnName = "idPromo")
	@JsonBackReference 
	private Promo promo;
	
	@ManyToOne
	@JoinColumn(name = "IDTIPOPROMO", referencedColumnName = "idTipoPromo")
	@NotNull(message = "{NotNull.DettPromo.tipoPromo.Validation}")
	private TipoPromo tipoPromo;

}

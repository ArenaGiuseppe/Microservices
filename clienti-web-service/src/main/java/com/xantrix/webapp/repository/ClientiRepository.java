package com.xantrix.webapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xantrix.webapp.entities.Clienti;


public interface ClientiRepository extends JpaRepository<Clienti,String>
{
	Clienti findByCodice(String codfid);
	
	List<Clienti> findByCognomeLike(String cognome);
	
	@Query(value = "SELECT a FROM Clienti a WHERE a.cognome LIKE :Cognome")
	List<Clienti> selByCognomeLike(@Param("Cognome") String cognome);
	
	//Query SQL - Modifica monte bollini tabella Cards
	@Modifying
	@Query(value = "UPDATE cards SET bollini = bollini + (:bollini), ultimaspesa = now() WHERE codfidelity = :codfidelity", nativeQuery = true)
	void updMonteBollini(@Param("codfidelity") String codfid, @Param("bollini") int bollini);
		
}

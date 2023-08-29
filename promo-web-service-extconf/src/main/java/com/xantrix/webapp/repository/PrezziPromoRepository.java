package com.xantrix.webapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xantrix.webapp.entities.DettPromo;
 
public interface PrezziPromoRepository extends JpaRepository<DettPromo, Long>
{
	
	//Query JPQL - Selezione promo per codice articolo
	@Query(value = "SELECT b FROM Promo a JOIN a.dettPromo b WHERE b.codart = :codart AND b.isfid = 'No' AND " + 
				   "CURRENT_DATE BETWEEN b.inizio AND b.fine ORDER BY b.oggetto DESC")
	List<DettPromo> SelByCodArt(@Param("codart") String CodArt);
	
	
	//Query JPQL - Selezione promo fidelity per codice articolo
	@Query(value = "SELECT b FROM Promo a JOIN a.dettPromo b WHERE b.codart = :codart AND b.isfid = 'Si' AND " + 
	               "CURRENT_DATE BETWEEN b.inizio AND b.fine ORDER BY b.oggetto DESC")
	List<DettPromo> SelByCodArtAndFid(@Param("codart") String CodArt);
	
		
	//Query JPQL - Selezione promo per codice fidelity e codice
	@Query(value = "SELECT b FROM Promo a JOIN a.dettPromo b WHERE b.codart = :codart AND b.codfid = :codfid AND " +
				   "CURRENT_DATE BETWEEN b.inizio AND b.fine ORDER BY b.oggetto DESC")
	List<DettPromo> SelByCodArtAndCodFid(@Param("codart") String CodArt, @Param("codfid") String CodFid);
	
	//Query SQL - Modifica oggetto promozione (prezzo)
	@Modifying
	@Query(value = "UPDATE dettpromo SET OGGETTO = :oggetto WHERE ID = :id", nativeQuery = true)
	void UpdOggettoPromo(@Param("oggetto") String Oggetto, @Param("id") Long Id);
	
	/*
	@Query("SELECT DISTINCT b FROM Promo a JOIN a.dettPromo b WHERE CURRENT_DATE BETWEEN b.inizio AND b.fine")
	List<DettPromo> SelPromoActive();
	*/
	
		
}

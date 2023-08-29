package com.xantrix.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xantrix.webapp.entities.Ordini;

public interface OrdiniRepository extends JpaRepository<Ordini, String>
{
	@Query(value = "SELECT SUM(qta * prezzo) FROM dettordini WHERE idordine = :id", nativeQuery = true)
	Double GetValOrdine(@Param("id") String IdOrdine);
}

package com.dock.teste.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.dock.teste.models.Contas;

public interface ContaRepository extends JpaRepository<Contas, Integer> {
	
	
	
}

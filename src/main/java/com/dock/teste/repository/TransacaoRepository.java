package com.dock.teste.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dock.teste.models.Contas;
import com.dock.teste.models.Transacoes;

public interface TransacaoRepository extends JpaRepository<Transacoes, Integer> {
	public static final EntityManager em = null;
	
	@SuppressWarnings("unchecked")
	public static List<Transacoes> findByIdConta(Integer id){
		Contas conta = new Contas();
		Query query = em.createQuery("from TB_TRANSACAO t where t.CD_CONTA = ?");
		query.setParameter(1, id);
		conta.setTransacoes(query.getResultList());
		return conta.getTransacoes();
	}
	

}

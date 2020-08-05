package com.dock.teste.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dock.teste.models.Contas;
import com.dock.teste.models.Transacoes;
import com.dock.teste.repository.ContaRepository;
import com.dock.teste.repository.TransacaoRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value="/api")
@Api(value="API REST Teste Dock")
public class TesteController {
	
	@Autowired
	ContaRepository contaRepository;
	
	@Autowired
	TransacaoRepository transacaoRepository;
	
	

	@ApiOperation(value="Criar uma Conta")
	@PostMapping("/criarConta")
	public Contas criarConta(@RequestBody @Validated Contas conta) {
		return contaRepository.save(conta);
	}
	
	
	@ApiOperation(value="Criar uma Transação de depósito")
	@PostMapping("/deposito")
	public Transacoes deposito(@RequestBody @Validated Transacoes transacao) {
		Transacoes retorno = transacaoRepository.save(transacao);
		Optional<Contas> contas = contaRepository.findById(transacao.getConta().getIdConta());
		if(contas != null) {
			Contas conta = contas.get();
			conta.setSaldo(conta.getSaldo().add(transacao.getValor()));
			contaRepository.save(conta);
		}
		return retorno;
	}
	
	@ApiOperation(value="Criar uma Transação de retirada")
	@PostMapping("/retirada")
	public Transacoes retirada(@RequestBody @Validated Transacoes transacao) {
		Transacoes retorno = transacaoRepository.save(transacao);
		Optional<Contas> contas = contaRepository.findById(transacao.getConta().getIdConta());
		if(contas != null) {
			Contas conta = contas.get();
			
			conta.setSaldo(conta.getSaldo().subtract(transacao.getValor()));
			contaRepository.save(conta);
		}
		return retorno;
	}
	
	@ApiOperation(value="Retorna o saldo da conta")
	@GetMapping("/saldo/{id}")
	public BigDecimal consultaSaldo(@PathVariable(value="id") Integer id){

		Optional<Contas> contas = contaRepository.findById(id);
		
		return contas.get().getSaldo();
	}
	
	@ApiOperation(value="Bloqueio de conta")
	@PutMapping("/bloqueioConta/{id}")
	public Contas bloqueioConta(@PathVariable(value="id") Integer id) {
		
		Contas conta = contaRepository.findById(id).get();
		conta.setFlagAtivo(false);
		
		return contaRepository.save(conta);
		
	}
	
	@ApiOperation(value="Desbloqueio de conta")
	@PutMapping("/desbloqueioConta/{id}")
	public Contas desbloqueioConta(@PathVariable(value="id") Integer id) {
		
		Contas conta = contaRepository.findById(id).get();
		conta.setFlagAtivo(true);
		
		return contaRepository.save(conta);
		
	}
	
	@ApiOperation(value="Gerar extrato de transações da conta")
	@GetMapping("/gerarExtrato/{id}")
	public List<Transacoes> gerarExtrato(@PathVariable(value="id") Integer id) {
		
		Contas conta = contaRepository.findById(id).get();
		
		conta.setTransacoes(TransacaoRepository.findByIdConta(conta.getIdConta()));
		
		return conta.getTransacoes();
	}
	
}

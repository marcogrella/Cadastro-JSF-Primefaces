package br.com.framework.utils;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class UtilFramework implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	
	
	/* O uso da thread � para verificar e filtrar quem est� acessando tal recurso, auxilia 
	 * na verifica��o do usu�rio que est� inserindo, excluindo e editando os registros no BD
	 * No caso iremos pegar o id do usu�rio. Essa classe ir� trabalhar em conjunto com os listeners (envers) de auditoria,
	 * que est� configurado no  arquivo hibernate.cfg.xml
	 * 
	 * */
	private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();
	
	public synchronized static ThreadLocal<Long> getThreadLocal(){
		return threadLocal;
	}
	

}

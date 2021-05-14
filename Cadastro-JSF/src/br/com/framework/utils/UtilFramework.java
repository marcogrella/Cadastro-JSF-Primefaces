package br.com.framework.utils;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class UtilFramework implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	
	
	/* O uso da thread é para verificar e filtrar quem está acessando tal recurso, auxilia 
	 * na verificação do usuário que está inserindo, excluindo e editando os registros no BD
	 * No caso iremos pegar o id do usuário. Essa classe irá trabalhar em conjunto com os listeners (envers) de auditoria,
	 * que está configurado no  arquivo hibernate.cfg.xml
	 * 
	 * */
	private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();
	
	public synchronized static ThreadLocal<Long> getThreadLocal(){
		return threadLocal;
	}
	

}

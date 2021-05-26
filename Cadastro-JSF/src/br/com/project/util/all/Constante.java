package br.com.project.util.all;

import java.io.Serializable;

public class Constante implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	/* 
	 * os atributos podem ser acessados de qualquer lugar e são constantes
	 * não há necessidade de se criar um objeto e por isso são estáticos e 
	 * finais
	 *  */
	
	public static final String SUCESSO = "sucesso";
	public static final String OPERACAO_REALIZADA_COM_SUCESSO = "Operação Realizada com Sucesso";
	public static final String ERRO_NA_OPERACAO = "Não foi possível executar a operação";
	
	
}

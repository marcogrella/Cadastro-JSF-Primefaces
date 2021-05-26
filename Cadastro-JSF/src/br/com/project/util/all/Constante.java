package br.com.project.util.all;

import java.io.Serializable;

public class Constante implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	/* 
	 * os atributos podem ser acessados de qualquer lugar e s�o constantes
	 * n�o h� necessidade de se criar um objeto e por isso s�o est�ticos e 
	 * finais
	 *  */
	
	public static final String SUCESSO = "sucesso";
	public static final String OPERACAO_REALIZADA_COM_SUCESSO = "Opera��o Realizada com Sucesso";
	public static final String ERRO_NA_OPERACAO = "N�o foi poss�vel executar a opera��o";
	
	
}

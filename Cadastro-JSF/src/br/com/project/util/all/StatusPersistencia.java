package br.com.project.util.all;


public enum StatusPersistencia {

	ERRO("Erro"), 
	SUCESSO("Sucesso"),
	OBJETO_REFERENCIADO("Esse objeto n�o pode ser apagado por possuir refer�ncias ao mesmo.");
	
	private String name;
	
	private StatusPersistencia(String s) {
		this.name = s;
	}
	
	/* O toString tr�s o valor que est� configurado em rela��o a constante
	 * exemplo se invocar ERRO trar� "Erro"
	 * */
	@Override
	public String toString() {
		
		return this.name();
	}
	
}

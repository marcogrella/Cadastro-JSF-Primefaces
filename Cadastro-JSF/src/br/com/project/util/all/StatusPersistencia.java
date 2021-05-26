package br.com.project.util.all;


public enum StatusPersistencia {

	ERRO("Erro"), 
	SUCESSO("Sucesso"),
	OBJETO_REFERENCIADO("Esse objeto não pode ser apagado por possuir referências ao mesmo.");
	
	private String name;
	
	private StatusPersistencia(String s) {
		this.name = s;
	}
	
	/* O toString trás o valor que está configurado em relação a constante
	 * exemplo se invocar ERRO trará "Erro"
	 * */
	@Override
	public String toString() {
		
		return this.name();
	}
	
}

package br.com.project.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public abstract @interface IdentificaCampoPesquisa {

	String descricaoCampo(); /* descri��o do campo para a tela */
	String campoConsulta(); /* campo do banco */
	int principal() default 10000; /* posi��o que ir� aparecer no combo por default */ 
	
	/* se n�o colocar a op��o default, qualquer uma destas ir� a parecer como 
	 * a primeira op��o no combo. */
}

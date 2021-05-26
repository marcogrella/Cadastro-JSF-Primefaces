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

	String descricaoCampo(); /* descrição do campo para a tela */
	String campoConsulta(); /* campo do banco */
	int principal() default 10000; /* posição que irá aparecer no combo por default */ 
	
	/* se não colocar a opção default, qualquer uma destas irá a parecer como 
	 * a primeira opção no combo. */
}

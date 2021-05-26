package br.com.project.listener;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/* prove as informações do ambiente de execução do Spring */

@ApplicationScoped // instancia única para todo o projeto
public class ContextLoaderListenerCadastroUtils extends ContextLoaderListener implements Serializable{

	
	private static final long serialVersionUID = 1L;

	/* método que retorna todo o contexto do spring */
	private static WebApplicationContext getWac() {
		return WebApplicationContextUtils
				.getWebApplicationContext(getCurrentWebApplicationContext()
										.getServletContext());
		
	}
	
	/* Esse método pode nos retornar qualquer instancia de um objeto utilizando o id */
	public static Object getBean(String idNomeBean) {
		return getWac().getBean(idNomeBean);
	}
	
	/* Esse método pode nos retornar qualquer instancia de um objeto utilizando a classe */

	public static Object getBean(Class<?> classe) {
		return getWac().getBean(classe);
	}
	
}

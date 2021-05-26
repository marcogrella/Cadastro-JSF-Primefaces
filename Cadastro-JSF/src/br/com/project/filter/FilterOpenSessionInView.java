package br.com.project.filter;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.filter.DelegatingFilterProxy;

import br.com.framework.hibernate.session.HibernateUtil;
import br.com.framework.utils.UtilFramework;
import br.com.project.listener.ContextLoaderListenerCadastroUtils;
import br.com.project.model.classes.Entidade;


@WebFilter(filterName="conexaoFilter")
public class FilterOpenSessionInView extends DelegatingFilterProxy implements Serializable{

	
	private static final long serialVersionUID = 1L;

	/* vari�vel que se comunica com o banco de dados */
	private static SessionFactory sf; 
	
	
	/* quando o servidor subir a aplica��o este m�todo � executado 
	 * OBS: executado somente uma vez. 
	 * */
	@Override
	protected void initFilterBean() throws ServletException {
		sf = HibernateUtil.getSessionFactory(); // inicia a sess�o
	}
	
	
	/* m�todo que � invocado para toda a requisi��o e toda a resposta */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws ServletException, IOException {

		/* JDBC Spring */
		BasicDataSource springBasicDataSource = (BasicDataSource) ContextLoaderListenerCadastroUtils.getBean("springDataSource"); 
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(springBasicDataSource);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		
		try {
			
			/* setar a codifica��o */
			request.setCharacterEncoding("UTF-8");
			
			/* Identifica qual usu�rio que est� fazendo a opera��o. Utiliza
			 * o envers */
			HttpServletRequest request2 = (HttpServletRequest) request; /* faz um cast do ServletRequest */
			HttpSession sessao = request2.getSession();
			
			/* Usuario que est� logado */
			Entidade userLogadoSessao = (Entidade) sessao.getAttribute("userLogadoSessao"); /* por navegador. */
			
			if(userLogadoSessao != null) {
				/* recupera o log do usu�rio e se tiver alguma altera��o identifica pelo c�digo */
				UtilFramework.getThreadLocal().set(userLogadoSessao.getEnt_codigo());
			}
			
			/* iniciar uma transa��o */
			sf.getCurrentSession().beginTransaction();
			
			/* antes de executar a a��o (Request). Ou seja a partir desta linha � 
			 * feita a execu��o das requisi��es JSF */	
			chain.doFilter(request, response); // executa a a��o no servidor
			/* ap�s executar a a��o (resposta) */
			
			/* ap�s executar a a��o anterior fazmos o commit */
			transactionManager.commit(status);
			
			if (sf.getCurrentSession().getTransaction().isActive()){
				sf.getCurrentSession().flush();
				sf.getCurrentSession().getTransaction().commit();
			}
			
			if (sf.getCurrentSession().isOpen()){
				sf.getCurrentSession().close();
			}
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset = UTF-8");
			
		} catch(Exception e) {
			
transactionManager.rollback(status);
			
			e.printStackTrace();
			
			if (sf.getCurrentSession().getTransaction().isActive()){
				sf.getCurrentSession().getTransaction().rollback();
			}
			
			if (sf.getCurrentSession().isOpen()){
				sf.getCurrentSession().close();
			}
			
		}finally {
			if (sf.getCurrentSession().isOpen()){
				if (sf.getCurrentSession().beginTransaction().isActive()){
					sf.getCurrentSession().flush();
					sf.getCurrentSession().clear();
				}
				
				if (sf.getCurrentSession().isOpen()){
					sf.getCurrentSession().close();
				}
			}
		}
	}
	

}

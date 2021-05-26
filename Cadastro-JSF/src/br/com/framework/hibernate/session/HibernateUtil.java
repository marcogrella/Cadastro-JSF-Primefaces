package br.com.framework.hibernate.session;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import javax.faces.bean.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.SessionFactoryImplementor;

import br.com.framework.implementacao.crud.VariavelConexaoUtil;


/**
 * Respons�vel por estabelecer a conex�o com o hibernate. Essas configura��es
 * est�o descritas na documenta��o do Tomcat
 * 
 */

@ApplicationScoped  // estamos dizendo que essa conex�o � para toda a aplica��o
public class HibernateUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	public static String JAVA_COMP_ENV_JDBC_DATA_SOURCE = "java:/comp/env/jdbc/datasource";

	/* objeto est�tico para prover a conex�o do hibernate */
	private static SessionFactory sessionFactory = buildSessionFactory();

	/* Respons�vel por ler o arquivo de configura��o hibernate.cfg.xml */
	private static SessionFactory buildSessionFactory() {

		try {

			/*
			 * verifica se j� existe um session factory, pois deve existir apenas uma
			 * conex�o
			 */
			if(sessionFactory == null) {
				sessionFactory = new Configuration().configure().buildSessionFactory();
			}

			return sessionFactory;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError("Erro ao criar a conex�o do Session Factory");
		}

	}

	
	/* m�todos auxiliares do hibernate util */

	/* Retorna o session factory corrente */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/* Retorna a sess�o do SessionFactory */
	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	/* Retorna uma nova sess�o no SessionFactory */
	public static Session openSession() {

		/* se n�o houver chamar� o m�todo e criar� um session */
		if (sessionFactory == null) {
			buildSessionFactory();
		}
		
		return sessionFactory.openSession();

	}

	/* Obtem a conex�o do provedor de conex�es configurado */
	public static Connection getConnectionProvider() throws SQLException {

		return ((SessionFactoryImplementor) sessionFactory)
				.getConnectionProvider().getConnection();

	}

	/* Connection no Initial Context java:/comp/env/jdbc/datasource */
	public static Connection getConnection() throws Exception {

		InitialContext context = new InitialContext();
		DataSource ds = (DataSource) (DataSource) context.lookup(JAVA_COMP_ENV_JDBC_DATA_SOURCE);
		
		return ds.getConnection();

	}

	
	/* DataDource do JNDI Tomcat */ 
	public DataSource getDataSourceJndi() throws NamingException {

		InitialContext context = new InitialContext();
		return (DataSource) context.lookup(VariavelConexaoUtil.JAVA_COMP_ENV_JDBC_DATA_SOURCE);

	}

}

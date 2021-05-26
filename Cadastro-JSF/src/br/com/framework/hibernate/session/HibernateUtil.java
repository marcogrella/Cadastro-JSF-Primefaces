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
 * Responsável por estabelecer a conexão com o hibernate. Essas configurações
 * estão descritas na documentação do Tomcat
 * 
 */

@ApplicationScoped  // estamos dizendo que essa conexão é para toda a aplicação
public class HibernateUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	public static String JAVA_COMP_ENV_JDBC_DATA_SOURCE = "java:/comp/env/jdbc/datasource";

	/* objeto estático para prover a conexão do hibernate */
	private static SessionFactory sessionFactory = buildSessionFactory();

	/* Responsável por ler o arquivo de configuração hibernate.cfg.xml */
	private static SessionFactory buildSessionFactory() {

		try {

			/*
			 * verifica se já existe um session factory, pois deve existir apenas uma
			 * conexão
			 */
			if(sessionFactory == null) {
				sessionFactory = new Configuration().configure().buildSessionFactory();
			}

			return sessionFactory;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError("Erro ao criar a conexão do Session Factory");
		}

	}

	
	/* métodos auxiliares do hibernate util */

	/* Retorna o session factory corrente */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/* Retorna a sessão do SessionFactory */
	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	/* Retorna uma nova sessão no SessionFactory */
	public static Session openSession() {

		/* se não houver chamará o método e criará um session */
		if (sessionFactory == null) {
			buildSessionFactory();
		}
		
		return sessionFactory.openSession();

	}

	/* Obtem a conexão do provedor de conexões configurado */
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

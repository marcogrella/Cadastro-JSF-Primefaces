package br.com.framework.implementacao.crud;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.framework.hibernate.session.HibernateUtil;
import br.com.framework.interfac.crud.InterfaceCrud;


@Component
@Transactional
public class ImplementacaoCrud<T> implements InterfaceCrud{

	private static final long serialVersionUID = 1L;

	/* objeto da classe HibernateUtil que fará a conexão única com o banco de dados. */
	private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	
	@Autowired
	private JdbcTemplateImpl jdbcTemplate;
	
	@Autowired
	private SimpleJdbcTemplateImpl simpleJdbcTemplate; 
	
	@Autowired
	private SimpleJdbcInsert simpleJdbcInsert; 
	
	@Autowired
	private SimpleJdbcClassImpl simpleJdbcClassImpl; 
	


	@Override
	public void save(Object obj) throws Exception {
		
	}

	@Override
	public void persist(Object obj) throws Exception {
		
	}

	@Override
	public void saveOrUpdate(Object obj) throws Exception {
		
	}

	@Override
	public void delete(Object obj) throws Exception {
		
	}

	@Override
	public Object merge(Object obj) throws Exception {
		return null;
	}

	@Override
	public List findList(Class objs) throws Exception {
		return null;
	}

	@Override
	public Object findById(Class entidade, Long id) throws Exception {
		return null;
	}

	@Override
	public Object findByPorId(Class entidade, Long id) throws Exception {
		return null;
	}

	@Override
	public List findListByQueryDinamica(String s) throws Exception {
		return null;
	}

	@Override
	public void executeUpdateQueryDinamica(String s) throws Exception {
		
	}

	@Override
	public void executeUpdateSQLDinamica(String s) throws Exception {
		
	}

	@Override
	public void clearSession() throws Exception {
		
	}

	@Override
	public void evict(Object objs) throws Exception {
		
	}

	@Override
	public Session getSession() throws Exception {
		return null;
	}

	@Override
	public List getListSQLDinamica(String sql) throws Exception {
		return null;
	}

	
	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	@Override
	public SimpleJdbcInsert getSimpleJdbcInsert() {
		return simpleJdbcInsert;
	}
	
	
	public SimpleJdbcClassImpl getSimpleJdbcClassImpl() {
		return simpleJdbcClassImpl;
	}

	

	@Override
	public Long totalRegistro(String table) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query obterQuery(String query) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List findListByQueryDinamica(String query, int inicianoRegistro, int maximoResultado) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* Validar SessionFactory */
	private void validaSessionFactory() {
		if(sessionFactory == null) {
			sessionFactory = HibernateUtil.getSessionFactory();
		}		
		validaTransaction();			
	}
	
	
	/* antes de iniciar os métodos de transação, fazemos a verificação se existe uma sessão e se não houver iniciar uma transaction no hibernate*/
	private void validaTransaction() {
		if(!sessionFactory.getCurrentSession().getTransaction().isActive());
			sessionFactory.getCurrentSession().beginTransaction();
	}
	
	
	/* Todas as solicitações irão passar pelo filtro que irá interceptar as requisições do JSF, no entanto
	 * haverá ocorrências que deverão passar esse filtro, por exemplo, requisições me jquery ou ajax e o método
	 * nos auxiliará com isso para que seja executado sem conhecimento do JSF */ 
	private void commitProcessoAjax(){
		sessionFactory.getCurrentSession().beginTransaction().commit();
	}
	
	private void rollBackProcessoAjax() {
		sessionFactory.getCurrentSession().beginTransaction().rollback();
	}
	
	
}

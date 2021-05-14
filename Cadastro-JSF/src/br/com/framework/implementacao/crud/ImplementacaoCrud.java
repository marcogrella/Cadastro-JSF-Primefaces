package br.com.framework.implementacao.crud;

import java.util.ArrayList;
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
		validaSessionFactory();
		sessionFactory.getCurrentSession().save(obj);
		executeFlushSession();
		
	}

	/* método igual ao anterior save (só para conhecimento) */
	@Override
	public void persist(Object obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().persist(obj);
		executeFlushSession();
	}

	/* identifica se um objeto já está na base de dados pela PK e se tiver atualiza. */
	@Override
	public void saveOrUpdate(Object obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().saveOrUpdate(obj);
		executeFlushSession();
	}

	@Override 
	public void update(Object obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().update(obj);
		executeFlushSession();
	}
	
	
	@Override
	public void delete(Object obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlushSession();
	}

	@Override
	public Object merge(Object obj) throws Exception {
		validaSessionFactory();
		/* salva o objeto e retorna para a mesma variável.  */
		obj = (T) sessionFactory.getCurrentSession().merge(obj);
		executeFlushSession();
		return obj;
	}

	@Override
	public List findList(Class entidade) throws Exception {
		validaSessionFactory();
		StringBuilder query = new StringBuilder();
		query.append(" select distinct (entity)").append(entidade.getSimpleName()).append(" entity");
		/* ficaria algo tipo: select distinct(entity) from cidades entity   (se fosse tabela cidades)*/ 
		
		List<T> lista = sessionFactory.getCurrentSession().createQuery(query.toString()).list();
		
		return lista;
	}

	@Override
	public Object findById(Class entidade, Long id) throws Exception {
		validaSessionFactory();
		Object obj = sessionFactory.getCurrentSession().load(getClass(), id);
		return obj;
	}

	@Override
	public Object findByPorId(Class entidade, Long id) throws Exception {
		validaSessionFactory();
		T obj = (T) sessionFactory.getCurrentSession().load(getClass(), id);
		return obj;
	}

	@Override
	public List findListByQueryDinamica(String s) throws Exception {
		validaSessionFactory();
		List<T> lista = new ArrayList<T>();
		lista = sessionFactory.getCurrentSession().createQuery(s).list(); /* recebe qualquer consulta */
		return lista;
	}

	@Override
	public void executeUpdateQueryDinamica(String s) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().createQuery(s).executeUpdate();  /* aqui nesse método é para HSQL */
		executeFlushSession();
	}

	@Override
	public void executeUpdateSQLDinamica(String s) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().createSQLQuery(s).executeUpdate(); /* aqui nesse método é para SQL puro*/
		executeFlushSession();

	}

	/* método para limpar a sessão e dar um alívio na memória */
	@Override
	public void clearSession() throws Exception {
		sessionFactory.getCurrentSession().clear();
	}

	/* O evict é caso no qual se conhece o objeto que está dando problema de memória */
	@Override
	public void evict(Object objs) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().evict(objs);
	}

	@Override
	public Session getSession() throws Exception {
		validaSessionFactory();
		return sessionFactory.getCurrentSession();
	}

	
	@Override
	public List<?> getListSQLDinamica(String sql) throws Exception {
		validaSessionFactory();
		List<?> lista = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		
		return lista;
	}

	/* serve para trazer mais de um registro em um SQL dinâmico */
	@Override
	public List<Object[]> getListSQLDinamicaArray(String sql) throws Exception {
		validaSessionFactory();
		List<Object[]> lista = (List<Object[]>) sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		return lista;
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
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(1) from ").append(table);
		return jdbcTemplate.queryForLong(sql.toString());
	}

	@Override
	public Query obterQuery(String query) throws Exception {
		validaSessionFactory();
		/* passa uma instrução para o hibernate e retorna a string de consulta. Depois utilizaremos para ouras 
		 * consultas / rotinas avançadas */
		Query queryReturn = sessionFactory.getCurrentSession().createQuery(query.toString());
		return queryReturn;
	}

	/* 
	 * Esse método realiza a consulta no banco de dados, iniciando o carregamento a partir do registro 
	 * passado no parametro -> iniciaNoRegistro e obtem o máximo de resultados passados em -> maximoResultado 
	 */
	@Override
	public List<T> findListByQueryDinamica(String query, int inicianoRegistro, int maximoResultado) throws Exception {
		validaSessionFactory();
		List<T> lista = new ArrayList<T>();
		lista = sessionFactory.getCurrentSession().createQuery(query).setFirstResult(inicianoRegistro).setMaxResults(maximoResultado).list();
		return lista;
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
	
	
	/* Roda instantaneamente o SQL no banco de dados. Auxilia no insert de várias instruçõe SQL no Banco de dados
	 * principalmente quando um objeto depende do outro. Se não fizer pode aconteecer de dar restrição na base de dados. 
	 * Obs: o executeFlushSession() não precisa ser executado em operações de consulta, pois não alteram em nada no banco.
 */
	private void executeFlushSession() {
		sessionFactory.getCurrentSession().flush();
	}

	
	
	
}

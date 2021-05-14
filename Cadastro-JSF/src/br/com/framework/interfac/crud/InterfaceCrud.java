package br.com.framework.interfac.crud;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/* os m�todos aqui ir�o recber qualquer objeto, ou seja, podemos receber qualquer objeto de qualquer classe */   

@Component /* componente do Spring */
@Transactional /* ir� realizar transa��es no banco */
public interface InterfaceCrud<T> extends Serializable{
	
	/* salvar os dados */
	void save(T obj) throws Exception;
	
	void persist(T obj) throws Exception;
	
	/* salvar ou atualizar */
	void saveOrUpdate(T obj) throws Exception;

	/* realiza a exclus�o de dados */
	void delete(T obj) throws Exception;
	
	/* salva ou atualiza e retorna o objeto em estado persistente */
	T merge(T obj) throws Exception;
	
	/* carrega a lista de dados de determinada classe */
	List<T> findList(Class<T> objs) throws Exception;
	
	/* busca e retorna um Object pelo ID de uma classe X */
	Object findById(Class<T> entidade, Long id) throws Exception;
	
	/* busca e retorna um objeto pelo ID de uma classe X */
	T findByPorId(Class<T> entidade, Long id) throws Exception;
	
	/* busca por consulta din�mica*/
	List<T> findListByQueryDinamica(String s) throws Exception;

	/* executar update com por consulta din�mica por HQL */
	void executeUpdateQueryDinamica(String s) throws Exception;
	
	/* executar update com por consulta din�mica por SQL puro */
	void executeUpdateSQLDinamica(String s) throws Exception;
	
	/* limpa a sess�o do hibernate e limpa o cache do banco de dados. */
	void clearSession() throws Exception;
	
	/* retira o objeto da sess�o do hibernate */
	void evict (Object objs) throws Exception;
	
	/* retorna a sess�o do hibernate */
	Session getSession() throws Exception;
	
	List<?> getListSQLDinamica(String sql) throws Exception;
	
	
	
	/* m�todos (3 classes) que implementam jdbc do spring e com boa performance */ 
	JdbcTemplate getJdbcTemplate();
	
	SimpleJdbcTemplate getSimpleJdbcTemplate();
	
	SimpleJdbcInsert getSimpleJdbcInsert();
	
	
	
	/* total de registros de uma tabela */
	Long totalRegistro(String table) throws Exception;
	
	/* Consulta din�mica para realizar consultas din�micas no banco de daos. */
	Query obterQuery(String query) throws Exception;
	
	/* carregamento por demanda - pagina��o */
	List<T> findListByQueryDinamica(String query, int inicianoRegistro, int maximoResultado) throws Exception; 
	
	
}

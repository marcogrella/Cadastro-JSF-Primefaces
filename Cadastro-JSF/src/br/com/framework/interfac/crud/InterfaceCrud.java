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


/* os métodos aqui irão recber qualquer objeto, ou seja, podemos receber qualquer objeto de qualquer classe */   

@Component /* componente do Spring */
@Transactional /* irá realizar transações no banco */
public interface InterfaceCrud<T> extends Serializable{
	
	/* salvar os dados */
	void save(T obj) throws Exception;
	
	void persist(T obj) throws Exception;
	
	/* salvar ou atualizar */
	void saveOrUpdate(T obj) throws Exception;

	/* realiza a exclusão de dados */
	void delete(T obj) throws Exception;
	
	/* salva ou atualiza e retorna o objeto em estado persistente */
	T merge(T obj) throws Exception;
	
	/* carrega a lista de dados de determinada classe */
	List<T> findList(Class<T> objs) throws Exception;
	
	/* busca e retorna um Object pelo ID de uma classe X */
	Object findById(Class<T> entidade, Long id) throws Exception;
	
	/* busca e retorna um objeto pelo ID de uma classe X */
	T findByPorId(Class<T> entidade, Long id) throws Exception;
	
	/* busca por consulta dinâmica*/
	List<T> findListByQueryDinamica(String s) throws Exception;

	/* executar update com por consulta dinâmica por HQL */
	void executeUpdateQueryDinamica(String s) throws Exception;
	
	/* executar update com por consulta dinâmica por SQL puro */
	void executeUpdateSQLDinamica(String s) throws Exception;
	
	/* limpa a sessão do hibernate e limpa o cache do banco de dados. */
	void clearSession() throws Exception;
	
	/* retira o objeto da sessão do hibernate */
	void evict (Object objs) throws Exception;
	
	/* retorna a sessão do hibernate */
	Session getSession() throws Exception;
	
	List<?> getListSQLDinamica(String sql) throws Exception;
	
	
	
	/* métodos (3 classes) que implementam jdbc do spring e com boa performance */ 
	JdbcTemplate getJdbcTemplate();
	
	SimpleJdbcTemplate getSimpleJdbcTemplate();
	
	SimpleJdbcInsert getSimpleJdbcInsert();
	
	
	
	/* total de registros de uma tabela */
	Long totalRegistro(String table) throws Exception;
	
	/* Consulta dinâmica para realizar consultas dinâmicas no banco de daos. */
	Query obterQuery(String query) throws Exception;
	
	/* carregamento por demanda - paginação */
	List<T> findListByQueryDinamica(String query, int inicianoRegistro, int maximoResultado) throws Exception; 
	
	
}

package br.com.framework.implementacao.crud;

import java.io.Serializable;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/* propagation.Required significa que se não houver sessão o Spring irá criar uma 
 * rollback caso der erro na transação do banco.
 * 
 * */

@Component
@Transactional(propagation=Propagation.REQUIRED, rollbackFor = Exception.class)
public class JdbcTemplateImpl extends JdbcTemplate implements Serializable {

	private static final long serialVersionUID = 1L;

	/* construtor dessa classe e que instancia um objeto dataSource que será passado na configuração do spring */
	JdbcTemplateImpl(DataSource datasource){
		super(datasource);
	}
	
	
}

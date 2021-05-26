package br.com.project.listener;

import java.io.Serializable;

import org.hibernate.envers.RevisionListener;
import org.springframework.stereotype.Service;

import br.com.framework.utils.UtilFramework;
import br.com.project.model.classes.Entidade;
import br.com.project.model.classes.InformacaoRevisao;

/* Essa classe possui o intuito de gerenciar o listener que ir� ouvir
 * as transa��es no banco. Ela ir� capturar o usu�rio e passar para a 
 * classe Revis�o; */


@Service
public class CustomListener implements RevisionListener, Serializable {


	private static final long serialVersionUID = 1L;

	
	@Override
	public void newRevision(Object revisionEntity) {
		
		/* objeto da classe de log */
		InformacaoRevisao informacaoRevisao = (InformacaoRevisao) revisionEntity;
		
		/* objeto do listener que pega o cod do Usuario */
		Long codUser = UtilFramework.getThreadLocal().get(); // pega o c�digo do usuario
		
		Entidade entidade = new Entidade();
		
		/* se for diferente de null e diferente de Long */
		
		if(codUser != null && codUser != 0L) {
			entidade.setEnt_codigo(codUser);
			informacaoRevisao.setEntidade(entidade); 
		}
	
	}

	
	
}

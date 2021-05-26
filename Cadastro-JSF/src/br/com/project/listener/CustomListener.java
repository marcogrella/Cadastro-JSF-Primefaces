package br.com.project.listener;

import java.io.Serializable;

import org.hibernate.envers.RevisionListener;
import org.springframework.stereotype.Service;

import br.com.framework.utils.UtilFramework;
import br.com.project.model.classes.Entidade;
import br.com.project.model.classes.InformacaoRevisao;

/* Essa classe possui o intuito de gerenciar o listener que irá ouvir
 * as transações no banco. Ela irá capturar o usuário e passar para a 
 * classe Revisão; */


@Service
public class CustomListener implements RevisionListener, Serializable {


	private static final long serialVersionUID = 1L;

	
	@Override
	public void newRevision(Object revisionEntity) {
		
		/* objeto da classe de log */
		InformacaoRevisao informacaoRevisao = (InformacaoRevisao) revisionEntity;
		
		/* objeto do listener que pega o cod do Usuario */
		Long codUser = UtilFramework.getThreadLocal().get(); // pega o código do usuario
		
		Entidade entidade = new Entidade();
		
		/* se for diferente de null e diferente de Long */
		
		if(codUser != null && codUser != 0L) {
			entidade.setEnt_codigo(codUser);
			informacaoRevisao.setEntidade(entidade); 
		}
	
	}

	
	
}

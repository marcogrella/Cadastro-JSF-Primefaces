package br.com.project.exception;

import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import org.hibernate.SessionFactory;
import br.com.framework.hibernate.session.HibernateUtil;


public class CustomExceptionHandler extends ExceptionHandlerWrapper {

	
	
	private ExceptionHandler wrapperd;
	
	final FacesContext facesContext = FacesContext.getCurrentInstance();
	
	/* par�metros da requisi��o */
	final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
	
	/* estado atual da navega��o */
	final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();
	
	/* construtor */
	public CustomExceptionHandler(ExceptionHandler exceptionHandler) {
		this.wrapperd = exceptionHandler;
	}
	
	
	/* sobrescreve o m�todo ExceptionHandler que retorna a pilha de exce��es */
	@Override
	public ExceptionHandler getWrapped() {
		
		return wrapperd;
	}
	
	/* Sobrescreve o m�todo handle que � respons�vel por manipular as exce��es do JSF */
	@Override
	public void handle() throws FacesException {
		
		/* lista de eventos de erro */
		final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();
		
		/* varrer esta lista */
		
		while(iterator.hasNext()) {
			ExceptionQueuedEvent event = iterator.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
			
			/* recuperar a exce��o do context*/
			Throwable exception = context.getException();
			
			/* Aqui trabalhamos a exce��o */
			
			
			try {
				
				/* OBS: alguns erros s�o previs�veis, mas outros podem ocorrer de forma inesperada */
				
				requestMap.put("exceptionMessage", exception.getMessage());
				
				if(exception != null && exception.getMessage() != null
						&& exception.getMessage().indexOf("ConstraintViolationException") != -1) {
					 
					FacesContext.getCurrentInstance().
					 addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_WARN, "Registro"
					 		+ "n�o pode ser removido por possuir associa��o.", ""));
				
				/* tipo de erro pode acontecer quando 2 usu�rios do sistem manipulam o mesmo registro. */
				} else if (exception != null && exception.getMessage() != null
						&& exception.getMessage().indexOf("org.hibernate.StaleObjectStateException") != -1) {
					
					FacesContext.getCurrentInstance().
					 addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registro"
					 		+ "foi atualizado ou exclu�do por outro usu�rio. Consulte novamente", ""));
					
				}  else {
					
					/* caso n�o ocorra um erro que foi identificado acima */
					FacesContext.getCurrentInstance().
					 addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, "O sistema se recuperou "
					 		+ "de um erro inesperado. ", ""));
					
					/* avisa o cliente e relata que o sistema pode ser utilizado normalmente */
					FacesContext.getCurrentInstance().
					 addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Continue "
					 		+ "utilizando o sistema normalmente. ", ""));
					
					/* informa o erro causado */
					FacesContext.getCurrentInstance().
					 addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, "O erro foi causado "
					 		+ "por: \n" + exception.getMessage(), ""));
					
					/* 
					 * PrimeFaces 
					 * O alert � exibido se apenas a p�gina n�o for redirecionada
					 * */
				
					org.primefaces.context.RequestContext
						.getCurrentInstance()
						.execute("alert('O sistema se recuperou de um erro inesperado')");
					
					
					/* mostrar em dialog */
					org.primefaces.context.RequestContext
						.getCurrentInstance()
						.showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "O sistemma se recuperou de um erro inesperado."));
					
					
					/* Caso queiramos redirecionar para uma p�gina de erro em espec�fico criada para isso 
					 * o expired=true � para expirar o contexto.
					 */
					navigationHandler.handleNavigation(facesContext, null, "/error/error.jsf?faces-redirect=true&expired=true");
					
				} 
				
				
					/* renderiza a p�gina de erro e exibe as mensagens */ 
				facesContext.renderResponse();
				
			} finally {
				SessionFactory sf = HibernateUtil.getSessionFactory();
				if(sf.getCurrentSession().getTransaction().isActive()) {
					sf.getCurrentSession().getTransaction().rollback();
				}
				
				/* imprime o erro no console*/
				exception.printStackTrace();
				
				/* removemos o objeto de exce��o, depois que n�o � mais �til removemos. */
				iterator.remove();
				
			}
			
			/* ap�s os erros serem apresentados iremos finalizar a manipula��o */
			getWrapped().handle();
			
		}
		
	}
	

}

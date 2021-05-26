package br.com.project.bean.geral;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.context.request.FacesRequestAttributes;

public class ViewScope implements Scope, Serializable{

	private static final long serialVersionUID = 1L;

	/* verificar na documentação do springboot o viewScope.callBacks */
	public static final String VIEW_SCOPE_CALLBACKS = "viewScope.callBacks";
	
	
	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		Object instance = getViewMap().get(name);
		if(instance == null) {
			instance = objectFactory.getObject();
			getViewMap().put(name, instance);
			
		}
		
		return instance;
	}

	@Override
	public String getConversationId() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesRequestAttributes facesRequestAtributes = new FacesRequestAttributes(facesContext);
		return facesRequestAtributes.getSessionId() + "-" + facesContext.getViewRoot().getViewId();

	}

	/* quando saímos de uma tela o escopo de view morre e com esse métood será encerrado.*/
	@Override
	public void registerDestructionCallback(String name, Runnable runnable) {
		Map<String, Runnable> callbacks = (Map<String, Runnable>) getViewMap().get(VIEW_SCOPE_CALLBACKS);
		if(callbacks !=null ){
			callbacks.put(VIEW_SCOPE_CALLBACKS, runnable);
		}
	}
	
	
	

	@Override
	public Object remove(String name) {
		Object instance = getViewMap().remove(name);
		if(instance != null ) {
			Map<String, Runnable> callBacks = (Map<String, Runnable>) getViewMap().get(VIEW_SCOPE_CALLBACKS);
			if (callBacks!=null) {
				callBacks.remove(name);
			}
		}{
			
		}
		return instance;
	}

	/* resolve as referencias dos objetos de escopo */ 
	
	@Override
	public Object resolveContextualObject(String name) {
		 FacesContext facesContext = FacesContext.getCurrentInstance();
		 FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);
		return facesRequestAttributes.resolveReference(name);
	}

	/* 
	 * pegamos os mapas de parâmetros do jsf 
	 * getViewRoot() é o mapa de parâmetros 
	 * retorna o componente raiz associado a esta solicitação (request).
	 * @getViewMap retora um Map que atua como uma interface para armazenamento de 
	 * dados
	 * 
	 * */
	private Map<String, Object> getViewMap(){
		return FacesContext.getCurrentInstance() != null? 
				FacesContext.getCurrentInstance().getViewRoot().getViewMap() :
					new HashMap<String, Object>();
		
		
	}
	
	
}

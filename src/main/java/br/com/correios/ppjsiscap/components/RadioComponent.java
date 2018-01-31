package br.com.correios.ppjsiscap.components;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@FacesComponent("br.com.correios.ppjsistema.components.Radio")
@ResourceDependencies({@ResourceDependency(library="javax.faces", name="jsf.js")})
public class RadioComponent extends UIInput implements NamingContainer, ClientBehaviorHolder{
	
	private static final String EVENTO_CLICK = "click";

	private static List<String> eventos = Arrays.asList(EVENTO_CLICK);
	
	private String name;
	
	private String para;
	
	private Object valorSelecionado;
	
	private Object idx;
	
	@Override
	public String getFamily() {
		return "javax.faces.NamingContainer";
	}
	
	@Override
	public Object getSubmittedValue() {
		return this;
	}
	
	@Override
	public void encodeBegin(FacesContext ctx) throws IOException {
		String formId = getClientId().substring(0, getClientId().indexOf(':')+1);
		RadioGroupComponent rdgc = (RadioGroupComponent) ctx.getViewRoot().findComponent(formId +getPara());
		name = rdgc.getClientId();
		if(rdgc.getSelectItems().size() > 0){
			Object object = rdgc.getSelectItems().get(Integer.valueOf(idx.toString()));
			if(object.getClass().isAssignableFrom(SelectItem.class)){
				SelectItem selectItem = (SelectItem) object;
				setValue(selectItem.getValue());				
			}else{
				setValue(object);
			}
		}
		valorSelecionado = rdgc.getValue();				
		
		super.encodeBegin(ctx);
	}
	
	//TODO - verificar possibilidade de utilizar js no próprio xhtml
	@SuppressWarnings("unused")
	private String getScript(FacesContext ctx, RadioComponent radioComponent) {
		String script = null;
		ClientBehaviorContext behaviorContext = ClientBehaviorContext.createClientBehaviorContext(
				ctx, radioComponent, EVENTO_CLICK, radioComponent.getClientId(ctx), null);
		Map<String, List<ClientBehavior>> behaviors = radioComponent.getClientBehaviors();
		if(behaviors.containsKey(EVENTO_CLICK)){			
			script = behaviors.get(EVENTO_CLICK).get(0).getScript(behaviorContext);
		}
		return script;
	}

	@Override
	public String getDefaultEventName() {		
		return EVENTO_CLICK;
	}
	
	@Override
	public Collection<String> getEventNames() {		
		return eventos;
	}
	
	

	/**
	 * @return the para
	 */
	public String getPara() {
		return para;
	}

	/**
	 * @param para the para to set
	 */
	public void setPara(String para) {
		this.para = para;
	}

	/**
	 * @return the idx
	 */
	public Object getIdx() {
		return idx;
	}

	/**
	 * @param idx the idx to set
	 */
	public void setIdx(Object itemIndex) {
		this.idx = itemIndex;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the valorSelecionado
	 */
	public Object getValorSelecionado() {
		return valorSelecionado;
	}

	/**
	 * @param valorSelecionado the valorSelecionado to set
	 */
	public void setValorSelecionado(Object valorSelecionado) {
		this.valorSelecionado = valorSelecionado;
	}

	
	
	

}

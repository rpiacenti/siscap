package br.com.correios.ppjsiscap.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

@FacesComponent("br.com.correios.ppjsistema.components.RadioGroup")
public class RadioGroupComponent extends UIInput implements NamingContainer,ClientBehaviorHolder{
	
	@SuppressWarnings("rawtypes")
	private List selectItems = new ArrayList();
	
	@Override
	public String getFamily() {
		return "javax.faces.NamingContainer";
	}
	
	@Override
	public Object getSubmittedValue() {
		return this;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void encodeBegin(FacesContext arg0) throws IOException {
		List<UIComponent> components = getChildren();
		for(UIComponent c : components){
			if(c instanceof UISelectItems){	
				if(((UISelectItems)c).getValue() instanceof  List){
					setSelectItems(((List) ((UISelectItems)c).getValue()));					
				}
				if(((UISelectItems)c).getValue() instanceof  Object []){ 
					setSelectItems(Arrays.asList((Object [])((UISelectItems)c).getValue()));
				}
			}
			if(c instanceof UISelectItem){
				Object object = c.getAttributes().get("value");
				getSelectItems().add(new SelectItem(object));
			}
			
		}
		super.encodeBegin(arg0);
	}
	
	@Override
	protected Object getConvertedValue(FacesContext ctx, Object novoObjetoSubmetido)
			throws ConverterException {
		HttpServletRequest req = (HttpServletRequest) ctx.getExternalContext().getRequest();
		Object resultado = req.getParameter(getClientId());
		setValue(resultado);
		return resultado;
	}
	
	

	/**
	 * @return the selectItems
	 */
	@SuppressWarnings("rawtypes")
	public List getSelectItems() {
		return selectItems;
	}

	/**
	 * @param selectItems the selectItems to set
	 */
	@SuppressWarnings("rawtypes")
	public void setSelectItems(List selectItems) {
		this.selectItems = selectItems;
	}

	

}

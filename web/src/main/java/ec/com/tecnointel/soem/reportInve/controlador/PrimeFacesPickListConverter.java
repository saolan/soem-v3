package ec.com.tecnointel.soem.reportInve.controlador;

import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "primeFacesPickListConverter")
public class PrimeFacesPickListConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
	    Object ret = null;
//	    if (arg1 instanceof PickList) {
//	        Object dualList = ((PickList) arg1).getValue();
//	        DualListModel dl = (DualListModel) dualList;
//	        for (Object o : dl.getSource()) {
//	            String id = "" + ((PersProv) o).getPersonaId();
//	            if (arg2.equals(id)) {
//	                ret = o;
//	                break;
//	            }
//	        }
//	        if (ret == null)
//	            for (Object o : dl.getTarget()) {
//	                String id = "" + ((PersProv) o).getPersonaId();
//	                if (arg2.equals(id)) {
//	                    ret = o;
//	                    break;
//	                }
//	            }
//	    }
	    return ret;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
	    String str = "";
	    if (arg2 instanceof PersProv) {
	        str = "" + ((PersProv) arg2).getPersonaId();
	    }
	    return str;
	}
}

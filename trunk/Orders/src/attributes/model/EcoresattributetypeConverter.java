package attributes.model;

import attributes.core.EcoresattributetypeFacade;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.util.logging.Logger;


@ManagedBean
@RequestScoped
public class EcoresattributetypeConverter implements Converter {

    private static Logger _log = Logger.getLogger(EcoresattributetypeConverter.class.getName());

    @EJB
    public EcoresattributetypeFacade attributetypeFacade;

    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.isEmpty()) {
            return null;
        }

        try {
            _log.info("Новое значение: " + attributetypeFacade.find(Long.valueOf(submittedValue)).getTypeName());
            return attributetypeFacade.find(Long.valueOf(submittedValue));
        } catch (NumberFormatException exception) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player ID"));
        }
    }

    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        if (modelValue == null) {
            return "";
        }

        if (modelValue instanceof Ecoresattributetype) {
            return String.valueOf(((Ecoresattributetype) modelValue).getRecid());
        } else {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid  instance"));
        }
    }
}

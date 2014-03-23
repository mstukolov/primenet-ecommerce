package attributes.model;

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
public class DatatypeConverter implements Converter {
    private static Logger _log = Logger.getLogger(DatatypeConverter.class.getName());

    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.isEmpty()) {
            //_log.info("Новое значение типа данных пустое: " + submittedValue);
            return null;
        }

        try {
            //_log.info("Новое значение типа данных: " + submittedValue);
            return AttributeDataTypes2.getAttributeDataTypes2Label(submittedValue);
        } catch (NumberFormatException exception) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player ID"));
        }
    }

    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        if (modelValue == null) {
            return "";
        }
        //_log.info("Началась конвертация объекта AttributeDataTypes2: " + modelValue.getClass());

        if (modelValue instanceof AttributeDataTypes2) {

            return ((AttributeDataTypes2) modelValue).name();
        } else {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid  instance"));
        }
    }
}

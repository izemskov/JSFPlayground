package ru.develgame.jsfgame.converters;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

@FacesConverter("converter.enum")
public class EnumValueConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
                              String value) {
        //assume that "value" property holds the value (it is true for t:selectOneRadio)
        ValueExpression ve = component.getValueExpression("value");
        if (ve != null) {
            Class<?> type = ve.getType(context.getELContext());
            if (type.isEnum()) {
                return new EnumConverter(type).getAsObject(context, component, value);
            } else {
                throw new IllegalArgumentException("value type isn't enum");
            }
        } else {
            throw new UnsupportedOperationException("value property doesn't exist");
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
                              Object value) {
        if (value != null) {
            if (value.getClass().isEnum()) {
                return ((Enum<?>) value).name();
            } else {
                return value.toString();
            }
        }
        return null;
    }

}

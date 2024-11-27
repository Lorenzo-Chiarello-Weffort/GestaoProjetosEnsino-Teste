package converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@FacesConverter("dateConverter")
public class DateConverter implements Converter {

    private static final String DATE_PATTERN = "yyyy-MM-dd"; // Formato usado no HTML5 (type="date")

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        try {
            return format.parse(value); // Converte String para Date
        } catch (ParseException e) {
            throw new IllegalArgumentException("Erro ao converter a data: " + value, e);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        return format.format((Date) value); // Converte Date para String
    }
}

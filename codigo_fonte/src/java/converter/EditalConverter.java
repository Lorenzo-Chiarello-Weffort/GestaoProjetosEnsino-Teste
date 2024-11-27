package converter;

import dao.EditalDAO;
import modelo.Edital;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@FacesConverter(value = "editalConverter")
public class EditalConverter implements Converter, Serializable {

    private EditalDAO editalDAO;

    public EditalConverter() {
        editalDAO = new EditalDAO(); // Idealmente, usar um DAO injetado
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return editalDAO.find(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID inválido: " + value, e);
        } catch (Exception ex) {
            Logger.getLogger(EditalConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof Edital) {
            return String.valueOf(((Edital) value).getIdedital());
        } else if (value instanceof String) {
            return (String) value; // Se já for uma String, simplesmente retorna o valor.
        } else {
            throw new IllegalArgumentException("Tipo inválido: " + value.getClass().getName());
        }
    }

}

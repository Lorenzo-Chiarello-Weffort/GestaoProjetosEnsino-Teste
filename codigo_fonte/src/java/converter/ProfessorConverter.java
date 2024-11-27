package converter;

import dao.ProfessorDAO;
import modelo.Professor;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@FacesConverter(value = "professorConverter")
public class ProfessorConverter implements Converter, Serializable {

    private ProfessorDAO professorDAO;

    public ProfessorConverter() {
        professorDAO = new ProfessorDAO(); // Idealmente, usar um DAO injetado
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return professorDAO.find(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID inválido: " + value, e);
        } catch (Exception ex) {
            Logger.getLogger(ProfessorConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof Professor) {
            return String.valueOf(((Professor) value).getProntuario());
        } else if (value instanceof String) {
            return (String) value; // Se já for uma String, simplesmente retorna o valor.
        } else {
            throw new IllegalArgumentException("Tipo inválido: " + value.getClass().getName());
        }
    }

}

package converter;

import dao.CursoDAO;
import modelo.Curso;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@FacesConverter(value = "cursoConverter")
public class CursoConverter implements Converter, Serializable {

    private CursoDAO cursoDAO;

    public CursoConverter() {
        cursoDAO = new CursoDAO(); // Idealmente, usar um DAO injetado
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Integer id = Integer.valueOf(value);
            return cursoDAO.find(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID inválido: " + value, e);
        } catch (Exception ex) {
            Logger.getLogger(CursoConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof Curso) {
            return String.valueOf(((Curso) value).getIdCurso());
        } else if (value instanceof String) {
            return (String) value; // Se já for uma String, simplesmente retorna o valor.
        } else {
            throw new IllegalArgumentException("Tipo inválido: " + value.getClass().getName());
        }
    }

}

package converter;

import dao.ProjetoDAO;
import modelo.Projeto;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@FacesConverter(value = "projetoConverter")
public class ProjetoConverter implements Converter, Serializable {

    private ProjetoDAO projetoDAO;

    public ProjetoConverter() {
        projetoDAO = new ProjetoDAO();
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Integer id = Integer.valueOf(value);
            return projetoDAO.find(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID inválido: " + value, e);
        } catch (Exception ex) {
            Logger.getLogger(ProjetoConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof Projeto) {
            return String.valueOf(((Projeto) value).getIdProjeto());
        } else if (value instanceof String) {
            return (String) value; // Se já for uma String, simplesmente retorna o valor.
        } else {
            throw new IllegalArgumentException("Tipo inválido: " + value.getClass().getName());
        }
    }

}

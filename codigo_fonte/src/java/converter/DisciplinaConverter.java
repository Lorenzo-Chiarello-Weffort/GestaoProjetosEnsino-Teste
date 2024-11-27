package converter;

import dao.DisciplinaDAO;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import modelo.Disciplina;
import java.sql.SQLException;

@FacesConverter("disciplinaConverter")
public class DisciplinaConverter implements Converter {

    private DisciplinaDAO disciplinaDAO;

    public DisciplinaConverter() {
        // Inicia o DAO para obter os dados da disciplina
        this.disciplinaDAO = new DisciplinaDAO();
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            // Tenta obter a Disciplina com base no ID fornecido
            Integer idDisciplina = Integer.valueOf(value);
            return disciplinaDAO.find(idDisciplina);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof Disciplina) {
            Disciplina disciplina = (Disciplina) object;
            return String.valueOf(disciplina.getIddisciplina());
        } else {
            return null;
        }
    }
}

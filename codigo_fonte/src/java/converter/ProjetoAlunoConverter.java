package converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import modelo.Projeto;
import modelo.Projetoaluno;

@FacesConverter(value = "projetoAlunoConverter")
public class ProjetoAlunoConverter implements Converter {

    // Método para converter o Projetoaluno para String (idProjeto)
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        Projetoaluno projetoaluno = new Projetoaluno();
        Projeto projeto = new Projeto();
        
        // Aqui você busca o projeto pelo ID. A busca pode ser feita via EntityManager, DAO ou serviço
        int idProjeto = Integer.parseInt(value);  // Convertendo a String para int
        projeto.setIdProjeto(idProjeto);
        
        // Supondo que você tenha um método para recuperar o título e outras informações
        // Isso seria uma busca no banco de dados ou serviço
        // projeto.setTitulo("Título do Projeto"); 
        
        projetoaluno.setIdProjeto(projeto);  // Associa o Projeto ao Projetoaluno
        return projetoaluno;
    }

    // Método para converter o Projetoaluno para String (idProjeto)
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null || !(value instanceof Projetoaluno)) {
            return "";
        }
        
        Projetoaluno projetoaluno = (Projetoaluno) value;
        // Retorna o idProjeto como String
        return String.valueOf(projetoaluno.getIdProjeto().getIdProjeto());
    }
}

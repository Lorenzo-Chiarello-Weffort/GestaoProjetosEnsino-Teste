package controller;

import dao.CursoDAO;
import dao.DisciplinaDAO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Curso;
import modelo.Disciplina;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.model.file.UploadedFile;

@Named
@ViewScoped
public class DisciplinaController implements Serializable {

    private List<Curso> cursos;
    private Curso cursoSelecionado;
    private CursoDAO cursoDAO = new CursoDAO();
    private DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private String novaDisciplina;
     private Disciplina Disciplina = new Disciplina();
    private List<String> sugestoes = new ArrayList<>();
    private String disciplinaSelecionada;
    List<String> disciplinas = new ArrayList<>();
    private List<Disciplina> disciplinasList  = new ArrayList<>();
    private UploadedFile arquivoXML;

    @PostConstruct
    public void init() {
        try {
            cursos = cursoDAO.findAll();
            preencherTabela();
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao preencher cursos e disciplinas: " ,""));
        }
    }

    private void preencherTabela() {
        disciplinasList = disciplinaDAO.findAll();
    }

     public void sugerirDisciplinas() {
         List<Disciplina> todasDisciplinas = disciplinaDAO.findAll();
         sugestoes.clear();
         if (novaDisciplina != null && !novaDisciplina.isEmpty()) {
             todasDisciplinas.stream()
                     .filter(disciplina -> disciplina.getNome().contains(novaDisciplina))
                     .forEach(disciplina -> sugestoes.add(disciplina.getNome()));
         }
    }
     
    public void adicionarDisciplina() throws SQLException {
        if (cursoSelecionado != null && (novaDisciplina != null && !novaDisciplina.trim().isEmpty())) {
             if (verificarNome(novaDisciplina)) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Disciplina já existente", ""));
                return;
            }
            Disciplina.setNome(novaDisciplina);
            Disciplina.setIdCurso(cursoSelecionado);
            disciplinaDAO.create(Disciplina);
            disciplinas.add(novaDisciplina.trim());
             FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Disciplina Cadastrada!", ""));
             
            Disciplina = new Disciplina();
            novaDisciplina = "";
            sugestoes.clear();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao cadastrar disciplina.", ""));
        }
    }

    public void carregarDisciplinasDeXML() {
        if (arquivoXML != null) {
            try (InputStream inputStream = arquivoXML.getInputStream()) {
                // Usa Apache POI para ler o arquivo XLS
                Workbook workbook = WorkbookFactory.create(inputStream);
                Sheet sheet = workbook.getSheetAt(0); // Primeira aba da planilha

                for (Row row : sheet) {
                    // Ignora a primeira linha (cabeçalho)
                    if (row.getRowNum() == 0) continue;

                    Cell cellDisciplina = row.getCell(3); // Coluna 4 (baseado no exemplo)
                    if (cellDisciplina != null) {
                        String disciplinaNome = cellDisciplina.getStringCellValue().trim();
                        if (!disciplinas.contains(disciplinaNome)) {
                            disciplinas.add(disciplinaNome); // Adiciona à lista de disciplinas

                            // Cria a entidade Disciplina e define os valores
                            Disciplina novaDisciplina = new Disciplina();
                            novaDisciplina.setNome(disciplinaNome);
                            novaDisciplina.setIdCurso(cursoSelecionado);

                            // Cadastra a disciplina no banco de dados
                            try {
                                disciplinaDAO.create(novaDisciplina);
                            } catch (Exception e) {
                                System.err.println("Erro ao cadastrar disciplina no banco: " + e.getMessage());
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao cadastrar disciplina no banco: " + e.getMessage(), null));
                            }
                        }
                    }
                }
                workbook.close();

                // Limpa o arquivo após a leitura
                arquivoXML = null;

                // Adiciona uma mensagem de sucesso
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Disciplinas carregadas e cadastradas com sucesso!", null));
            } catch (Exception e) {
                e.printStackTrace();
                // Adiciona uma mensagem de erro
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar disciplinas do XLS: " + e.getMessage(), null));
            }
        } else {
            // Adiciona uma mensagem de erro se o arquivo não foi selecionado
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Por favor, selecione um arquivo XLS.", null));
        }
    }

    public boolean verificarNome(String nome) {
        Disciplina discEncontrado = disciplinaDAO.findByNome(nome);
        if (discEncontrado != null) {
            return true;
        }
        return false;
    }
    
    public UploadedFile getArquivoXML() {
        return arquivoXML;
    }

    public void setArquivoXML(UploadedFile arquivoXML) {
        this.arquivoXML = arquivoXML;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

    public Curso getCursoSelecionado() {
        return cursoSelecionado;
    }

    public void setCursoSelecionado(Curso cursoSelecionado) {
        this.cursoSelecionado = cursoSelecionado;
    }

    public String getNovaDisciplina() {
        return novaDisciplina;
    }

    public void setNovaDisciplina(String novaDisciplina) {
        this.novaDisciplina = novaDisciplina;
    }

    public List<String> getSugestoes() {
        return sugestoes;
    }

    public void setSugestoes(List<String> sugestoes) {
        this.sugestoes = sugestoes;
    }

    public String getDisciplinaSelecionada() {
        return disciplinaSelecionada;
    }

    public void setDisciplinaSelecionada(String disciplinaSelecionada) {
        this.disciplinaSelecionada = disciplinaSelecionada;
    }

    public List<String> getDisciplinas() {
        return disciplinas;
    }

    public CursoDAO getCursoDAO() {
        return cursoDAO;
    }

    public void setCursoDAO(CursoDAO cursoDAO) {
        this.cursoDAO = cursoDAO;
    }

    public DisciplinaDAO getDisciplinaDAO() {
        return disciplinaDAO;
    }

    public void setDisciplinaDAO(DisciplinaDAO disciplinaDAO) {
        this.disciplinaDAO = disciplinaDAO;
    }

    public List<Disciplina> getDisciplinasList() {
        return disciplinasList;
    }

    public void setDisciplinasList(List<Disciplina> disciplinasList) {
        this.disciplinasList = disciplinasList;
    }

    public Disciplina getDisciplina() {
        return Disciplina;
    }

    public void setDisciplina(Disciplina Disciplina) {
        this.Disciplina = Disciplina;
    }
}

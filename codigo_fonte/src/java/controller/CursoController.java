package controller;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import modelo.Curso;
import modelo.Disciplina;
import dao.CursoDAO;
import jakarta.enterprise.context.SessionScoped;
import java.io.IOException;
import java.sql.SQLException;

@Named
@SessionScoped
public class CursoController implements Serializable {

    private Curso cursoParaCadastro;
    private Curso cursoSelecionado;
    private CursoDAO cursoDAO = new CursoDAO();
    private List<Curso> listaCurso = new ArrayList<>();
    private boolean editar = false; // Variável para controlar modo de edição

    @PostConstruct
    public void preencherTabela() {
        try {
            if (cursoDAO != null) {
                listaCurso = cursoDAO.findAll();
                if (listaCurso == null) {
                    listaCurso = new ArrayList<>(); // Evita `null` na lista
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: DAO não inicializado.", ""));
            }
        } catch (SQLException e) {
            listaCurso = new ArrayList<>();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao preencher tabela: " + e.getMessage(), ""));
        }
    }

    public Curso getCursoParaCadastro() {
        if (cursoParaCadastro == null) {
            cursoParaCadastro = new Curso();
        }
        return cursoParaCadastro;
    }

    public void setCursoParaCadastro(Curso cursoParaCadastro) {
        this.cursoParaCadastro = cursoParaCadastro;
    }

    public Curso getCursoSelecionado() {
        return cursoSelecionado;
    }

    public void setCursoSelecionado(Curso cursoSelecionado) {
        this.cursoSelecionado = cursoSelecionado;
    }

    public List<Curso> getListaCurso() {
        return listaCurso;
    }

    public boolean isEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public void adicionarDisciplina() {
        if (!editar) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não é possível adicionar disciplinas durante a criação de um curso. Edite o curso para adicionar disciplinas.", ""));
            return;
        }
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Disciplina adicionada", ""));
    }

 

    public void salvarCurso() {
        try {
            if (cursoParaCadastro == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum Curso para salvar", ""));
                return;
            }

            if (cursoParaCadastro.getNomeCurso() == null || cursoParaCadastro.getNomeCurso().trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "O nome do curso é obrigatório", ""));
                return;
            }

            // Validar se o nome do curso já existe, somente se for criação
            if (!editar && verificarNome(cursoParaCadastro.getNomeCurso())) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Curso já existente", ""));
                return;
            }

            if (editar) {
                cursoDAO.edit(cursoParaCadastro);
                editar = false;
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Curso atualizado com sucesso", ""));
            } else {
                cursoDAO.create(cursoParaCadastro);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Curso criado com sucesso", ""));
            }

            preencherTabela();
            cursoParaCadastro = new Curso(); // Limpa o formulário
           
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar Curso: " + e.getMessage(), ""));
        }
    }

    /**
     * Prepara o formulário para cadastro ou edição de um curso.
     *
     * @param paraEdicao Indica se é para edição (true) ou criação (false).
     * @throws IOException Se ocorrer um erro durante o redirecionamento.
     */
    public void prepararCadastroOuEdicao(boolean paraEdicao) throws IOException {
        if (paraEdicao) {
            if (cursoSelecionado == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum Curso selecionado para edição", ""));
                return;
            }
            try {
                // Buscar o curso completo com disciplinas
                Curso cursoCompleto = cursoDAO.find(cursoSelecionado.getIdCurso());
                if (cursoCompleto != null) {
                    cursoParaCadastro = cursoCompleto;
                    editar = true; // Define o modo de edição
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Curso não encontrado", ""));
                    return;
                }
            } catch (SQLException e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao buscar Curso: " + e.getMessage(), ""));
                return;
            }
        } else {
            cursoParaCadastro = new Curso();
          
            editar = false; // Define o modo de criação
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("cursoCadastro.xhtml"); // Redireciona para a página de cadastro/edição
    }

    public void excluirCurso() {
        try {
            if (cursoSelecionado == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum Curso selecionado para exclusão", ""));
                return;
            }

            cursoDAO.remove(cursoSelecionado.getIdCurso());
            preencherTabela();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Curso excluído com sucesso", ""));
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir Curso: " + e.getMessage(), ""));
        }
    }

    /**
     * Verifica se um curso com o nome especificado já existe.
     *
     * @param nome O nome do curso a ser verificado.
     * @return true se o curso existir, false caso contrário.
     */
    public boolean verificarNome(String nome) {
        try {
            Curso cursoEncontrado = cursoDAO.findByName(nome);
            if (cursoEncontrado != null) {
                return true;
            }
        } catch (SQLException e) {
            // Log de erro opcional
        }

        // Não tem nome igual
        return false;
    }
}

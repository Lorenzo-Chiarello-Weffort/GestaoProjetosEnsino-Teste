package controller;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import modelo.Professor;
import dao.ProfessorDAO;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Inject;
import java.io.IOException;
import java.sql.SQLException;
import util.HashUtils;

@Named
@SessionScoped
public class ProfessorController implements Serializable {

    private Professor professorParaCadastro;
    private Professor professorSelecionado;
    private ProfessorDAO professorDAO = new ProfessorDAO();
    private List<Professor> listaProfessor;
    private List<Professor> listaProfessoresPendentes;
    private boolean editar = false;

    @Inject
    private LoginController loginController;

    @PostConstruct
    public void preencherTabela() {
        try {
            listaProfessor = professorDAO.findAll();
            preencherListaPendentes();
        } catch (Exception e) {
            listaProfessor = null;
            listaProfessoresPendentes = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao preencher tabela: " + e.getMessage(), ""));
        }
    }
    
    public void preencherListaPendentes() {
        try {
            listaProfessoresPendentes = professorDAO.findPending();
        } catch (Exception e) {
            listaProfessoresPendentes = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao preencher tabela: " + e.getMessage(), ""));
        }
    }

    public Professor getProfessorParaCadastro() {
        if (professorParaCadastro == null) {
            professorParaCadastro = new Professor();
        }
        return professorParaCadastro;
    }

    public void setProfessorParaCadastro(Professor professorParaCadastro) {
        this.professorParaCadastro = professorParaCadastro;
    }

    public Professor getProfessorSelecionado() {
        return professorSelecionado;
    }

    public void setProfessorSelecionado(Professor professorSelecionado) {
        this.professorSelecionado = professorSelecionado;
    }

    public List<Professor> getListaProfessor() {
        return listaProfessor;
    }

    public boolean isEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public void salvarProfessor() {
        try {
            if (professorParaCadastro == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum Professor para salvar", ""));
                return;
            }

            if (editar) {
                professorParaCadastro.setSenha(HashUtils.gerarHash(professorParaCadastro.getSenha()));
                professorDAO.edit(professorParaCadastro);
                loginController.setUsuarioLogado(professorParaCadastro.getNome());
                editar = false;
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Professor atualizado com sucesso", ""));
            } else {
                String prontuarioEncontrado = loginController.verificarProntuario(professorParaCadastro.getProntuario());
                String emailEncontrado = loginController.verificarEmail(professorParaCadastro.getEmail());

                if (!prontuarioEncontrado.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Prontuário já existente", ""));
                    return;
                }
                if (!emailEncontrado.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email já existente", ""));
                    return;
                }

                professorParaCadastro.setSenha(HashUtils.gerarHash(professorParaCadastro.getSenha()));
                professorParaCadastro.setStatus("0");
                professorDAO.create(professorParaCadastro);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Professor criado com sucesso", ""));
            }
            preencherTabela();
            professorParaCadastro = new Professor();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar Professor: " + e.getMessage(), ""));
        }
    }

    public String prepararCadastroOuEdicao(boolean isEdicao) throws IOException {
        if (isEdicao) {
            if (loginController.getProfessorAutenticado() == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum professor selecionado para edição", ""));
                return null;
            }
            professorParaCadastro = loginController.getProfessorAutenticado();
            editar = true;
        } else {
            professorParaCadastro = new Professor();
            editar = false;
        }
        return "professorCadastro.xhtml?faces-redirect=true";
    }

    public void excluirProfessor() {
        try {
            if (professorSelecionado == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum Professor selecionado para exclusão", ""));
                return;
            }

            professorDAO.remove(professorSelecionado.getProntuario());
            preencherTabela();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Professor excluído com sucesso", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir Professor: " + e.getMessage(), ""));
        }
    }

    public void aprovarProfessor(Professor professor) throws SQLException {
        if (professor == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum professor selecionado", ""));
            return;
        }
        professor = professorDAO.find(professor.getProntuario());
        professor.setStatus("1");
        professorDAO.edit(professor);
        preencherTabela();
        preencherListaPendentes();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Professor aprovado com Sucesso", ""));
    }
    
    public List<Professor> getListaProfessoresPendentes() {
        if (listaProfessoresPendentes == null) {
            preencherListaPendentes();
        }
        return listaProfessoresPendentes;
    }

    public void setListaProfessoresPendentes(List<Professor> listaProfessoresPendentes) {
        this.listaProfessoresPendentes = listaProfessoresPendentes;
    }

    public ProfessorDAO getProfessorDAO() {
        return professorDAO;
    }

    public void setProfessorDAO(ProfessorDAO professorDAO) {
        this.professorDAO = professorDAO;
    }
}

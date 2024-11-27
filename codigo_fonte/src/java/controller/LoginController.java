/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.AlunoDAO;
import dao.CspDAO;
import dao.ProfessorDAO;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import modelo.Aluno;
import modelo.Csp;
import modelo.Professor;
import org.primefaces.PrimeFaces;
import util.HashUtils;

/**
 *
 * @author Lorenzo
 */
@Named
@SessionScoped
public class LoginController implements Serializable {

    private String prontuario = "";
    private String senha = "";
    private String usuarioLogado = "";
    private String usuarioTipo = "";

    private Csp cspAutenticado;
    private Professor professorAutenticado;
    private Aluno alunoAutenticado;

    @Inject
    private CspDAO cspDAO;

    @Inject
    private ProfessorDAO professorDAO;

    @Inject
    private AlunoDAO alunoDAO;

    @Inject
    private CspController cspController;

    @Inject
    private ProfessorController professorController;

    @Inject
    private AlunoController alunoController;

    public LoginController() {
        this.prontuario = "";
        this.senha = "";
    }

    public String verificarUsuarioAutenticado(String... tipos) {
        for (String tipo : tipos) {
            if (usuarioTipo.equals(tipo)) {
                return null;
            }
        }
        return "acessoNegado?faces-redirect=true";
    }

    // Verifica se não tá logado com nenhum usuário, retorna pro login
    public String verificarQualquerUsuarioAutenticado() {
        if (usuarioLogado.isEmpty()) {
            return "login?faces-redirect=true";
        }
        return null;
    }
    
    public String login() {
        String prontuarioEncontrado = verificarProntuario(prontuario);
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        try {
            if (prontuarioEncontrado.equals("csp")) {
                if (cspAutenticado.getSenha().equals(HashUtils.gerarHash(senha))) {
                    session.setAttribute("usuarioLogado", cspAutenticado.getNome());
                    session.setAttribute("usuarioTipo", "csp");
                    usuarioLogado = cspAutenticado.getNome();
                    usuarioTipo = "csp";
                    limparCampos();
                    return "index?faces-redirect=true";
                }
                else{
                       FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Senha incorreta", ""));
                }
            } else if (prontuarioEncontrado.equals("professor")) {
                if (professorAutenticado.getStatus().equals("0")) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL, "Seu cadastro deve ser ativado antes", ""));
                } else if (professorAutenticado.getSenha().equals(HashUtils.gerarHash(senha))) {
                    session.setAttribute("usuarioLogado", professorAutenticado.getNome());
                    session.setAttribute("usuarioTipo", "professor");
                    usuarioLogado = professorAutenticado.getNome();
                    usuarioTipo = "professor";
                    limparCampos();
                    return "index?faces-redirect=true";
                }
                 else{
                       FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Senha incorreta", ""));
                }
                
            } else if (prontuarioEncontrado.equals("aluno")) {
                if (alunoAutenticado.getSenha().equals(HashUtils.gerarHash(senha))) {
                    session.setAttribute("usuarioLogado", alunoAutenticado.getNome());
                    session.setAttribute("usuarioTipo", "aluno");
                    usuarioLogado = alunoAutenticado.getNome();
                    usuarioTipo = "aluno";
                    limparCampos();
                    return "index?faces-redirect=true";
                }
                 else{
                       FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Senha incorreta", ""));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário não encontrado", ""));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro: " + e.getMessage(), ""));
        }
        return null;
    }

    public String logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        limparCampos();
        this.usuarioLogado = "";
        this.usuarioTipo = "";
        PrimeFaces.current().ajax().update("messages");
        return "/login?faces-redirect=true";
    }

    public String verificarProntuario(String prontuario) {
        try {
            cspAutenticado = cspDAO.find(prontuario);
            if (cspAutenticado != null) {
                return "csp";
            }
        } catch (Exception e) {
            return "";
        }

        try {
            professorAutenticado = professorDAO.find(prontuario);
            if (professorAutenticado != null) {
                return "professor";
            }
        } catch (Exception e) {
            return "";
        }

        try {
            alunoAutenticado = alunoDAO.find(prontuario);
            if (alunoAutenticado != null) {
                return "aluno";
            }
        } catch (Exception e) {
            return "";
        }

        // Não tem prontuário igual
        return "";
    }

    public String verificarEmail(String email) {
        try {
            cspAutenticado = cspDAO.findOneByEmail(email);
            if (cspAutenticado != null) {
                return "csp";
            }
        } catch (Exception e) {
            return "";
        }

        try {
            professorAutenticado = professorDAO.findOneByEmail(email);
            if (professorAutenticado != null) {
                return "professor";
            }
        } catch (Exception e) {
            return "";
        }

        try {
            alunoAutenticado = alunoDAO.findOneByEmail(email);
            if (alunoAutenticado != null) {
                return "aluno";
            }
        } catch (Exception e) {
            return "";
        }

        // Não tem email igual
        return "";
    }

    private void limparCampos() {
        this.prontuario = "";
        this.senha = "";
    }

    public String abrirConfiguracoes() {
        try {
            switch (usuarioTipo) {
                case "csp":
                    cspController.prepararCadastroOuEdicao(true);
                    return "cspCadastro.xhtml?faces-redirect=true";
                case "professor":
                    professorController.prepararCadastroOuEdicao(true);
                    return "professorCadastro.xhtml?faces-redirect=true";
                case "aluno":
                    alunoController.prepararCadastroOuEdicao(true);
                    return "alunoCadastro.xhtml?faces-redirect=true";
            }
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao abrir configurações: " + e.getMessage(), ""));
        }
        return "";
    }

    public String getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(String usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public String getUsuarioTipo() {
        return usuarioTipo;
    }

    public void setUsuarioTipo(String usuarioTipo) {
        this.usuarioTipo = usuarioTipo;
    }

    public String getProntuario() {
        return prontuario;
    }

    public void setProntuario(String prontuario) {
        this.prontuario = prontuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Csp getCspAutenticado() {
        return cspAutenticado;
    }

    public void setCspAutenticado(Csp cspAutenticado) {
        this.cspAutenticado = cspAutenticado;
    }

    public Professor getProfessorAutenticado() {
        return professorAutenticado;
    }

    public void setProfessorAutenticado(Professor professorAutenticado) {
        this.professorAutenticado = professorAutenticado;
    }

    public Aluno getAlunoAutenticado() {
        return alunoAutenticado;
    }

    public void setAlunoAutenticado(Aluno alunoAutenticado) {
        this.alunoAutenticado = alunoAutenticado;
    }

    public CspDAO getCspDAO() {
        return cspDAO;
    }

    public void setCspDAO(CspDAO cspDAO) {
        this.cspDAO = cspDAO;
    }

    public ProfessorDAO getProfessorDAO() {
        return professorDAO;
    }

    public void setProfessorDAO(ProfessorDAO professorDAO) {
        this.professorDAO = professorDAO;
    }

    public AlunoDAO getAlunoDAO() {
        return alunoDAO;
    }

    public void setAlunoDAO(AlunoDAO alunoDAO) {
        this.alunoDAO = alunoDAO;
    }
}

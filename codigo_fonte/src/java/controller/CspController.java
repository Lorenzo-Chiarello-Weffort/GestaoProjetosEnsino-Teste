package controller;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import modelo.Csp;
import dao.CspDAO;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Inject;
import java.io.IOException;
import java.util.stream.Collectors;
import util.HashUtils;

@Named
@SessionScoped
public class CspController implements Serializable {

    @Inject
    private LoginController usuarioSessao;

    private Csp cspParaCadastro;
    private Csp cspSelecionado;
    private CspDAO cspDAO = new CspDAO();
    private List<Csp> listaCsp;
    private boolean editar = false; // Variável para controlar modo de edição

    @Inject
    private LoginController loginController;

    @PostConstruct
    public void preencherTabela() {
        try {
            if (usuarioSessao.getCspAutenticado() != null) {
                listaCsp = cspDAO.findAll().stream()
                        .filter(csp -> !csp.getProntuario().equals(usuarioSessao.getCspAutenticado().getProntuario()))
                        .collect(Collectors.toList());
            } else {
                listaCsp = cspDAO.findAll();
            }
        } catch (Exception e) {
            listaCsp = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao preencher tabela: " + e.getMessage(), ""));
        }
    }

    public Csp getCspParaCadastro() {
        if (cspParaCadastro == null) {
            cspParaCadastro = new Csp();
        }
        return cspParaCadastro;
    }

    public void setCspParaCadastro(Csp cspParaCadastro) {
        this.cspParaCadastro = cspParaCadastro;
    }

    public Csp getCspSelecionado() {
        return cspSelecionado;
    }

    public void setCspSelecionado(Csp cspSelecionado) {
        this.cspSelecionado = cspSelecionado;
    }

    public List<Csp> getListaCsp() {
        return listaCsp;
    }

    public boolean isEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public void salvarCsp() {
        try {
            if (cspParaCadastro == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum CSP para salvar", ""));
                return;
            }

            if (editar) {
                cspParaCadastro.setSenha(HashUtils.gerarHash(cspParaCadastro.getSenha()));
                cspDAO.edit(cspParaCadastro);
                loginController.setUsuarioLogado(cspParaCadastro.getNome());
                editar = false;
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "CSP atualizado com sucesso", ""));
            } else {
                String prontuarioEncontrado = loginController.verificarProntuario(cspParaCadastro.getProntuario());
                String emailEncontrado = loginController.verificarEmail(cspParaCadastro.getEmail());

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

                cspParaCadastro.setSenha(HashUtils.gerarHash(cspParaCadastro.getSenha()));
                cspDAO.create(cspParaCadastro);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "CSP criado com sucesso", ""));
            }
            preencherTabela();
            cspParaCadastro = new Csp();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar CSP: " + e.getMessage(), ""));

        }
    }

    public String prepararCadastroOuEdicao(boolean isEdicao) throws IOException {
        if (isEdicao) {
            if (loginController.getCspAutenticado() == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum CSP selecionado para edição", ""));
                return null;
            }
            // Preenche o formulário com os dados do CSP selecionado
            cspParaCadastro = loginController.getCspAutenticado();
            editar = true;
        } else {
            cspParaCadastro = new Csp(); // Cria um novo CSP para cadastro
            editar = false;
        }
        return "cspCadastro.xhtml?faces-redirect=true";
    }

    public void excluirCsp() {
        try {
            if (cspSelecionado == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum CSP selecionado para exclusão", ""));
                return;
            }

            cspDAO.remove(cspSelecionado.getProntuario());
            preencherTabela();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "CSP excluído com sucesso", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir CSP: " + e.getMessage(), ""));
        }
    }

    public void novoCsp() throws IOException {
        cspParaCadastro = new Csp();
        editar = false; // Define o modo de criação
    }
}

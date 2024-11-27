package controller;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import modelo.Aluno;
import dao.AlunoDAO;
import dao.ProjetoalunoDAO;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Projeto;
import modelo.Projetoaluno;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import util.HashUtils;

@Named
@SessionScoped
public class AlunoController implements Serializable {

    private Aluno alunoParaCadastro;
    private Aluno alunoSelecionado;
    private AlunoDAO alunoDAO = new AlunoDAO();
    private List<Aluno> listaAluno;
    private List<Aluno> listaAlunoprojeto = new ArrayList<>();
    private boolean editar = false;
    private Projeto projeto = new Projeto();
    private Projetoaluno projetoAluno = new Projetoaluno();
    private Projeto projetoSelecionado;
    private List<Projeto> listaProjeto;
    private List<Projetoaluno> listaProjetoAluno; // Lista de alunos para o projeto selecionado
    private ProjetoalunoDAO projetoaDAO = new ProjetoalunoDAO();
    private List<String> tipo = Arrays.asList("Integral", "Matutino", "Vesperino", "Noturno");
    private UploadedFile uploadedFile; // Para o arquivo carregado
    private byte[] historicoPdf;       // Armazena o histórico do aluno

    @Inject
    private LoginController loginController;

    @PostConstruct
    public void preencherTabela() {
        try {
            listaAluno = alunoDAO.findAll();
        } catch (Exception e) {
            listaAluno = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao preencher tabela: " + e.getMessage(), ""));
        }
    }

    public void buscarAlunosPorProjeto() throws SQLException {
        System.out.println("buscarAlunosPorProjeto");
        System.out.println(projetoSelecionado);
        try {
            if (projetoSelecionado != null) {
                listaAluno = alunoDAO.findAll();
                for (Aluno a : listaAluno) {
                    if (alunoDAO.verificarInscricao(a, projetoSelecionado)) {
                        listaAlunoprojeto.add(a);
                    }
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao pesquisar: " + e.getMessage(), ""));
        }
    }

    public boolean verificarSeAlunoTemSalaHora(Aluno aluno) {

        boolean resultado = alunoDAO.verificarSalaHora(aluno);

        if (resultado) {
            System.out.println("O aluno está associado a pelo menos um projeto com salaHora definida.");
        } else {
            System.out.println("O aluno não possui projetos com salaHora definida.");
        }
        return resultado;
    }

    public List<Aluno> getListaAlunoprojeto() {
        return listaAlunoprojeto;
    }

    public void setListaAlunoprojeto(List<Aluno> listaAlunoprojeto) {
        this.listaAlunoprojeto = listaAlunoprojeto;
    }

    public Date dataEntrevista(Projeto projeto) throws SQLException {
        return projetoaDAO.findDataEntrevistaByProjetoEAluno(
                projeto.getIdProjeto(),
                loginController.getAlunoAutenticado().getProntuario()
        );
    }

    public boolean isInscrito(Projeto projeto) {
        return projetoaDAO.isAlunoInscritoNoProjeto(loginController.getAlunoAutenticado(), projeto); // Retorna true se aluno estiver inscrito
    }

    public void carregarAlunos() throws SQLException {
        if (projetoSelecionado != null) {
            // Método que busca os alunos associados ao projeto
            listaProjetoAluno = projetoaDAO.findByProjeto(projetoSelecionado);
        }
    }

    // Getter e Setter para UploadedFile
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

// Retorna o arquivo atual para download
    public StreamedContent getHistoricoParaDownload() {
        try {
            alunoSelecionado = alunoDAO.find(loginController.getAlunoAutenticado().getProntuario());
            historicoPdf = alunoSelecionado.getHistorico();
            return DefaultStreamedContent.builder()
                    .name("historico.pdf")
                    .contentType("application/pdf")
                    .stream(() -> new ByteArrayInputStream(historicoPdf))
                    .build();
        } catch (SQLException ex) {
            Logger.getLogger(AlunoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

// Salva o PDF carregado
    public void salvarHistoricoPDF() {
        if (uploadedFile == null || uploadedFile.getContent() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Nenhum arquivo selecionado!", ""));
            return;
        }

        try {
            historicoPdf = uploadedFile.getContent(); // Armazena o conteúdo em memória
            alunoSelecionado = loginController.getAlunoAutenticado();
            alunoDAO.updateHistorico(alunoSelecionado.getProntuario(), historicoPdf);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Histórico salvo com sucesso!", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar histórico: " + e.getMessage(), ""));
        }
    }

    // Getter e Setter para 'projetoSelecionado'
    public Projeto getProjetoSelecionado() {
        return projetoSelecionado;
    }

    public void setProjetoSelecionado(Projeto projetoSelecionado) throws SQLException {
        this.projetoSelecionado = projetoSelecionado;
        carregarAlunos(); // Atualiza a lista de alunos quando um projeto é selecionado
    }

    public List<Projeto> getListaProjeto() {
        return listaProjeto;
    }

    public void setListaProjeto(List<Projeto> listaProjeto) {
        this.listaProjeto = listaProjeto;
    }

    public void setListaProjetoAluno(List<Projetoaluno> listaProjetoAluno) {
        this.listaProjetoAluno = listaProjetoAluno;
    }

    public Aluno getAlunoParaCadastro() {
        if (alunoParaCadastro == null) {
            alunoParaCadastro = new Aluno();
        }
        return alunoParaCadastro;
    }

    public AlunoDAO getAlunoDAO() {
        return alunoDAO;
    }

    public void setAlunoDAO(AlunoDAO alunoDAO) {
        this.alunoDAO = alunoDAO;
    }

    public List<String> getTipo() {
        return tipo;
    }

    public void setTipo(List<String> tipo) {
        this.tipo = tipo;
    }

    public void setAlunoParaCadastro(Aluno alunoParaCadastro) {
        this.alunoParaCadastro = alunoParaCadastro;
    }

    public Aluno getAlunoSelecionado() {
        return alunoSelecionado;
    }

    public void setAlunoSelecionado(Aluno alunoSelecionado) {
        this.alunoSelecionado = alunoSelecionado;
    }

    public List<Aluno> getListaAluno() {
        return listaAluno;
    }

    public boolean isEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void salvarAluno() {
        try {
            if (alunoParaCadastro == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum Aluno para salvar", ""));
                return;
            }

            if (editar) {
                alunoParaCadastro.setSenha(HashUtils.gerarHash(alunoParaCadastro.getSenha()));
                alunoDAO.edit(alunoParaCadastro);
                loginController.setUsuarioLogado(alunoParaCadastro.getNome());
                editar = false;
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Aluno atualizado com sucesso", ""));
            } else {
                String prontuarioEncontrado = loginController.verificarProntuario(alunoParaCadastro.getProntuario());
                String emailEncontrado = loginController.verificarEmail(alunoParaCadastro.getEmail());
                boolean cpfEncontrado = verificarCpf(alunoParaCadastro.getCpf());
                boolean rgEncontrado = verificarRg(alunoParaCadastro.getRg());

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

                if (cpfEncontrado) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "CPF já existente", ""));
                    return;
                }
                if (rgEncontrado) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "RG já existente", ""));
                    return;
                }

                alunoParaCadastro.setSenha(HashUtils.gerarHash(alunoParaCadastro.getSenha()));
                alunoDAO.create(alunoParaCadastro);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Aluno criado com sucesso", ""));

            }
            preencherTabela();
            alunoParaCadastro = new Aluno();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar Aluno: " + e.getMessage(), ""));
        }

    }

    public String prepararCadastroOuEdicao(boolean isEdicao) throws IOException {
        if (isEdicao) {
            if (loginController.getAlunoAutenticado() == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum aluno selecionado para edição", ""));
                return null;
            }
            alunoParaCadastro = loginController.getAlunoAutenticado();
            editar = true;
        } else {
            alunoParaCadastro = new Aluno();
            editar = false;
        }
        return "alunoCadastro.xhtml?faces-redirect=true";
    }

    public void excluirAluno() {
        try {
            if (alunoSelecionado == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum Aluno selecionado para exclusão", ""));
                return;
            }

            alunoDAO.remove(alunoSelecionado.getProntuario());
            preencherTabela();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Aluno excluído com sucesso", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir Aluno: " + e.getMessage(), ""));
        }
    }

    public void inscrever(Projeto projeto) {
        Aluno alunoAutenticado = loginController.getAlunoAutenticado();

        if (alunoAutenticado == null) {
            System.out.println("Aluno não encontrado");
            return;
        }

        // Verificar se o aluno já está inscrito no projeto
        boolean estaInscrito = alunoDAO.verificarInscricao(alunoAutenticado, projeto);
        if (estaInscrito) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Você já está inscrito nesse projeto", ""));
            return;
        }

        // Inscreve o aluno no projeto
        projetoAluno = new Projetoaluno();
        projetoAluno.setIdProjeto(projeto);
        projetoAluno.setProntuario(alunoAutenticado);
        projetoAluno.setRemunerado(null);
        projetoAluno.setValorbolsa(null);

        System.out.println(projetoAluno.getIdProjeto());

        alunoDAO.inscrever(projetoAluno);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Aluno inscrito com sucesso", ""));
    }

    public void novoAluno() throws IOException {
        alunoParaCadastro = new Aluno();
        editar = false;
    }

    public boolean verificarCpf(String cpf) {
        try {
            alunoSelecionado = alunoDAO.findByCpf(cpf);
            if (alunoSelecionado != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        // Não tem cpf igual
        return false;
    }

    public boolean verificarRg(String rg) {
        try {
            alunoSelecionado = alunoDAO.findByRg(rg);
            if (alunoSelecionado != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        // Não tem rg igual
        return false;
    }
}

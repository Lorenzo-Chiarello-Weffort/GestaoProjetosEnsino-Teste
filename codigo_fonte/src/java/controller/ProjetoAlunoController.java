package controller;

import dao.AlunoDAO;
import dao.ProjetoDAO;
import dao.ProjetoalunoDAO;
import dao.RelatorioDAO;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Aluno;
import modelo.Horario;
import modelo.Projeto;
import modelo.Projetoaluno;
import modelo.Relatoriopresencamensal;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

@Named
@SessionScoped
public class ProjetoAlunoController implements Serializable {

    @Inject
    private LoginController usuarioSessao;

    @Inject
    private ProjetoalunoDAO projetoAlunoDAO;

    @Inject
    private RelatorioDAO relatorioDAO;
    
    private Date dataEntrevista;
    private String salaHora;
    private Date dataRelatorio = new Date();
    private UploadedFile relatorioFinalPDF;

    private List<Projetoaluno> listaProjetoAluno = new ArrayList<>();
    private List<Projetoaluno> listaProjetoAlunoAprovados = new ArrayList<>();
    private List<Projetoaluno> listaProjetoAlunoEntrevista = new ArrayList<>();
    private List<Horario> listaHorario = new ArrayList<>();
    private Projetoaluno projetoAlunoSelecionado = new Projetoaluno();
    private Projeto projetoSelecionado = new Projeto();
    private Aluno alunoSelecionado;
    private List<Projetoaluno> listaAlunoProjetos;
    private List<Relatoriopresencamensal> listaProjetoRelatorio = new ArrayList<>();
    private AlunoDAO alunoDAO = new AlunoDAO();

    private Relatoriopresencamensal relatorio = new Relatoriopresencamensal();
    private Relatoriopresencamensal relatorioSelecionado = new Relatoriopresencamensal();

    private boolean editar;
    private boolean mostrarPainelAlunos = false;
    private boolean mostrarPainelRelatorios = false;

    @PostConstruct
    public void init() {
        try {
            carregarListaProjetoAluno();
            carregarProjetoAprovado();
            carregarAlunoEntrevista();
            if(usuarioSessao.getAlunoAutenticado() != null){
            preencherListaAlunoProjetos();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar a listas: " + e.getMessage(), ""));
        }
    }

    public void preencherListaAlunoProjetos() {
        try {
            listaAlunoProjetos = projetoAlunoDAO.findByAluno(usuarioSessao.getAlunoAutenticado());
        } catch (SQLException e) {
            listaAlunoProjetos = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao preencher tabela: " + e.getMessage(), ""));
        }
    }

    public void carregarAlunoPorProjeto() {
        if (projetoAlunoSelecionado == null) {
            System.out.println("Erro: List não foi inicializada");
            return;
        }
        try {
            listaProjetoAluno = projetoAlunoDAO.findByProjetoAluno(projetoAlunoSelecionado);
            mostrarPainelAlunos = true;
        } catch (SQLException ex) {
            listaProjetoAluno = null;
            Logger.getLogger(ProjetoAlunoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void carregarRelatorioPorProjeto() {
        System.out.println("projetoAlunoSelecionado " + projetoAlunoSelecionado);
        if (projetoAlunoSelecionado == null) {
            System.out.println("Erro: Lista não foi inicializada");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione um projeto!", ""));
            return;
        }
        try {
            listaProjetoAluno = projetoAlunoDAO.findByProjetoAlunoRelatorio(projetoAlunoSelecionado);
            mostrarPainelRelatorios = true;
        } catch (SQLException ex) {
            listaProjetoAluno = null;
            Logger.getLogger(ProjetoAlunoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void carregarAlunoAprovadoPorProjeto() {
        if (projetoAlunoSelecionado == null) {
            System.out.println("Erro: List não foi inicializada");
            return;
        }
        try {
            listaProjetoAluno = projetoAlunoDAO.findAlunosAprovadosByProjeto(projetoAlunoSelecionado.getIdProjeto());
            mostrarPainelAlunos = true;
        } catch (SQLException ex) {
            listaProjetoAluno = null;
            Logger.getLogger(ProjetoAlunoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void carregarHorario(Aluno prontuario) {
        try {
            listaHorario = projetoAlunoDAO.listarHorariosPorAluno(prontuario);
            System.out.println(listaHorario);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar a listas: " + e.getMessage(), ""));
        }
    }

    // Método para assinar o relatório pelo aluno
    public void assinarAluno() {
        try {
            Date hoje = Date.from(Instant.now());
            projetoAlunoSelecionado.setProntuario(usuarioSessao.getAlunoAutenticado());
            relatorio.setProjetoaluno(projetoAlunoSelecionado);
            relatorio.setArquivopresencapdf(relatorioFinalPDF.getContent());
            relatorio.setData(dataRelatorio);
            relatorio.setDtassinaaluno(hoje);
            relatorioDAO.create(relatorio);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Aluno assinou o relatório"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Erro ao assinar relatório: " + e.getMessage()));
        }
    }

    public Date getDataRelatorio() {
        return dataRelatorio;
    }

    public void setDataRelatorio(Date dataRelatorio) {
        this.dataRelatorio = dataRelatorio;
    }

    public UploadedFile getRelatorioFinalPDF() {
        return relatorioFinalPDF;
    }

    public void setRelatorioFinalPDF(UploadedFile relatorioFinalPDF) {
        this.relatorioFinalPDF = relatorioFinalPDF;
    }

    public Relatoriopresencamensal getRelatorio() {
        return relatorio;
    }

    public void setRelatorio(Relatoriopresencamensal relatorio) {
        this.relatorio = relatorio;
    }

    public Relatoriopresencamensal getRelatorioSelecionado() {
        return relatorioSelecionado;
    }

    public void setRelatorioSelecionado(Relatoriopresencamensal relatorioSelecionado) {
        this.relatorioSelecionado = relatorioSelecionado;
    }

    public List<Horario> getListaHorario() {
        return listaHorario;
    }

    public void setListaHorario(List<Horario> listaHorario) {
        this.listaHorario = listaHorario;
    }

    public void carregarAlunoEntrevista() {
        try {
            listaProjetoAlunoEntrevista = projetoAlunoDAO.findByStatusEntrevista();
        } catch (Exception ex) {
            listaProjetoAlunoEntrevista = null;
            Logger.getLogger(ProjetoAlunoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void carregarProjetoAprovado() {
        try {
            listaProjetoAlunoAprovados = projetoAlunoDAO.findAllUniqueProjects();
        } catch (Exception ex) {
            listaProjetoAlunoAprovados = null;
            Logger.getLogger(ProjetoAlunoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void carregarListaProjetoAluno() {
        try {
            listaProjetoAluno = projetoAlunoDAO.findAll();
            if (listaProjetoAluno != null) {
                listaProjetoAluno.forEach(projetoAluno -> {
                    projetoAluno.getIdProjeto().getTitulo(); // Garante que o título do projeto é carregado.
                    projetoAluno.getProntuario().getNome(); // Garante que o nome do aluno é carregado.
                });
            }
        } catch (Exception e) {
            listaProjetoAluno = new ArrayList<>();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar a lista de projetos: " + e.getMessage(), ""));
        }
    }

    public void salvarProjetoAluno() {
        try {
            if (projetoAlunoSelecionado == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum aluno selecionado para salvar", ""));
                return;
            }
            if (editar) {
                System.out.println("Salvar edição: " + projetoAlunoSelecionado);
                projetoAlunoDAO.edit(projetoAlunoSelecionado);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro atualizado com sucesso", ""));
            } else {
                projetoAlunoDAO.create(projetoAlunoSelecionado);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso", ""));
            }
            carregarListaProjetoAluno();
            projetoAlunoSelecionado = null;
            editar = false;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar registro: " + e.getMessage(), ""));
        }
    }

    public void editarProjetoAluno(Projetoaluno projetoAluno) {
        System.out.println("Edição");
        this.editar = true;
    }

    public void excluirProjetoAluno(Projetoaluno projetoAluno) {
        try {
            projetoAlunoDAO.remove(projetoAluno);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro excluído com sucesso", ""));
            carregarListaProjetoAluno();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir registro: " + e.getMessage(), ""));
        }
    }

    public void aprovarEntrevista(Projetoaluno projetoAluno) {
        projetoAluno.setStatusAluno("Entrevista");

        System.out.println("Data Entrevista: " + dataEntrevista);
        System.out.println("Sala e Hora: " + salaHora);

        projetoAluno.setDataEntrevista(dataEntrevista);
        projetoAluno.setSalaHora(salaHora);;
        System.out.println(projetoAluno);
        projetoAlunoDAO.edit(projetoAluno);
        carregarAlunoEntrevista();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro atualizado com sucesso", ""));

    }

    public void cancelarAluno(Projetoaluno projetoAluno) {
        projetoAluno.setStatusAluno("Cancelado");
        editarProjetoAluno(projetoAluno);
    }

    public void aprovarAluno(Projetoaluno projetoAluno) {
        if (!projetoAlunoDAO.verificarLimiteBolsistas(projetoAluno.getIdProjeto())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Numero de Bolsistas maximo atingido", ""));
        } else {
            try {
                projetoAluno.setStatusAluno("Aprovado");
                projetoAluno.setRemunerado("1");
                projetoAluno.setValorbolsa(projetoAlunoDAO.findValorBolsaByIdProjeto(projetoAluno.getIdProjeto().getIdProjeto()));
                editarProjetoAluno(projetoAluno);
                projetoAlunoDAO.edit(projetoAluno);
                carregarAlunoEntrevista();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro atualizado com sucesso", ""));
            } catch (SQLException e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao associar projeto com aluno: " + e.getMessage(), ""));
            }
        }
    }

    public void aprovarAlunoVoluntario(Projetoaluno projetoAluno) {
        projetoAluno.setStatusAluno("Aprovado");
        projetoAluno.setRemunerado("0");
        projetoAluno.setValorbolsa("0");
        editarProjetoAluno(projetoAluno);
        projetoAlunoDAO.edit(projetoAluno);
        carregarAlunoEntrevista();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro atualizado com sucesso", ""));
    }

    public void associarProjetoComAluno(Projeto projeto) {
        try {
            if (projeto == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum projeto selecionado", ""));
                return;
            }
            this.projetoSelecionado = projeto;
            carregarListaProjetoAluno();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao associar projeto com aluno: " + e.getMessage(), ""));
        }
    }
    
    public StreamedContent getFileByAluno(Aluno aluno) throws SQLException {
        System.out.println("entrei");
        Aluno alunoS = alunoDAO.find(aluno.getProntuario());
          System.out.println("achei" + alunoS);
        if (alunoS != null && alunoS.getHistorico()!= null) { // Considerando que 'pdf' armazena o arquivo em bytes
            InputStream stream = new ByteArrayInputStream(alunoS.getHistorico());
            return DefaultStreamedContent.builder()
                    .name("Historico_" + aluno.getNome() + ".pdf")
                    .contentType("application/pdf")
                    .stream(() -> stream)
                    .build();
        }
        return null; // Ou uma mensagem de erro apropriada
    }    
    
    public List<Projetoaluno> getListaProjetoAlunoAprovados() {
        return listaProjetoAlunoAprovados;
    }

    public void setListaProjetoAlunoAprovados(List<Projetoaluno> listaProjetoAlunoAprovados) {
        this.listaProjetoAlunoAprovados = listaProjetoAlunoAprovados;
    }

    public List<Relatoriopresencamensal> getListaProjetoRelatorio() {
        return listaProjetoRelatorio;
    }

    public void setListaProjetoRelatorio(List<Relatoriopresencamensal> listaProjetoRelatorio) {
        this.listaProjetoRelatorio = listaProjetoRelatorio;
    }

    public LoginController getUsuarioSessao() {
        return usuarioSessao;
    }

    public void setUsuarioSessao(LoginController usuarioSessao) {
        this.usuarioSessao = usuarioSessao;
    }

    public Date getDataEntrevista() {
        return dataEntrevista;
    }

    public void setDataEntrevista(Date dataEntrevista) {
        this.dataEntrevista = dataEntrevista;
    }

    public String getSalaHora() {
        return salaHora;
    }

    public void setSalaHora(String salaHora) {
        this.salaHora = salaHora;
    }

    // Getters e Setters
    public List<Projetoaluno> getListaProjetoAluno() {
        return listaProjetoAluno;
    }

    public void setListaProjetoAluno(List<Projetoaluno> listaProjetoAluno) {
        this.listaProjetoAluno = listaProjetoAluno;
    }

    public List<Projetoaluno> getListaProjetoAlunoEntrevista() {
        return listaProjetoAlunoEntrevista;
    }

    public void setListaProjetoAlunoEntrevista(List<Projetoaluno> listaProjetoAlunoEntrevista) {
        this.listaProjetoAlunoEntrevista = listaProjetoAlunoEntrevista;
    }

    public Projetoaluno getProjetoAlunoSelecionado() {
        return projetoAlunoSelecionado;
    }

    public void setProjetoAlunoSelecionado(Projetoaluno projetoAlunoSelecionado) {
        this.projetoAlunoSelecionado = projetoAlunoSelecionado;
    }

    public Projeto getProjetoSelecionado() {
        return projetoSelecionado;
    }

    public void setProjetoSelecionado(Projeto projetoSelecionado) {
        this.projetoSelecionado = projetoSelecionado;
    }

    public boolean isEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public boolean isMostrarPainelAlunos() {
        return mostrarPainelAlunos;
    }

    public void setMostrarPainelAlunos(boolean mostrarPainelAlunos) {
        this.mostrarPainelAlunos = mostrarPainelAlunos;
    }

    public boolean isMostrarPainelRelatorios() {
        return mostrarPainelRelatorios;
    }

    public void setMostrarPainelRelatorios(boolean mostrarPainelRelatorios) {
        this.mostrarPainelRelatorios = mostrarPainelRelatorios;
    }

    public Aluno getAlunoSelecionado() {
        return alunoSelecionado;
    }

    public void setAlunoSelecionado(Aluno alunoSelecionado) {
        this.alunoSelecionado = alunoSelecionado;
    }

    public List<Projetoaluno> getListaAlunoProjetos() {
        return listaAlunoProjetos;
    }

    public void setListaAlunoProjetos(List<Projetoaluno> listaAlunoProjetos) {
        this.listaAlunoProjetos = listaAlunoProjetos;
    }

    public AlunoDAO getAlunoDAO() {
        return alunoDAO;
    }

    public void setAlunoDAO(AlunoDAO alunoDAO) {
        this.alunoDAO = alunoDAO;
    }
}

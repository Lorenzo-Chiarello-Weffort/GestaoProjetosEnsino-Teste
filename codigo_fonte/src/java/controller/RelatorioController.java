package controller;

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
import modelo.Relatoriopresencamensal;
import java.io.Serializable;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import modelo.Projetoaluno;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

@Named
@SessionScoped
public class RelatorioController implements Serializable {

    private Relatoriopresencamensal relatorio = new Relatoriopresencamensal();
    private Relatoriopresencamensal relatorioSelecionado = new Relatoriopresencamensal();
    private List<Relatoriopresencamensal> listrelatorio = new ArrayList<>();
    private List<Relatoriopresencamensal> listTodosRelatorios = new ArrayList<>();
    private List<Relatoriopresencamensal> listaRelatorioProjetos;
    private Projetoaluno projetoSelecionado;
    private List<Projetoaluno> listaProjetoAlunoAprovados = new ArrayList<>();
    private Projetoaluno projetoAlunoSelecionado = new Projetoaluno();
    private List<Relatoriopresencamensal> listaRelatorio = new ArrayList<>();

    private Date dataRelatorio;
    private List<Map<String, Object>> relatorios;

    private UploadedFile relatorioFinalPDF;

    @Inject
    private ProjetoalunoDAO projetoAlunoDAO;

    @Inject
    private RelatorioDAO relatorioDAO;

    @Inject
    private LoginController usuario;

    @PostConstruct
    public void init() {

        try {
            carregarProjetoAprovado();
            preencherTodosRelatorio();
            if (usuario.getAlunoAutenticado() != null) {
                preencherTabelaRelatorio();
                preencherRelatorio();
            }
            if (usuario.getProfessorAutenticado() != null) {
                preencherTabelaRelatorioProfessor();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao preencher tabela: " + e.getMessage(), ""));
        }
    }

    private List<Map<String, Object>> relatoriosMensais;

    public RelatorioController() {
        try {
            carregarRelatoriosMensais();
        } catch (SQLException e) {
            e.printStackTrace();
            // Você pode adicionar uma mensagem JSF para exibir erros, caso necessário
        }
    }

    public List<Map<String, Object>> getRelatoriosMensais() {
        return relatoriosMensais;
    }

    private void carregarRelatoriosMensais() throws SQLException {
        RelatorioDAO dao = new RelatorioDAO(); // Instancie seu DAO
        relatoriosMensais = dao.findRelatoriosMensaisComDetalhes();
    }

    public List<Map<String, Object>> getRelatorios() {
        return relatorios;
    }

    public void preencherRelatorio() throws SQLException {
        listrelatorio = relatorioDAO.findAllByAluno(usuario.getAlunoAutenticado());
    }

    public void preencherTodosRelatorio() throws SQLException {
        listTodosRelatorios = relatorioDAO.findAll();
    }

    public void preencherTabelaRelatorioProfessor() {
        try {
            listaRelatorio = relatorioDAO.findByProfessor(usuario.getProfessorAutenticado());
        } catch (SQLException ex) {
            listaRelatorio = null;
        }
    }

    public void preencherTabelaRelatorio() {
        try {
            listaRelatorio = relatorioDAO.findAllByAluno(usuario.getAlunoAutenticado());
        } catch (SQLException ex) {
            listaRelatorio = null;
        }
    }

    public void removerRelatorio(Relatoriopresencamensal relatorio) {
        relatorioDAO.remove(relatorio);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Relatório Removido com sucesso!", ""));
    }

    public void assinarProfessor(Relatoriopresencamensal relatorio) {
        try {
            Date hoje = Date.from(Instant.now());
            relatorio.setDtassinaprofessor(hoje);
            relatorioDAO.edit(relatorio); // Atualizar no banco de dados
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Professor assinou o relatório", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao assinar o relatorio: " + e.getMessage(), ""));
        }
    }

    public void carregarProjetoAprovado() {
        try {
            listaProjetoAlunoAprovados = projetoAlunoDAO.findAllUniqueProjects();
        } catch (Exception e) {
            listaProjetoAlunoAprovados = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar lista: " + e.getMessage(), ""));
        }
    }

    public StreamedContent getFileById(int idrelatorio) throws SQLException {
        // Busca o conteúdo do arquivo PDF pelo ID
        System.out.println("id" + idrelatorio);
        byte[] arquivoPdf = relatorioDAO.getArquivoPdfById(idrelatorio);
        if (arquivoPdf != null) {
            InputStream stream = new ByteArrayInputStream(arquivoPdf);
            return DefaultStreamedContent.builder()
                    .name("Relatorio_" + idrelatorio + ".pdf") // Adapte o nome do arquivo conforme necessário
                    .contentType("application/pdf")
                    .stream(() -> stream)
                    .build();
        }
        // Retorna null se o relatório não for encontrado ou não tiver arquivo PDF
        return null;
    }

    public List<Relatoriopresencamensal> getListaRelatorioProjetos() {
        return listaRelatorioProjetos;
    }

    public void setListaRelatorioProjetos(List<Relatoriopresencamensal> listaRelatorioProjetos) {
        this.listaRelatorioProjetos = listaRelatorioProjetos;
    }

    public Date getDataRelatorio() {
        return dataRelatorio;
    }

    public void setDataRelatorio(Date dataRelatorio) {
        this.dataRelatorio = dataRelatorio;
    }

    public Relatoriopresencamensal getRelatorioSelecionado() {
        return relatorioSelecionado;
    }

    public void setRelatorioSelecionado(Relatoriopresencamensal relatorioSelecionado) {
        this.relatorioSelecionado = relatorioSelecionado;
    }

    public List<Relatoriopresencamensal> getListrelatorio() {
        return listrelatorio;
    }

    public void setListrelatorio(List<Relatoriopresencamensal> listrelatorio) {
        this.listrelatorio = listrelatorio;
    }

    public List<Relatoriopresencamensal> getListTodosRelatorios() {
        return listTodosRelatorios;
    }

    public void setListTodosRelatorios(List<Relatoriopresencamensal> listTodosRelatorios) {
        this.listTodosRelatorios = listTodosRelatorios;
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

    public Projetoaluno getProjetoSelecionado() {
        return projetoSelecionado;
    }

    public void setProjetoSelecionado(Projetoaluno projetoSelecionado) {
        this.projetoSelecionado = projetoSelecionado;
    }

    public List<Projetoaluno> getListaProjetoAlunoAprovados() {
        return listaProjetoAlunoAprovados;
    }

    public void setListaProjetoAlunoAprovados(List<Projetoaluno> listaProjetoAlunoAprovados) {
        this.listaProjetoAlunoAprovados = listaProjetoAlunoAprovados;
    }

    public Projetoaluno getProjetoAlunoSelecionado() {
        return projetoAlunoSelecionado;
    }

    public void setProjetoAlunoSelecionado(Projetoaluno projetoAlunoSelecionado) {
        this.projetoAlunoSelecionado = projetoAlunoSelecionado;
    }

    public List<Relatoriopresencamensal> getListaRelatorio() {
        return listaRelatorio;
    }

    public void setListaRelatorio(List<Relatoriopresencamensal> listaRelatorio) {
        this.listaRelatorio = listaRelatorio;
    }
}

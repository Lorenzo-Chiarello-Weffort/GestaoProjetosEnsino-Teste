/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.DisciplinaProjetoDAO;
import dao.ProfessorDAO;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import modelo.Projeto;
import dao.ProjetoDAO;
import dao.ProjetoalunoDAO;
import jakarta.enterprise.context.SessionScoped;
import java.io.IOException;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Disciplina;
import modelo.DisciplinaProjeto;
import modelo.Professor;
import modelo.Projetoaluno;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

@Named
@SessionScoped
public class ProjetoController implements Serializable {

    @Inject
    private LoginController usuarioSessao;

    @Inject
    private DisciplinaProjetoDAO DPdao = new DisciplinaProjetoDAO();
    private Projetoaluno projetoAlunoSelecionado;
    private ProjetoalunoDAO paDAO = new ProjetoalunoDAO();
    private List<Projetoaluno> projetoAlunoList = new ArrayList<>();

    private List<Disciplina> DisList = new ArrayList<>();
    private Projeto projetoParaCadastro;
    private Projeto projetoSelecionado;
    private ProjetoDAO projetoDAO = new ProjetoDAO();
    private List<Projeto> listaProjeto;
    private List<Projeto> listaProjetoAprovados;
    private List<Projeto> listaProjetoCorrecoes;
    private boolean editar = false;
    private ProfessorDAO professordao = new ProfessorDAO();

    private UploadedFile arquivoPDF;
    private UploadedFile relatorioFinalPDF;
    private String statusSelecionado;
    private String observacoes;
    private Float valorRemuneracao;
    private Integer numBolsa;
    private Date hoje = new Date();

    public Projetoaluno getProjetoAlunoSelecionado() {
        return projetoAlunoSelecionado;
    }

    public void setProjetoAlunoSelecionado(Projetoaluno projetoAlunoSelecionado) {
        this.projetoAlunoSelecionado = projetoAlunoSelecionado;
    }

    public List<Projetoaluno> getProjetoAlunoList() {
        return projetoAlunoList;
    }

    public void setProjetoAlunoList(List<Projetoaluno> projetoAlunoList) {
        this.projetoAlunoList = projetoAlunoList;
    }

    public String getStatusSelecionado() {
        return statusSelecionado;
    }

    public void setStatusSelecionado(String statusSelecionado) {
        this.statusSelecionado = statusSelecionado;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Float getValorRemuneracao() {
        return valorRemuneracao;
    }

    public void setValorRemuneracao(Float valorRemuneracao) {
        this.valorRemuneracao = valorRemuneracao;
    }

    public Integer getNumBolsa() {
        return numBolsa;
    }

    public void setNumBolsa(Integer numBolsa) {
        this.numBolsa = numBolsa;
    }

    public StreamedContent getFileByTitulo(String titulo) throws SQLException {
        Projeto projeto = projetoDAO.findByTitulo(titulo);
        if (projeto != null && projeto.getArquivopdf() != null) { // Considerando que 'pdf' armazena o arquivo em bytes
            InputStream stream = new ByteArrayInputStream(projeto.getArquivopdf());
            return DefaultStreamedContent.builder()
                    .name("projeto_" + titulo + ".pdf")
                    .contentType("application/pdf")
                    .stream(() -> stream)
                    .build();
        }
        return null; // Ou uma mensagem de erro apropriada
    }

    public Professor getUsuarioSessao() {
        return usuarioSessao.getProfessorAutenticado();
    }

    public void setUsuarioSessao(LoginController usuarioSessao) {
        this.usuarioSessao = usuarioSessao;
    }

    @PostConstruct
    public void preencherTabela() {
        try {
            listaProjeto = projetoDAO.findAll();
            for(Projeto p: listaProjeto){
                if(p.getDataTermino().before(hoje)){
                    p.setStatus("Inativo");
                    projetoDAO.edit(p);
                }
            }
            listaProjeto = projetoDAO.findAll();
            preencherListaAprovados();
            preencherListaCorrecoes();
            preencherListaProjetosAluno();
            getUsuarioSessao();
        } catch (Exception e) {
            listaProjeto = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao preencher tabela: " + e.getMessage(), ""));
        }
    }

    public Date getHoje() {
        return hoje;
    }

    public void setHoje(Date hoje) {
        this.hoje = hoje;
    }
    
    

    public void buscarAlunosPorProjeto() throws SQLException {
        if (projetoSelecionado != null) {
            projetoAlunoList = paDAO.findByProjeto(projetoSelecionado);
        }
    }

    public void preencherListaProjetosAluno() {
        projetoAlunoList = paDAO.findAllUniqueProjects();
    }

    public void preencherListaAprovados() {
        try {
            System.out.println("bom");
            listaProjetoAprovados = projetoDAO.findByTipoAprovado();
        } catch (Exception e) {
            listaProjetoAprovados = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao preencher tabela: " + e.getMessage(), ""));
        }
    }
    
    public void preencherListaCorrecoes() {
        try {
            listaProjetoCorrecoes = projetoDAO.findByTipoCorrecao();
        } catch (Exception e) {
            listaProjetoCorrecoes = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao preencher tabela: " + e.getMessage(), ""));
        }
    }

    public List<Disciplina> getDisList() {
        return DisList;
    }

    public void setDisList(List<Disciplina> DisList) {
        this.DisList = DisList;
    }

    public Projeto getProjetoParaCadastro() {
        if (projetoParaCadastro == null) {
            projetoParaCadastro = new Projeto();
        }
        return projetoParaCadastro;
    }

    public void setProjetoParaCadastro(Projeto projetoParaCadastro) {
        this.projetoParaCadastro = projetoParaCadastro;
    }

    public Projeto getProjetoSelecionado() {
        return projetoSelecionado;
    }

    public void setProjetoSelecionado(Projeto projetoSelecionado) {
        this.projetoSelecionado = projetoSelecionado;
    }

    public List<Projeto> getListaProjeto() {
        return listaProjeto;
    }

    public boolean isEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public UploadedFile getArquivoPDF() {
        return arquivoPDF;
    }

    public void setArquivoPDF(UploadedFile arquivoPDF) {
        this.arquivoPDF = arquivoPDF;
    }

    public UploadedFile getRelatorioFinalPDF() {
        return relatorioFinalPDF;
    }

    public void setRelatorioFinalPDF(UploadedFile relatorioFinalPDF) {
        this.relatorioFinalPDF = relatorioFinalPDF;
    }

    public void aprovarProjeto(Projeto projeto) throws SQLException {
        try {
            if (projeto == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum Projeto Selecionado", ""));
                return;
            }
            System.out.println(numBolsa);
            projeto = projetoDAO.findByTitulo(projeto.getTitulo());
            projeto.setRemuneracao(valorRemuneracao);
            projeto.setNumBolsista(numBolsa);
            projeto.setStatus("Aprovado");
            System.out.println(valorRemuneracao);
            projetoDAO.edit(projeto);
            System.out.println(valorRemuneracao);
            preencherTabela();
            preencherListaAprovados();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Projeto Aprovado com Sucesso", ""));

        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao aprovar projeto: " + e.getMessage(), ""));
        }
    }

    public void enviarParaCorrecao(Projeto projeto) {
        try {
            if (projeto == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum Projeto Selecionado", ""));
                return;
            }
            if (this.observacoes == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Faça uma observação de correção!", ""));
                return;
            }
            float remuneracao = (float) 0.0;
            System.out.println(remuneracao);
            projeto = projetoDAO.findByTitulo(projeto.getTitulo());
            projeto.setObservacoes(observacoes);
            projeto.setStatus("Correção");
            projeto.setRemuneracao(remuneracao);
            System.out.println(remuneracao);
            projetoDAO.edit(projeto);
            System.out.println(remuneracao);
            preencherTabela();
            preencherListaAprovados();
            preencherListaCorrecoes();
            observacoes = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Projeto enviado para Correção", ""));

        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao enviar projeto para correção: " + e.getMessage(), ""));
        }
    }

    public void PendenteProjeto(Projeto projeto) throws SQLException {
        if (projeto == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum Projeto Selecionado", ""));
            return;
        }
        projeto = projetoDAO.find(projeto.getIdProjeto());
        projeto.setStatus("Pendente");

        projetoDAO.edit(projeto);
        preencherTabela();
        preencherListaAprovados();
    }

    public void salvarStatus(Projeto projeto) throws SQLException {
        if ("Correção".equals(statusSelecionado)) {
            projeto.setStatus(statusSelecionado);
            projeto.setObservacoes(observacoes);
        } else if ("Aprovado".equals(statusSelecionado)) {
            projeto.setStatus(statusSelecionado);
            projeto.setObservacoes(null);
        }
        // Atualize o projeto no banco de dados
        projetoDAO.edit(projeto);
    }

    public List<Projeto> getListaProjetoAprovados() {
        if (listaProjetoAprovados == null) {
            preencherListaAprovados();
        }
        return listaProjetoAprovados;
    }

    public void setListaProjetoAprovados(List<Projeto> listaProjetoAprovados) {
        this.listaProjetoAprovados = listaProjetoAprovados;
    }
    
    public List<Projeto> getListaProjetoCorrecoes() {
        return listaProjetoCorrecoes;
    }
    public void setListaProjetoCorrecoes(List<Projeto> listaProjetoCorrecoes) {
        this.listaProjetoCorrecoes = listaProjetoCorrecoes;
    }
    
    public void relatorioFinal(Projeto projeto){
        try {
              projeto.setRelatoriofinalpdf(relatorioFinalPDF.getContent());
            projetoDAO.edit(projeto);
        } catch (SQLException ex) {
            Logger.getLogger(ProjetoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void salvarProjeto() {
        try {
            if (projetoParaCadastro == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum Projeto para salvar", ""));
                return;
            }

            // Tratamento dos arquivos PDF
            if (arquivoPDF != null && !arquivoPDF.getFileName().isEmpty()) {
                if (!arquivoPDF.getFileName().toLowerCase().endsWith(".pdf")) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "O arquivo deve ser um PDF", ""));
                    return;
                }
                projetoParaCadastro.setArquivopdf(arquivoPDF.getContent());
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "É necessário selecionar um arquivo PDF", ""));
                return;
            }
            if (projetoParaCadastro.getDataInicio() == null || projetoParaCadastro.getDataTermino() == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "As datas de início e término são obrigatórias", ""));
                return;
            }

            if (projetoParaCadastro.getDataInicio().after(projetoParaCadastro.getDataTermino())) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "A data de início não pode ser posterior à data de término", ""));
                return;
            }

            // Atualiza o status do edital com base na data atual
            Date hoje = Date.from(Instant.now());
            if (hoje.after(projetoParaCadastro.getDataTermino())) {
                projetoParaCadastro.setStatus("Inativo");
            } else {
                projetoParaCadastro.setStatus("Ativo");

            }

            projetoParaCadastro.setProntuario(usuarioSessao.getProfessorAutenticado());
            if (projetoParaCadastro.getProntuario() == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Professor Responsável é obrigatório", ""));
                return;
            }

            // Chamada ao DAO para criar ou editar
            if (editar) {
                projetoDAO.edit(projetoParaCadastro);
                editar = false;
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Projeto atualizado com sucesso", ""));
            } else {

                if (DisList == null || DisList.isEmpty()) {
                    System.out.println("A lista DisList está vazia");
                } else {
                    System.out.println("cadastro");
                    projetoParaCadastro.setStatus("Pendente");
                    System.out.println(projetoParaCadastro.getTitulo());
                    projetoDAO.create(projetoParaCadastro);

                    for (Disciplina d : DisList) {
                        DisciplinaProjeto DP = new DisciplinaProjeto();
                        System.out.println(d.getNome());
                        DP.setIdProjeto(projetoDAO.findByTitulo(projetoParaCadastro.getTitulo()));
                        DP.setIddisciplina(d);
                        DP.setNome(d.getNome());
                        DPdao.create(DP);
                    }
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Projeto criado com sucesso", ""));
                }
            }

            // Atualizar a tabela e resetar o objeto
            preencherTabela();
            projetoParaCadastro = new Projeto();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar Projeto: " + e.getMessage(), ""));
        }
    }

    public String prepararCadastroOuEdicao(boolean isEdicao) throws IOException {
        if (isEdicao) {
            if (projetoSelecionado == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum Projeto selecionado para edição", ""));
                return null;
            }
            projetoParaCadastro = projetoSelecionado;
            editar = true;
        } else {
            projetoParaCadastro = new Projeto();
            editar = false;
        }
        return "projetoCadastro.xhtml?faces-redirect=true";
    }

    public void excluirProjeto() {
        try {
            if (projetoSelecionado == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum Projeto selecionado para exclusão", ""));
                return;
            }

            projetoDAO.remove(projetoSelecionado.getIdProjeto());
            preencherTabela();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Projeto excluído com sucesso", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir Projeto: " + e.getMessage(), ""));
        }
    }

    public void novoProjeto() throws IOException {
        projetoParaCadastro = new Projeto();
        editar = false;
    }

}

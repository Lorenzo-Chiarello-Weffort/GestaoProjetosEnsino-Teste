package controller;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import modelo.Edital;
import dao.EditalDAO;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

@Named
@SessionScoped
public class EditalController implements Serializable {

    private Edital editalParaCadastro = new Edital();
    private Edital editalParaChecar = new Edital();
    private Edital editalSelecionado = new Edital();
    private List<Edital> listaEdital = new ArrayList<>();
    private boolean editar = false;
    private UploadedFile arquivoPDF;
    private UploadedFile relatorioFinalPDF;

    @Inject
    private EditalDAO editalDAO = new EditalDAO();

    ;

    @PostConstruct
    public void preencherTabela() {
        try {
            listaEdital = editalDAO.findAll();
            Date hoje = Date.from(Instant.now());
            boolean necessidadeAtualizacao = false;

            for (Edital edital : listaEdital) {
                if ("Ativo".equals(edital.getStatus()) && hoje.after(edital.getDatafechamento())) {
                    edital.setStatus("Inativo");
                    editalDAO.edit(edital);
                    necessidadeAtualizacao = true;
                }
                if ("Inativo".equals(edital.getStatus()) && hoje.after(edital.getDatafechamento())) {
                    edital.setStatus("Ativo");
                    editalDAO.edit(edital);
                    necessidadeAtualizacao = true;
                }
            }

            if (necessidadeAtualizacao) {
                listaEdital = editalDAO.findAll();
            }

        } catch (Exception e) {
            listaEdital = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao preencher tabela: " + e.getMessage(), ""));
        }
    }

    public boolean isProjetoAprovadoSelecionado() {
        return "projeto_aprovado".equals(editalParaCadastro.getTipoedital());
    }

    public void onTipoEditalChange() {
        // Apenas para garantir que o valor foi atualizado corretamente
        if ("projeto_aprovado".equals(editalParaCadastro.getTipoedital())) {
            isProjetoAprovadoSelecionado();
        }
    }

    public StreamedContent getFileById(String editalId) throws SQLException {
        Edital edital = editalDAO.findByIdedital(editalId);
        if (edital != null && edital.getDocpdfedital() != null) { // Considerando que 'pdf' armazena o arquivo em bytes
            InputStream stream = new ByteArrayInputStream(edital.getDocpdfedital());
            return DefaultStreamedContent.builder()
                    .name("edital_" + editalId + ".pdf")
                    .contentType("application/pdf")
                    .stream(() -> stream)
                    .build();
        }
        return null; // Ou uma mensagem de erro apropriada
    }

    public Edital getEditalParaCadastro() {
        if (editalParaCadastro == null) {
            editalParaCadastro = new Edital();
        }
        return editalParaCadastro;
    }

    public void setEditalParaCadastro(Edital editalParaCadastro) {
        this.editalParaCadastro = editalParaCadastro;
    }

    public Edital getEditalSelecionado() {
        return editalSelecionado;
    }

    public void setEditalSelecionado(Edital editalSelecionado) {
        this.editalSelecionado = editalSelecionado;
    }

    public List<Edital> getListaEdital() {
        return listaEdital;
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

    public void salvarEdital() {
        try {
            if (editalParaCadastro == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum Edital para salvar", ""));
                return;
            }

            // Validação das datas
            if (editalParaCadastro.getDataabertura() == null || editalParaCadastro.getDatafechamento() == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "As datas de início e término são obrigatórias", ""));
                return;
            }

            if (editalParaCadastro.getDataabertura().after(editalParaCadastro.getDatafechamento())) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "A data de início não pode ser posterior à data de término", ""));
                return;
            }

            // Atualiza o status do edital com base na data atual
            Date hoje = Date.from(Instant.now());
            if (hoje.after(editalParaCadastro.getDatafechamento())) {
                editalParaCadastro.setStatus("Inativo");
            } else {
                editalParaCadastro.setStatus("Ativo");
            }

            // Verifica se o arquivo foi selecionado
            if (arquivoPDF == null || arquivoPDF.getFileName().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "É necessário selecionar um arquivo PDF", ""));
                return;
            }

            // Verifica se o arquivo é um PDF
            if (!arquivoPDF.getFileName().toLowerCase().endsWith(".pdf")) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "O arquivo deve ser um PDF", ""));
                return;
            }

            // Armazena o conteúdo do PDF no objeto Edital
            editalParaCadastro.setDocpdfedital(arquivoPDF.getContent());

            if (editar) {
                editalDAO.edit(editalParaCadastro);
                editar = false;
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Edital atualizado com sucesso", ""));
            } else {
                boolean idEncontrado = verificarId(editalParaCadastro.getIdedital());

                if (idEncontrado) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Id de edital já existente", ""));
                    return;
                }
                editalDAO.create(editalParaCadastro);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Edital criado com sucesso", ""));
            }

            preencherTabela();
            editalParaCadastro = new Edital();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar Edital: " + e.getMessage(), ""));
        }
    }

    public String prepararCadastroOuEdicao(boolean isEdicao) throws IOException {
        if (isEdicao) {
            if (editalSelecionado == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum Edital selecionado para edição", ""));
                return null;
            }
            editalParaCadastro = editalSelecionado;
            editar = true;
        } else {
            editalParaCadastro = new Edital();
            editar = false;
        }
        return "editalCadastro.xhtml?faces-redirect=true";
    }

    public void excluirEdital() {
        try {
            if (editalSelecionado == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum Edital selecionado para exclusão", ""));
                return;
            }

            editalDAO.remove(editalSelecionado.getIdedital().toString());
            preencherTabela();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Edital excluído com sucesso", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir Edital: " + e.getMessage(), ""));
        }
    }

    public void novoEdital() throws IOException {
        editalParaCadastro = new Edital();
        editar = false;
    }

    public boolean verificarId(String id) {
        try {
            editalParaChecar = editalDAO.find(id);
            if (editalParaChecar != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        // Não tem id igual
        return false;
    }
}

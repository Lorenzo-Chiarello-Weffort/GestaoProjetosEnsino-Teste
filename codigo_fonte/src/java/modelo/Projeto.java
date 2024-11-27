/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Paulo
 */
@Entity
@Table(name = "projeto")
@NamedQueries({
    @NamedQuery(name = "Projeto.findAll", query = "SELECT p FROM Projeto p"),
    @NamedQuery(name = "Projeto.findByIdProjeto", query = "SELECT p FROM Projeto p WHERE p.idProjeto = :idProjeto"),
    @NamedQuery(name = "Projeto.findByTitulo", query = "SELECT p FROM Projeto p WHERE p.titulo = :titulo"),
    @NamedQuery(name = "Projeto.findByResumo", query = "SELECT p FROM Projeto p WHERE p.resumo = :resumo"),
    @NamedQuery(name = "Projeto.findByDuracaoSemestre", query = "SELECT p FROM Projeto p WHERE p.duracaoSemestre = :duracaoSemestre"),
    @NamedQuery(name = "Projeto.findByNumBolsista", query = "SELECT p FROM Projeto p WHERE p.numBolsista = :numBolsista"),
    @NamedQuery(name = "Projeto.findByCargaSemanal", query = "SELECT p FROM Projeto p WHERE p.cargaSemanal = :cargaSemanal"),
    @NamedQuery(name = "Projeto.findByPerfilBolsista", query = "SELECT p FROM Projeto p WHERE p.perfilBolsista = :perfilBolsista"),
    @NamedQuery(name = "Projeto.findByDisciplinaRequerida", query = "SELECT p FROM Projeto p WHERE p.disciplinaRequerida = :disciplinaRequerida"),
    @NamedQuery(name = "Projeto.findByAtividadesPrevistas", query = "SELECT p FROM Projeto p WHERE p.atividadesPrevistas = :atividadesPrevistas"),
    @NamedQuery(name = "Projeto.findByDataInicio", query = "SELECT p FROM Projeto p WHERE p.dataInicio = :dataInicio"),
    @NamedQuery(name = "Projeto.findByDataTermino", query = "SELECT p FROM Projeto p WHERE p.dataTermino = :dataTermino"),
    @NamedQuery(name = "Projeto.findByStatus", query = "SELECT p FROM Projeto p WHERE p.status = :status")})
public class Projeto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProjeto")
    private Integer idProjeto;
    @Size(max = 255)
    @Column(name = "titulo")
    private String titulo;
    @Size(max = 1000)
    @Column(name = "resumo")
    private String resumo;
    @Column(name = "duracao_semestre")
    private Integer duracaoSemestre;
    @Column(name = "num_bolsista")
    private Integer numBolsista;
    @Size(max = 1000)
    @Column(name = "observacoes")
    private String observacoes;
    @Column(name = "carga_semanal")
    private Float cargaSemanal;
    @Size(max = 1000)
    @Column(name = "perfil_bolsista")
    private String perfilBolsista;
    @Size(max = 1000)
    @Column(name = "atividades_previstas")
    private String atividadesPrevistas;
    @Column(name = "data_inicio")
    @Temporal(TemporalType.DATE)
    private Date dataInicio;
    @Column(name = "data_termino")
    @Temporal(TemporalType.DATE)
    private Date dataTermino;
    @Size(max = 10)
    @Column(name = "status")
    private String status;
    @Column(name = "remuneracao")
    private Float remuneracao;
    @Lob
    @Column(name = "arquivopdf")
    private byte[] arquivopdf;
    @Lob
    @Column(name = "relatoriofinalpdf")
    private byte[] relatoriofinalpdf;
    
    @JoinColumn(name = "prontuario", referencedColumnName = "prontuario")
    @ManyToOne(optional = false)
    private Professor prontuario;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iddisciplina")
    private List<Disciplina> disciplinaRequerida;
    
    @JoinColumn(name = "idedital", referencedColumnName = "idedital")
    @ManyToOne(optional = false)
    private Edital idedital;

    public Projeto() {
    }

    public Projeto(Integer idProjeto) {
        this.idProjeto = idProjeto;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    

    public Integer getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(Integer idProjeto) {
        this.idProjeto = idProjeto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public List<Disciplina> getDisciplinaRequerida() {
        return disciplinaRequerida;
    }

    public void setDisciplinaRequerida(List<Disciplina> disciplinaRequerida) {
        this.disciplinaRequerida = disciplinaRequerida;
    }

    

    public Integer getDuracaoSemestre() {
        return duracaoSemestre;
    }

    public void setDuracaoSemestre(Integer duracaoSemestre) {
        this.duracaoSemestre = duracaoSemestre;
    }

    public Integer getNumBolsista() {
        return numBolsista;
    }

    public void setNumBolsista(Integer numBolsista) {
        this.numBolsista = numBolsista;
    }

    public Float getCargaSemanal() {
        return cargaSemanal;
    }

    public void setCargaSemanal(Float cargaSemanal) {
        this.cargaSemanal = cargaSemanal;
    }

    public String getPerfilBolsista() {
        return perfilBolsista;
    }

    public void setPerfilBolsista(String perfilBolsista) {
        this.perfilBolsista = perfilBolsista;
    }

    public String getAtividadesPrevistas() {
        return atividadesPrevistas;
    }

    public void setAtividadesPrevistas(String atividadesPrevistas) {
        this.atividadesPrevistas = atividadesPrevistas;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getRemuneracao() {
        return remuneracao;
    }

    public void setRemuneracao(Float remuneracao) {
        this.remuneracao = remuneracao;
    }

    public byte[] getArquivopdf() {
        return arquivopdf;
    }

    public void setArquivopdf(byte[] arquivopdf) {
        this.arquivopdf = arquivopdf;
    }

    public byte[] getRelatoriofinalpdf() {
        return relatoriofinalpdf;
    }

    public void setRelatoriofinalpdf(byte[] relatoriofinalpdf) {
        this.relatoriofinalpdf = relatoriofinalpdf;
    }

    public Professor getProntuario() {
        return prontuario;
    }

    public void setProntuario(Professor prontuario) {
        this.prontuario = prontuario;
    }

    public Edital getIdedital() {
        return idedital;
    }

    public void setIdedital(Edital idedital) {
        this.idedital = idedital;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProjeto != null ? idProjeto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Projeto)) {
            return false;
        }
        Projeto other = (Projeto) object;
        if ((this.idProjeto == null && other.idProjeto != null) || (this.idProjeto != null && !this.idProjeto.equals(other.idProjeto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Projeto{" + "observacoes=" + observacoes + '}';
    }

   

   

}

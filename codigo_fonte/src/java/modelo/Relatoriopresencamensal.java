/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Paulo
 */
@Entity
@Table(name = "relatoriopresencamensal")
@IdClass(RelatoriopresencamensalPK.class)
@NamedQueries({
    @NamedQuery(name = "Relatoriopresencamensal.findAll", query = "SELECT r FROM Relatoriopresencamensal r"),
    @NamedQuery(name = "Relatoriopresencamensal.findByIdrelatorio", query = "SELECT r FROM Relatoriopresencamensal r WHERE r.relatoriopresencamensalPK.idrelatorio = :idrelatorio"),
    @NamedQuery(name = "Relatoriopresencamensal.findByData", query = "SELECT r FROM Relatoriopresencamensal r WHERE r.data = :data"),
    @NamedQuery(name = "Relatoriopresencamensal.findByDtassinaaluno", query = "SELECT r FROM Relatoriopresencamensal r WHERE r.dtassinaaluno = :dtassinaaluno"),
    @NamedQuery(name = "Relatoriopresencamensal.findByDtassinaprofessor", query = "SELECT r FROM Relatoriopresencamensal r WHERE r.dtassinaprofessor = :dtassinaprofessor"),
    @NamedQuery(name = "Relatoriopresencamensal.findByProjetoAlunoidProjeto", query = "SELECT r FROM Relatoriopresencamensal r WHERE r.relatoriopresencamensalPK.projetoAlunoidProjeto = :projetoAlunoidProjeto"),
    @NamedQuery(name = "Relatoriopresencamensal.findByProjetoAlunoprontuario", query = "SELECT r FROM Relatoriopresencamensal r WHERE r.relatoriopresencamensalPK.projetoAlunoprontuario = :projetoAlunoprontuario")})
public class Relatoriopresencamensal implements Serializable {

    @Id
    @JoinColumns({
        @JoinColumn(name = "projetoAlunoidProjeto", referencedColumnName = "idProjeto", insertable = false, updatable = false),
        @JoinColumn(name = "projetoAlunoprontuario", referencedColumnName = "prontuario", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Projetoaluno projetoaluno;

    @Id
    @Column(name = "idrelatorio")
    private Integer idrelatorio;

    @Basic(optional = false)
    @NotNull
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @Lob
    @Column(name = "arquivopresencapdf")
    private byte[] arquivopresencapdf;
    @Column(name = "dtassinaaluno")
    @Temporal(TemporalType.DATE)
    private Date dtassinaaluno;
    @Column(name = "dtassinaprofessor")
    @Temporal(TemporalType.DATE)
    private Date dtassinaprofessor;

    public Relatoriopresencamensal() {
    }

    public Relatoriopresencamensal(Projetoaluno projetoaluno, Integer idrelatorio, Date data, byte[] arquivopresencapdf, Date dtassinaaluno, Date dtassinaprofessor) {
        this.projetoaluno = projetoaluno;
        this.idrelatorio = idrelatorio;
        this.data = data;
        this.arquivopresencapdf = arquivopresencapdf;
        this.dtassinaaluno = dtassinaaluno;
        this.dtassinaprofessor = dtassinaprofessor;
    }

    public Integer getIdrelatorio() {
        return idrelatorio;
    }

    public void setIdrelatorio(Integer idrelatorio) {
        this.idrelatorio = idrelatorio;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public byte[] getArquivopresencapdf() {
        return arquivopresencapdf;
    }

    public void setArquivopresencapdf(byte[] arquivopresencapdf) {
        this.arquivopresencapdf = arquivopresencapdf;
    }

    public Date getDtassinaaluno() {
        return dtassinaaluno;
    }

    public void setDtassinaaluno(Date dtassinaaluno) {
        this.dtassinaaluno = dtassinaaluno;
    }

    public Date getDtassinaprofessor() {
        return dtassinaprofessor;
    }

    public void setDtassinaprofessor(Date dtassinaprofessor) {
        this.dtassinaprofessor = dtassinaprofessor;
    }

    public Projetoaluno getProjetoaluno() {
        return projetoaluno;
    }

    public void setProjetoaluno(Projetoaluno projetoaluno) {
        this.projetoaluno = projetoaluno;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.projetoaluno);
        hash = 47 * hash + Objects.hashCode(this.idrelatorio);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Relatoriopresencamensal other = (Relatoriopresencamensal) obj;
        if (!Objects.equals(this.projetoaluno, other.projetoaluno)) {
            return false;
        }
        return Objects.equals(this.idrelatorio, other.idrelatorio);
    }

}

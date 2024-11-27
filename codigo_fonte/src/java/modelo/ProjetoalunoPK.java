/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import jakarta.persistence.Column;
import java.io.Serializable;

/**
 *
 * @author Paulo
 */

public class ProjetoalunoPK implements Serializable {


    @Column(name = "idProjeto")
    private int idProjeto;
    @Column(name = "prontuario")
    private String prontuario;

    public ProjetoalunoPK() {
    }

    public ProjetoalunoPK(int idProjeto, String prontuario) {
        this.idProjeto = idProjeto;
        this.prontuario = prontuario;
    }

    public int getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(int idProjeto) {
        this.idProjeto = idProjeto;
    }

    public String getProntuario() {
        return prontuario;
    }

    public void setProntuario(String prontuario) {
        this.prontuario = prontuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idProjeto;
        hash += (prontuario != null ? prontuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjetoalunoPK)) {
            return false;
        }
        ProjetoalunoPK other = (ProjetoalunoPK) object;
        if (this.idProjeto != other.idProjeto) {
            return false;
        }
        if ((this.prontuario == null && other.prontuario != null) || (this.prontuario != null && !this.prontuario.equals(other.prontuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.ProjetoalunoPK[ idProjeto=" + idProjeto + ", prontuario=" + prontuario + " ]";
    }
    
}

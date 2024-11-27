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
public class PresencaPK implements Serializable {

 
    @Column(name = "prontuario")
    private String prontuario;
    @Column(name = "idProjeto")
    private int idProjeto;
    @Column(name = "data")
    private int data;

    public PresencaPK() {
    }

    public PresencaPK(String prontuario, int idProjeto, int data) {
        this.prontuario = prontuario;
        this.idProjeto = idProjeto;
        this.data = data;
    }

    public String getProntuario() {
        return prontuario;
    }

    public void setProntuario(String prontuario) {
        this.prontuario = prontuario;
    }

    public int getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(int idProjeto) {
        this.idProjeto = idProjeto;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prontuario != null ? prontuario.hashCode() : 0);
        hash += (int) idProjeto;
        hash += (int) data;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PresencaPK)) {
            return false;
        }
        PresencaPK other = (PresencaPK) object;
        if ((this.prontuario == null && other.prontuario != null) || (this.prontuario != null && !this.prontuario.equals(other.prontuario))) {
            return false;
        }
        if (this.idProjeto != other.idProjeto) {
            return false;
        }
        if (this.data != other.data) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.PresencaPK[ prontuario=" + prontuario + ", idProjeto=" + idProjeto + ", data=" + data + " ]";
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

/**
 *
 * @author Paulo
 */
public class DisciplinaProjetoPK implements Serializable{
    
    private int iddisciplina;
 
    private int idProjeto;

    public int getIddisciplina() {
        return iddisciplina;
    }

    public void setIddisciplina(int iddisciplina) {
        this.iddisciplina = iddisciplina;
    }

    public int getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(int idProjeto) {
        this.idProjeto = idProjeto;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + this.iddisciplina;
        hash = 43 * hash + this.idProjeto;
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
        final DisciplinaProjetoPK other = (DisciplinaProjetoPK) obj;
        if (this.iddisciplina != other.iddisciplina) {
            return false;
        }
        return this.idProjeto == other.idProjeto;
    }
    
    
    
}

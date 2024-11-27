package dao;

import jakarta.ejb.Stateless;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Disciplina;
import modelo.Projeto;
import modelo.DisciplinaProjeto;

@Stateless
public class DisciplinaProjetoDAO {

    // Método para criar um novo DisciplinaProjeto
    public void create(DisciplinaProjeto disciplinaProjeto) {
        if (disciplinaProjeto == null) {
            throw new IllegalArgumentException("O objeto disciplinasProjeto está nulo.");
        }
        
        System.out.println(disciplinaProjeto.getIddisciplina().getIddisciplina());
        System.out.println(disciplinaProjeto.getIdProjeto().getIdProjeto());
        System.out.println(disciplinaProjeto.getNome());

        // Inserção na tabela 'disciplina_projeto'
        String insertQuery = "INSERT INTO disciplinasProjeto (iddisciplina, idProjeto, nome) VALUES (?, ?, ?)";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(insertQuery)) {

            pst.setInt(1, disciplinaProjeto.getIddisciplina().getIddisciplina()); 
            pst.setInt(2, disciplinaProjeto.getIdProjeto().getIdProjeto()); 
            pst.setString(3, disciplinaProjeto.getNome());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("disciplinasProjeto cadastrado com sucesso!");
            } else {
                System.err.println("Falha ao cadastrar o disciplinasProjeto.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar disciplinasProjeto: " + e.getMessage());
        }
    }

    // Método para editar um DisciplinaProjeto existente
    public void edit(DisciplinaProjeto disciplinaProjeto) {
        if (disciplinaProjeto == null) {
            throw new IllegalArgumentException("O objeto disciplinasProjeto está nulo.");
        }

        // Atualização da tabela 'disciplina_projeto'
        String updateQuery = "UPDATE disciplinasProjeto SET iddisciplina = ?, idProjeto = ?, nome = ? WHERE iddisciplina = ? and idProjeto = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(updateQuery)) {

            pst.setInt(1, disciplinaProjeto.getIddisciplina().getIddisciplina());
            pst.setInt(2, disciplinaProjeto.getIdProjeto().getIdProjeto());
            pst.setString(3, disciplinaProjeto.getNome());

            pst.setInt(4, disciplinaProjeto.getIddisciplina().getIddisciplina()); // Condição para o ID da Disciplina
            pst.setInt(5, disciplinaProjeto.getIdProjeto().getIdProjeto()); // Condição para o ID do Projeto

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("disciplinasProjeto atualizado com sucesso!");
            } else {
                System.err.println("Falha ao atualizar o disciplinasProjeto.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar disciplinasProjeto: " + e.getMessage());
        }
    }

    // Método para remover um DisciplinaProjeto
    public void remove(Disciplina disciplina, Projeto projeto) {
        if (disciplina == null || projeto == null) {
            throw new IllegalArgumentException("Disciplina ou Projeto estão nulos.");
        }

        // Deletar um relacionamento entre Disciplina e Projeto
        String deleteQuery = "DELETE FROM disciplinasProjeto WHERE iddisciplina = ? and idProjeto = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(deleteQuery)) {

            pst.setInt(1, disciplina.getIddisciplina()); // ID da Disciplina
            pst.setInt(2, projeto.getIdProjeto()); // ID do Projeto

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("disciplinasProjeto removido com sucesso!");
            } else {
                System.err.println("Falha ao remover o disciplinasProjeto.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover disciplinasProjeto: " + e.getMessage());
        }
    }

    // Método para buscar um DisciplinaProjeto pelo ID de Disciplina e Projeto
    public DisciplinaProjeto find(Disciplina disciplina, Projeto projeto) {
        if (disciplina == null || projeto == null) {
            throw new IllegalArgumentException("Disciplina ou Projeto estão nulos.");
        }

        // Selecionar um relacionamento específico entre Disciplina e Projeto
        String selectQuery = "SELECT * FROM disciplinasProjeto WHERE iddisciplina = ? and idProjeto = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {

            pst.setInt(1, disciplina.getIddisciplina()); // ID da Disciplina
            pst.setInt(2, projeto.getIdProjeto()); // ID do Projeto

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    DisciplinaProjeto disciplinaProjeto = new DisciplinaProjeto();

                    Disciplina disciplinaResult = new Disciplina();
                    disciplinaResult.setIddisciplina(rs.getInt("iddisciplina"));
                    disciplinaProjeto.setIddisciplina(disciplinaResult);

                    Projeto projetoResult = new Projeto();
                    projetoResult.setIdProjeto(rs.getInt("idProjeto"));
                    disciplinaProjeto.setIdProjeto(projetoResult);

                    disciplinaProjeto.setNome(rs.getString("nome"));
                    return disciplinaProjeto;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar disciplinasProjeto: " + e.getMessage());
        }

        return null;
    }

    // Método para buscar todos os DisciplinaProjeto relacionados a um Projeto
    public List<DisciplinaProjeto> findByProjetoId(Integer idProjeto) {
        if (idProjeto == null) {
            throw new IllegalArgumentException("O ID do projeto está nulo.");
        }

        String selectQuery = "SELECT * FROM disciplinasProjeto WHERE idProjeto = ?";
        List<DisciplinaProjeto> listaDisciplinaProjeto = new ArrayList<>();
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {

            pst.setInt(1, idProjeto); // ID do Projeto

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    DisciplinaProjeto disciplinaProjeto = new DisciplinaProjeto();

                    Disciplina disciplina = new Disciplina();
                    disciplina.setIddisciplina(rs.getInt("iddisciplina"));
                    disciplinaProjeto.setIddisciplina(disciplina);

                    Projeto projeto = new Projeto();
                    projeto.setIdProjeto(rs.getInt("idProjeto"));
                    disciplinaProjeto.setIdProjeto(projeto);

                    disciplinaProjeto.setNome(rs.getString("nome"));
                    listaDisciplinaProjeto.add(disciplinaProjeto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar disciplinasProjeto por ID de projeto: " + e.getMessage());
        }

        return listaDisciplinaProjeto;
    }

    // Método para buscar todos os DisciplinaProjeto
    public List<DisciplinaProjeto> findAll() {
        String selectAllQuery = "SELECT * FROM disciplinasProjeto";
        List<DisciplinaProjeto> listaDisciplinaProjeto = new ArrayList<>();

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectAllQuery); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                DisciplinaProjeto disciplinaProjeto = new DisciplinaProjeto();

                Disciplina disciplina = new Disciplina();
                disciplina.setIddisciplina(rs.getInt("iddisciplina"));
                disciplinaProjeto.setIddisciplina(disciplina);

                Projeto projeto = new Projeto();
                projeto.setIdProjeto(rs.getInt("idProjeto"));
                disciplinaProjeto.setIdProjeto(projeto);

                disciplinaProjeto.setNome(rs.getString("nome"));
                listaDisciplinaProjeto.add(disciplinaProjeto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as DisciplinaProjetos: " + e.getMessage());
        }

        return listaDisciplinaProjeto;
    }
}

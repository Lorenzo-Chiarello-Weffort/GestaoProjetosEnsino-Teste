package dao;

import jakarta.ejb.Stateless;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Curso;
import modelo.Disciplina;
import modelo.Projeto;

@Stateless
public class DisciplinaDAO {

    public void create(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("O objeto disciplina está nulo.");
        }

        String insertQuery = "INSERT INTO disciplina (nome, idCurso) VALUES (?, ?)";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, disciplina.getNome());
            pst.setInt(2, disciplina.getIdCurso().getIdCurso());
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        disciplina.setIddisciplina(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Disciplina cadastrada com sucesso!");
            } else {
                System.err.println("Falha ao cadastrar a disciplina.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar disciplina: " + e.getMessage());
        }
    }

    public void edit(Disciplina disciplina) {
        if (disciplina == null || disciplina.getIddisciplina() == null) {
            throw new IllegalArgumentException("O objeto disciplina está nulo ou sem ID.");
        }

        String updateQuery = "UPDATE disciplina SET nome = ?, idCurso = ? WHERE iddisciplina = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(updateQuery)) {

            pst.setString(1, disciplina.getNome());
            pst.setInt(2, disciplina.getIdCurso().getIdCurso());
            pst.setInt(3, disciplina.getIddisciplina());
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Disciplina atualizada com sucesso!");
            } else {
                System.err.println("Falha ao atualizar a disciplina.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar disciplina: " + e.getMessage());
        }
    }

    public void remove(Integer iddisciplina) {
        if (iddisciplina == null) {
            throw new IllegalArgumentException("O ID da disciplina está nulo.");
        }

        String deleteQuery = "DELETE FROM disciplina WHERE iddisciplina = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(deleteQuery)) {

            pst.setInt(1, iddisciplina);
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Disciplina removida com sucesso!");
            } else {
                System.err.println("Falha ao remover a disciplina.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover disciplina: " + e.getMessage());
        }
    }

    public void removeByCursoId(Integer idCurso) throws SQLException {
        if (idCurso == null) {
            throw new IllegalArgumentException("O ID do curso está nulo.");
        }

        String deleteQuery = "DELETE FROM disciplina WHERE idCurso = ?";
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = ConexaoMysql.obterConexao();
            pst = con.prepareStatement(deleteQuery);
            pst.setInt(1, idCurso);

            pst.executeUpdate();
        } finally {
            if (pst != null) try {
                pst.close();
            } catch (SQLException ignore) {
            }
            if (con != null) try {
                con.close();
            } catch (SQLException ignore) {
            }
        }
    }

    public Disciplina find(Integer iddisciplina) {
        if (iddisciplina == null) {
            throw new IllegalArgumentException("O ID da disciplina está nulo.");
        }

        String selectQuery = "SELECT * FROM disciplina WHERE iddisciplina = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {

            pst.setInt(1, iddisciplina);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Disciplina disciplina = new Disciplina();
                    disciplina.setIddisciplina(rs.getInt("iddisciplina"));
                    disciplina.setNome(rs.getString("nome"));

                    Curso curso = new Curso();
                    curso.setIdCurso(rs.getInt("idCurso"));
                    disciplina.setIdCurso(curso);

                    return disciplina;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar disciplina: " + e.getMessage());
        }

        return null;
    }

    public List<Disciplina> findByCursoId(Integer idCurso) throws SQLException {
        if (idCurso == null) {
            throw new IllegalArgumentException("O ID do curso está nulo.");
        }

        String selectQuery = "SELECT * FROM disciplina WHERE idCurso = ?";
        List<Disciplina> disciplinaList = new ArrayList<>();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = ConexaoMysql.obterConexao();
            pst = con.prepareStatement(selectQuery);
            pst.setInt(1, idCurso);
            rs = pst.executeQuery();

            while (rs.next()) {
                Disciplina disciplina = new Disciplina();
                disciplina.setIddisciplina(rs.getInt("iddisciplina"));
                disciplina.setNome(rs.getString("nome"));
                disciplina.setIdCurso(new Curso(idCurso));  // Define a referência ao curso

                disciplinaList.add(disciplina);
            }
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException ignore) {
            }
            if (pst != null) try {
                pst.close();
            } catch (SQLException ignore) {
            }
            if (con != null) try {
                con.close();
            } catch (SQLException ignore) {
            }
        }

        return disciplinaList;
    }

    public List<Disciplina> findAll() {
        String selectAllQuery = "SELECT d.iddisciplina, d.nome AS nomeDisciplina, "
                + "c.idCurso, c.nome_curso "
                + "FROM disciplina d "
                + "INNER JOIN curso c ON d.idCurso = c.idCurso";

        List<Disciplina> disciplinaList = new ArrayList<>();

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectAllQuery); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Disciplina disciplina = new Disciplina();
                disciplina.setIddisciplina(rs.getInt("iddisciplina"));
                disciplina.setNome(rs.getString("nomeDisciplina"));

                Curso curso = new Curso();
                curso.setIdCurso(rs.getInt("idCurso"));
                curso.setNomeCurso(rs.getString("nome_curso"));
                disciplina.setIdCurso(curso);

                disciplinaList.add(disciplina);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as disciplinas: " + e.getMessage());
        }

        return disciplinaList;
    }

    public Disciplina findByNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da disciplina está vazio ou nulo.");
        }

        String selectQuery = "SELECT * FROM disciplina WHERE nome = ?";
        Disciplina disciplina = null;

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {

            pst.setString(1, nome);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    disciplina = new Disciplina();
                    disciplina.setIddisciplina(rs.getInt("iddisciplina"));
                    disciplina.setNome(rs.getString("nome"));

                    Curso curso = new Curso();
                    curso.setIdCurso(rs.getInt("idCurso"));
                    disciplina.setIdCurso(curso);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar disciplina pelo nome: " + e.getMessage());
        }

        return disciplina;
    }

    public List<Disciplina> findByProjeto(int idProjeto) throws SQLException {
        String sql = "SELECT * FROM disciplina WHERE idProjeto = ?";
        List<Disciplina> disciplinas = new ArrayList<>();

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(sql)) {

            // Define o parâmetro para o idProjeto
            pst.setInt(1, idProjeto);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    // Instancia uma nova disciplina
                    Disciplina disciplina = new Disciplina();
                    disciplina.setIddisciplina(rs.getInt("iddisciplina"));
                    disciplina.setNome(rs.getString("nome"));

                    // Relacionamento com Curso
                    CursoDAO cursoDAO = new CursoDAO();
                    Curso curso = cursoDAO.find(rs.getInt("idCurso"));
                    disciplina.setIdCurso(curso);

                    // Relacionamento com Projeto
                    ProjetoDAO projetoDAO = new ProjetoDAO();
                    Projeto projeto = projetoDAO.find(rs.getInt("idProjeto"));
                    disciplina.setIdProjeto(projeto);

                    // Adiciona à lista de disciplinas
                    disciplinas.add(disciplina);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar disciplinas por projeto: " + e.getMessage());
            throw e;
        }

        return disciplinas;
    }

}

package dao;

import modelo.Curso;
import modelo.Disciplina;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {

    private DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

    public void create(Curso curso) throws SQLException {
        if (curso == null) {
            throw new IllegalArgumentException("O objeto curso está nulo.");
        }

        String insertQuery = "INSERT INTO curso (nome_curso) VALUES (?)";
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet generatedKeys = null;

        try {
            con = ConexaoMysql.obterConexao();
            con.setAutoCommit(false); // Inicia a transação

            pst = con.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, curso.getNomeCurso());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Falha ao cadastrar o curso: Nenhuma linha foi afetada.");
            }

            // Obtém o ID gerado para o curso
            generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idCurso = generatedKeys.getInt(1);
                curso.setIdCurso(idCurso);
            } else {
                throw new SQLException("Falha ao cadastrar o curso: ID não foi gerado.");
            }

            con.commit(); // Confirma a transação
            System.out.println("Curso cadastrado com sucesso!");
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback(); // Reverte a transação em caso de erro
                } catch (SQLException ex) {
                    System.err.println("Erro ao reverter a transação: " + ex.getMessage());
                }
            }
            System.err.println("Erro ao cadastrar o curso: " + e.getMessage());
            throw e; // Re-throw para que o controller possa lidar com
        } finally {
            if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException ignore) {}
            if (pst != null) try { pst.close(); } catch (SQLException ignore) {}
            if (con != null) try { con.setAutoCommit(true); con.close(); } catch (SQLException ignore) {}
        }
    }

    public void edit(Curso curso) throws SQLException {
        if (curso == null || curso.getIdCurso() == null) {
            throw new IllegalArgumentException("O objeto curso está nulo ou sem ID.");
        }

        String updateQuery = "UPDATE curso SET nome_curso = ? WHERE idCurso = ?";
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = ConexaoMysql.obterConexao();
            con.setAutoCommit(false); // Inicia a transação

            // Atualiza o curso
            pst = con.prepareStatement(updateQuery);
            pst.setString(1, curso.getNomeCurso());
            pst.setInt(2, curso.getIdCurso());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Falha ao atualizar o curso: Nenhuma linha foi afetada.");
            }

            con.commit(); // Confirma a transação
            System.out.println("Curso atualizado com sucesso!");
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback(); // Reverte a transação em caso de erro
                } catch (SQLException ex) {
                    System.err.println("Erro ao reverter a transação: " + ex.getMessage());
                }
            }
            System.err.println("Erro ao editar o curso: " + e.getMessage());
            throw e; // Re-throw para que o controller possa lidar com
        } finally {
            if (pst != null) try { pst.close(); } catch (SQLException ignore) {}
            if (con != null) try { con.setAutoCommit(true); con.close(); } catch (SQLException ignore) {}
        }
    }

    public void remove(Integer idCurso) throws SQLException {
        if (idCurso == null) {
            throw new IllegalArgumentException("O ID do curso está nulo.");
        }

        String deleteQuery = "DELETE FROM curso WHERE idCurso = ?";
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = ConexaoMysql.obterConexao();
            con.setAutoCommit(false); // Inicia a transação

            // Remover disciplinas associadas
            disciplinaDAO.removeByCursoId(idCurso);

            // Remover o curso
            pst = con.prepareStatement(deleteQuery);
            pst.setInt(1, idCurso);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Falha ao remover o curso: Nenhuma linha foi afetada.");
            }

            con.commit(); // Confirma a transação
            System.out.println("Curso excluído com sucesso!");
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback(); // Reverte a transação em caso de erro
                } catch (SQLException ex) {
                    System.err.println("Erro ao reverter a transação: " + ex.getMessage());
                }
            }
            System.err.println("Erro ao excluir o curso: " + e.getMessage());
            throw e; // Re-throw para que o controller possa lidar com
        } finally {
            if (pst != null) try { pst.close(); } catch (SQLException ignore) {}
            if (con != null) try { con.setAutoCommit(true); con.close(); } catch (SQLException ignore) {}
        }
    }

public Curso find(int idCurso) throws SQLException {
    String sql = "SELECT * FROM curso WHERE idCurso = ?";
    try (Connection con = ConexaoMysql.obterConexao();
         PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, idCurso);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                Curso curso = new Curso();
                curso.setIdCurso(rs.getInt("idCurso"));
                curso.setNomeCurso(rs.getString("nome_curso"));
                // Adicione outros campos conforme necessário
                return curso;
            }
        }
    }
    return null;
}

    public Curso findByName(String nome_curso) throws SQLException {
        if (nome_curso == null || nome_curso.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do curso está vazio.");
        }

        String selectQuery = "SELECT * FROM curso WHERE nome_curso = ?";
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = ConexaoMysql.obterConexao();
            pst = con.prepareStatement(selectQuery);
            pst.setString(1, nome_curso);
            rs = pst.executeQuery();
            if (rs.next()) {
                Curso curso = new Curso();
                curso.setIdCurso(rs.getInt("idCurso"));
                curso.setNomeCurso(rs.getString("nome_curso"));
                return curso;
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
            if (pst != null) try { pst.close(); } catch (SQLException ignore) {}
            if (con != null) try { con.close(); } catch (SQLException ignore) {}
        }
        return null;
    }

    public List<Curso> findAll() throws SQLException {
        String selectAllQuery = "SELECT * FROM curso";
        List<Curso> cursoList = new ArrayList<>();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = ConexaoMysql.obterConexao();
            pst = con.prepareStatement(selectAllQuery);
            rs = pst.executeQuery();
            while (rs.next()) {
                Curso curso = new Curso();
                curso.setIdCurso(rs.getInt("idCurso"));
                curso.setNomeCurso(rs.getString("nome_curso"));
                cursoList.add(curso);  // Correção: Adicionando o curso à lista
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
            if (pst != null) try { pst.close(); } catch (SQLException ignore) {}
            if (con != null) try { con.close(); } catch (SQLException ignore) {}
        }

        return cursoList;
    }
}

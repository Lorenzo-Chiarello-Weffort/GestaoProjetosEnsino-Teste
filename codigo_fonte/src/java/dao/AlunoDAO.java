    package dao;

import jakarta.ejb.Stateless;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Aluno;
import modelo.Projeto;
import modelo.Projetoaluno;

@Stateless
public class AlunoDAO {

    public void create(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("O objeto Aluno está nulo.");
        }

        String insertQuery = "INSERT INTO aluno (prontuario, email, nome, senha, CPF, rg, orgao_emissor, semestre, celular, "
                + "banco, agencia, conta_corrente, turno, idCurso, historico) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(insertQuery)) {
            pst.setString(1, aluno.getProntuario());
            pst.setString(2, aluno.getEmail());
            pst.setString(3, aluno.getNome());
            pst.setString(4, aluno.getSenha());
            pst.setString(5, aluno.getCpf());
            pst.setString(6, aluno.getRg());
            pst.setString(7, aluno.getOrgaoEmissor());
            pst.setInt(8, aluno.getSemestre());
            pst.setString(9, aluno.getCelular());
            pst.setString(10, aluno.getBanco());
            pst.setString(11, aluno.getAgencia());
            pst.setString(12, aluno.getContaCorrente());
            pst.setString(13, aluno.getTurno());
            pst.setInt(14, aluno.getIdCurso().getIdCurso());
            pst.setBytes(15, aluno.getHistorico()); // Supondo que o histórico é um arquivo em bytes

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Aluno cadastrado com sucesso!");
            } else {
                System.err.println("Falha ao cadastrar o Aluno: Nenhuma linha foi afetada durante a inserção");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar o Aluno: " + e.getMessage());
        }
    }

    public void updateHistorico(String prontuario, byte[] historico) {
        if (prontuario == null || historico == null) {
            throw new IllegalArgumentException("O prontuário ou o histórico está nulo.");
        }

        String updateQuery = "UPDATE aluno SET historico = ? WHERE prontuario = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(updateQuery)) {
            pst.setBytes(1, historico);
            pst.setString(2, prontuario);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Histórico atualizado com sucesso!");
            } else {
                System.err.println("Falha ao atualizar o histórico: Nenhuma linha foi afetada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar o histórico: " + e.getMessage());
        }
    }

    public void edit(Aluno aluno) throws SQLException {
        String updateQuery = "UPDATE aluno SET email = ?, nome = ?, senha = ?, CPF = ?, rg = ?, orgao_emissor = ?, semestre = ?, celular = ?, "
                + "banco = ?, agencia = ?, conta_corrente = ?, turno = ?, idCurso = ? WHERE prontuario = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(updateQuery)) {
            pst.setString(1, aluno.getEmail());
            pst.setString(2, aluno.getNome());
            pst.setString(3, aluno.getSenha());
            pst.setString(4, aluno.getCpf());
            pst.setString(5, aluno.getRg());
            pst.setString(6, aluno.getOrgaoEmissor());
            pst.setInt(7, aluno.getSemestre());
            pst.setString(8, aluno.getCelular());
            pst.setString(9, aluno.getBanco());
            pst.setString(10, aluno.getAgencia());
            pst.setString(11, aluno.getContaCorrente());
            pst.setString(12, aluno.getTurno());
            pst.setInt(13, aluno.getIdCurso().getIdCurso());
            pst.setString(14, aluno.getProntuario());

            pst.executeUpdate();
        }
    }

    public void remove(String prontuario) throws SQLException {
        String deleteQuery = "DELETE FROM aluno WHERE prontuario = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(deleteQuery)) {
            pst.setString(1, prontuario);
            pst.executeUpdate();
        }
    }

    public Aluno find(String prontuario) throws SQLException {
        String selectQuery = "SELECT * FROM aluno WHERE prontuario = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {
            pst.setString(1, prontuario);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAluno(rs);
                }
            }
        }
        return null;
    }

    public List<Aluno> findAll() throws SQLException {
        String selectAllQuery = "SELECT * FROM aluno";
        List<Aluno> alunoList = new ArrayList<>();
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectAllQuery); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                alunoList.add(mapResultSetToAluno(rs));
            }
        }
        return alunoList;
    }

    public List<Aluno> findByEmail(String email) throws SQLException {
        return findByField("email", email);
    }

    public Aluno findOneByEmail(String email) throws SQLException {
        List<Aluno> alunos = findByField("email", email);
        return alunos.isEmpty() ? null : alunos.get(0);
    }

    public Aluno findByCpf(String cpf) throws SQLException {
        return findByField("CPF", cpf).stream().findFirst().orElse(null);
    }

    public Aluno findByRg(String rg) throws SQLException {
        return findByField("rg", rg).stream().findFirst().orElse(null);
    }

    private List<Aluno> findByField(String field, String value) throws SQLException {
        String selectByFieldQuery = "SELECT * FROM aluno WHERE " + field + " = ?";
        List<Aluno> alunoList = new ArrayList<>();
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectByFieldQuery)) {
            pst.setString(1, value);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    alunoList.add(mapResultSetToAluno(rs));
                }
            }
        }
        return alunoList;
    }

    private Aluno mapResultSetToAluno(ResultSet rs) throws SQLException {
        Aluno aluno = new Aluno();
        aluno.setProntuario(rs.getString("prontuario"));
        aluno.setEmail(rs.getString("email"));
        aluno.setNome(rs.getString("nome"));
        aluno.setSenha(rs.getString("senha"));
        aluno.setCpf(rs.getString("cpf"));
        aluno.setRg(rs.getString("rg"));
        aluno.setOrgaoEmissor(rs.getString("orgao_emissor"));
        aluno.setSemestre(rs.getInt("semestre"));
        aluno.setCelular(rs.getString("celular"));
        aluno.setBanco(rs.getString("banco"));
        aluno.setAgencia(rs.getString("agencia"));
        aluno.setContaCorrente(rs.getString("conta_corrente"));
        aluno.setTurno(rs.getString("turno"));
        aluno.setHistorico(rs.getBytes("historico"));
      
        return aluno;
    }

    public void inscrever(Projetoaluno projetoAluno) {
        if (projetoAluno == null) {
            throw new IllegalArgumentException("O objeto Projetoaluno está nulo.");
        }
        String insertQuery = "INSERT INTO projetoaluno (idProjeto, prontuario, remunerado, valorbolsa) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(insertQuery)) {
            pst.setInt(1, projetoAluno.getIdProjeto().getIdProjeto());
            pst.setString(2, projetoAluno.getProntuario().getProntuario());
            pst.setString(3, projetoAluno.getRemunerado());
            pst.setString(4, projetoAluno.getValorbolsa());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Aluno inscrito no projeto com sucesso!");
            } else {
                System.err.println("Falha ao inscrever o aluno no projeto.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inscrever o aluno no projeto: " + e.getMessage());
        }
    }

    public boolean verificarInscricao(Aluno aluno, Projeto projeto) {
        String query = "SELECT COUNT(*) FROM projetoaluno WHERE idProjeto = ? AND prontuario = ?";

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, projeto.getIdProjeto());
            pst.setString(2, aluno.getProntuario());

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar inscrição: " + e.getMessage());
        }

        return false;
    }

    public boolean verificarSalaHora(Aluno aluno) {
        if (aluno == null || aluno.getProntuario() == null || aluno.getProntuario().isEmpty()) {
            throw new IllegalArgumentException("O aluno ou seu prontuário não pode ser nulo ou vazio.");
        }

        // Consulta para verificar se existem registros onde salaHora não é nulo para o aluno
        String query = "SELECT COUNT(*) AS total "
                + "FROM projetoaluno "
                + "WHERE prontuario = ? AND salaHora IS NOT NULL";

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(query)) {

            // Define o prontuário no preparedStatement
            pst.setString(1, aluno.getProntuario());

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("total");
                    return count > 0;  // Retorna true se houver projetos com salaHora diferente de NULL
                }
            }
        } catch (SQLException e) {
            // Log de erro em caso de falha na execução da consulta
            System.err.println("Erro ao verificar salaHora para o aluno: " + aluno.getProntuario());
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        }

        return false;  // Retorna false caso nenhum projeto tenha salaHora diferente de NULL
    }

}

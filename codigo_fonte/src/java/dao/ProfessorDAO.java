package dao;

import jakarta.ejb.Stateless;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Professor;

@Stateless
public class ProfessorDAO {

    public void create(Professor professor) {
        if (professor == null) {
            throw new IllegalArgumentException("O objeto Professor está nulo.");
        }

        String insertQuery = "INSERT INTO professor (prontuario, email, nome, senha, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(insertQuery)) {

            pst.setString(1, professor.getProntuario());
            pst.setString(2, professor.getEmail());
            pst.setString(3, professor.getNome());
            pst.setString(4, professor.getSenha());
            pst.setString(5, professor.getStatus());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Professor cadastrado com sucesso!");
            } else {
                System.err.println("Falha ao cadastrar o Professor: Nenhuma linha foi afetada durante a inserção");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar o Professor: " + e.getMessage());
        }
    }

    public void edit(Professor professor) throws SQLException {
        String updateQuery = "UPDATE professor SET email = ?, nome = ?, senha = ?, status = ? WHERE prontuario = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(updateQuery)) {
            pst.setString(1, professor.getEmail());
            pst.setString(2, professor.getNome());
            pst.setString(3, professor.getSenha());
            pst.setString(4, professor.getStatus());
            pst.setString(5, professor.getProntuario());
            pst.executeUpdate();
        }
    }

    public void remove(String prontuario) throws SQLException {
        String deleteQuery = "DELETE FROM professor WHERE prontuario = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(deleteQuery)) {
            pst.setString(1, prontuario);
            pst.executeUpdate();
        }
    }

    public Professor find(String prontuario) throws SQLException {
        String selectQuery = "SELECT * FROM professor WHERE prontuario = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {
            pst.setString(1, prontuario);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Professor professor = new Professor();
                    professor.setProntuario(rs.getString("prontuario"));
                    professor.setEmail(rs.getString("email"));
                    professor.setNome(rs.getString("nome"));
                    professor.setSenha(rs.getString("senha"));
                    professor.setStatus(rs.getString("status"));
                    return professor;
                }
            }
        }
        return null;
    }

    public List<Professor> findAll() throws SQLException {
        String selectAllQuery = "SELECT * FROM professor";
        List<Professor> professorList = new ArrayList<>();
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectAllQuery); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Professor professor = new Professor();
                professor.setProntuario(rs.getString("prontuario"));
                professor.setEmail(rs.getString("email"));
                professor.setNome(rs.getString("nome"));
                professor.setSenha(rs.getString("senha"));
                professor.setStatus(rs.getString("status"));
                professorList.add(professor);
            }
        }
        return professorList;
    }
    
    public List<Professor> findPending() throws SQLException {
        String selectAllQuery = "SELECT * FROM professor WHERE status = 0";
        List<Professor> professorList = new ArrayList<>();
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectAllQuery); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Professor professor = new Professor();
                professor.setProntuario(rs.getString("prontuario"));
                professor.setEmail(rs.getString("email"));
                professor.setNome(rs.getString("nome"));
                professor.setSenha(rs.getString("senha"));
                professor.setStatus(rs.getString("status"));
                professorList.add(professor);
            }
        }
        return professorList;
    }

    public List<Professor> findByEmail(String email) throws SQLException {
        String selectByEmailQuery = "SELECT * FROM professor WHERE email = ?";
        List<Professor> professorList = new ArrayList<>();
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectByEmailQuery)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Professor professor = new Professor();
                    professor.setProntuario(rs.getString("prontuario"));
                    professor.setEmail(rs.getString("email"));
                    professor.setNome(rs.getString("nome"));
                    professor.setSenha(rs.getString("senha"));
                    professor.setStatus(rs.getString("status"));
                    professorList.add(professor);
                }
            }
        }
        return professorList;
    }
    
    public Professor findOneByEmail(String email) throws SQLException {
        String selectByEmailQuery = "SELECT * FROM professor WHERE email = ?";
        try (Connection con = ConexaoMysql.obterConexao();
             PreparedStatement pst = con.prepareStatement(selectByEmailQuery)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Professor professor = new Professor();
                    professor.setProntuario(rs.getString("prontuario"));
                    professor.setEmail(rs.getString("email"));
                    professor.setNome(rs.getString("nome"));
                    professor.setSenha(rs.getString("senha"));
                    professor.setStatus(rs.getString("status"));
                    return professor;
                }
            }
        }
        return null;
    }

}

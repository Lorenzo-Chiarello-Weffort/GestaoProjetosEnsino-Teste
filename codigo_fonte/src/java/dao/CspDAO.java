package dao;

import jakarta.ejb.Stateless;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Csp;

@Stateless
public class CspDAO {

public void create(Csp csp) {
    if (csp == null) {
        throw new IllegalArgumentException("O objeto Csp está nulo.");
    }

    String insertQuery = "INSERT INTO csp (prontuario, email, nome, senha) VALUES (?, ?, ?, ?)";
    try (Connection con = ConexaoMysql.obterConexao();
         PreparedStatement pst = con.prepareStatement(insertQuery)) {

        pst.setString(1, csp.getProntuario());
        pst.setString(2, csp.getEmail());
        pst.setString(3, csp.getNome());
        pst.setString(4, csp.getSenha());

        int rowsAffected = pst.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("CSP cadastrado com sucesso!");
        } else {
            System.err.println("Falha ao cadastrar o CSP: Nenhuma linha foi afetada durante a inserção");
        }
    } catch (SQLException e) {
        System.err.println("Erro ao cadastrar o CSP: " + e.getMessage());
    }
}




    public void edit(Csp csp) throws SQLException {
        String updateQuery = "UPDATE csp SET email = ?, nome = ?, senha = ? WHERE prontuario = ?";
        try (Connection con = ConexaoMysql.obterConexao();
             PreparedStatement pst = con.prepareStatement(updateQuery)) {
            pst.setString(1, csp.getEmail());
            pst.setString(2, csp.getNome());
            pst.setString(3, csp.getSenha());
            pst.setString(4, csp.getProntuario());
            pst.executeUpdate();
        }
    }

    public void remove(String prontuario) throws SQLException {
        String deleteQuery = "DELETE FROM csp WHERE prontuario = ?";
        try (Connection con = ConexaoMysql.obterConexao();
             PreparedStatement pst = con.prepareStatement(deleteQuery)) {
            pst.setString(1, prontuario);
            pst.executeUpdate();
        }
    }

    public Csp find(String prontuario) throws SQLException {
        String selectQuery = "SELECT * FROM csp WHERE prontuario = ?";
        try (Connection con = ConexaoMysql.obterConexao(); 
             PreparedStatement pst = con.prepareStatement(selectQuery)) {
            pst.setString(1, prontuario);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Csp csp = new Csp();
                    csp.setProntuario(rs.getString("prontuario"));
                    csp.setEmail(rs.getString("email"));
                    csp.setNome(rs.getString("nome"));
                    csp.setSenha(rs.getString("senha"));
                    return csp;
                }
            }
        }
        return null;
    }

    public List<Csp> findAll() throws SQLException {
        String selectAllQuery = "SELECT * FROM csp";
        List<Csp> cspList = new ArrayList<>();
        try (Connection con = ConexaoMysql.obterConexao();
             PreparedStatement pst = con.prepareStatement(selectAllQuery);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Csp csp = new Csp();
                csp.setProntuario(rs.getString("prontuario"));
                csp.setEmail(rs.getString("email"));
                csp.setNome(rs.getString("nome"));
                csp.setSenha(rs.getString("senha"));
                cspList.add(csp);
            }
        }
        return cspList;
    }

    public List<Csp> findByEmail(String email) throws SQLException {
        String selectByEmailQuery = "SELECT * FROM csp WHERE email = ?";
        List<Csp> cspList = new ArrayList<>();
        try (Connection con = ConexaoMysql.obterConexao();
             PreparedStatement pst = con.prepareStatement(selectByEmailQuery)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Csp csp = new Csp();
                    csp.setProntuario(rs.getString("prontuario"));
                    csp.setEmail(rs.getString("email"));
                    csp.setNome(rs.getString("nome"));
                    csp.setSenha(rs.getString("senha"));
                    cspList.add(csp);
                }
            }
        }
        return cspList;
    }
    
    public Csp findOneByEmail(String email) throws SQLException {
        String selectByEmailQuery = "SELECT * FROM csp WHERE email = ?";
        try (Connection con = ConexaoMysql.obterConexao();
             PreparedStatement pst = con.prepareStatement(selectByEmailQuery)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Csp csp = new Csp();
                    csp.setProntuario(rs.getString("prontuario"));
                    csp.setEmail(rs.getString("email"));
                    csp.setNome(rs.getString("nome"));
                    csp.setSenha(rs.getString("senha"));
                    return csp;
                }
            }
        }
        return null;
    }

  
}


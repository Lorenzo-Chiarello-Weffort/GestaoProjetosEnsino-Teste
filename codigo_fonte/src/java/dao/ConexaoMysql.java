package dao;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoMysql {
    private static DataSource dataSource;
    
    static {
        try {
            InitialContext context = new InitialContext();
            dataSource = (DataSource) context.lookup("jdbc/__sgpe"); // Nome do recurso JNDI configurado no Payara
        } catch (NamingException e) {
            e.printStackTrace();
            System.err.println("Erro ao configurar o pool de conexões: " + e.getMessage());
        }
    }

    public static Connection obterConexao() throws SQLException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao obter conexão: " + e.getMessage());
            throw e;
        }
    }

    public static void executarComandoSQL(String comandoSQL) {
        Connection conexao = null;
        Statement statement = null;
        try {
            conexao = obterConexao();
            conexao.setAutoCommit(false);
            statement = conexao.createStatement();
            statement.executeUpdate("use projeto_ensino");
            statement.executeUpdate(comandoSQL);
            conexao.commit();
            System.out.println("Comando SQL executado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao executar comando SQL: " + e.getMessage());
            if (conexao != null) {
                try {
                    conexao.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erro ao fazer rollback: " + ex.getMessage());
                }
            }
        }
    }

   
}



import database.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Tentando conectar ao Supabase...");

        try (Connection conn = Conexao.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("SUCESSO! Conexão estabelecida com o banco.");

                // Executa uma consulta simples para confirmar que o banco responde
                try (PreparedStatement stmt = conn.prepareStatement("SELECT current_database(), version()");
                     ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Banco: " + rs.getString(1));
                        System.out.println("Versão: " + rs.getString(2));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("ERRO ao conectar ao Supabase:");
            System.err.println("Mensagem: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
        }
    }
}
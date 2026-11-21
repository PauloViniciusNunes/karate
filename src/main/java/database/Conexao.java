package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    // Substitua pelo host exato exibido na aba "JDBC -> Session pooler" do Supabase
    private static final String URL = "jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:5432/postgres";
    private static final String USER = "postgres.qdukgcrakqpdaxsufwqp";
    private static final String PASSWORD = "R^v5dQdkXQu$V6wtwPJyS7";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
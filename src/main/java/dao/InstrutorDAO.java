package dao;
import database.Conexao;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import model.Instrutor;
import model.enums.CargoInstrutor;
import model.enums.Faixa;

public class InstrutorDAO {

    public static void criarInstrutor(Instrutor i) throws SQLException {
        String sql = "insert into instrutor (id, cargo, faixa) values(?, ?, ?)";

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, i.getId());
            if(i.getCargo() != null) {
                stmt.setString(2, i.getCargo().name());
            } else {
                stmt.setNull(2, Types.VARCHAR);
            }
            if(i.getFaixa() != null) {
                stmt.setString(3, i.getFaixa().name());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }

            stmt.executeUpdate();
        }
    }

    public static List<Instrutor> listarInstrutores() throws SQLException {
        String sql = "select i.id, i.cargo, i.faixa, "
                   + "u.nome, u.cpf, u.login, u.senha, u.email, u.telefone, u.ativo "
                   + "from instrutor i join usuario u on i.id = u.id order by u.nome";
        List<Instrutor> instrutores = new ArrayList<>();

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while(rs.next()) {
                instrutores.add(montarInstrutor(rs));
            }
        }

        return instrutores;
    }

    public static Instrutor encontrarInstrutor(int id) throws SQLException {
        String sql = "select i.id, i.cargo, i.faixa, "
                   + "u.nome, u.cpf, u.login, u.senha, u.email, u.telefone, u.ativo "
                   + "from instrutor i join usuario u on i.id = u.id where i.id = ?";

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return montarInstrutor(rs);
                }
            }
        }

        return null;
    }

    public static void atualizarInstrutor(Instrutor instrutor) throws SQLException {
        String sql = "update instrutor set cargo = ?, faixa = ? where id = ?";

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            if(instrutor.getCargo() != null) {
                stmt.setString(1, instrutor.getCargo().name());
            } else {
                stmt.setNull(1, Types.VARCHAR);
            }
            if(instrutor.getFaixa() != null) {
                stmt.setString(2, instrutor.getFaixa().name());
            } else {
                stmt.setNull(2, Types.VARCHAR);
            }

            stmt.setInt(3, instrutor.getId());

            stmt.executeUpdate();
        }
    }

    @Deprecated
    public static void deletarInstrutor(int id) throws SQLException {
        String sql = "DELETE from instrutor where id = ?";

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    private static Instrutor montarInstrutor(ResultSet rs) throws SQLException {
        Instrutor instrutor = Instrutor.criarInstrutor("", "", "", "", "", "", null, null);
        instrutor.setId(rs.getInt("id"));
        instrutor.setNome(rs.getString("nome"));
        instrutor.setCpf(rs.getString("cpf"));
        instrutor.setLogin(rs.getString("login"));
        instrutor.setSenha(rs.getString("senha"));
        instrutor.setEmail(rs.getString("email"));
        instrutor.setTelefone(rs.getString("telefone"));
        instrutor.setAtivo(rs.getBoolean("ativo"));

        String cargo = rs.getString("cargo");
        if(cargo != null) {
            instrutor.setCargo(CargoInstrutor.valueOf(cargo.trim().toUpperCase()));
        }
        String faixa = rs.getString("faixa");
        if(faixa != null) {
            instrutor.setFaixa(Faixa.valueOf(faixa.trim().toUpperCase()));
        }

        return instrutor;
    }

}

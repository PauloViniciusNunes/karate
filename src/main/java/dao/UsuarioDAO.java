package dao;

import database.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Aluno;
import model.Instrutor;
import model.Usuario;
import model.enums.CargoInstrutor;
import model.enums.Faixa;

public class UsuarioDAO {

    private static final String BASE_SELECT =
            "select u.id, u.nome, u.cpf, u.login, u.senha, u.email, u.telefone, u.ativo, "
          + "a.id as id_aluno, a.id_turma, a.data_nascimento, a.faixa_atual, "
          + "i.id as id_instrutor, i.cargo, i.faixa "
          + "from usuario u "
          + "left join aluno a on u.id = a.id "
          + "left join instrutor i on u.id = i.id ";

    public static void criarUsuario(Usuario u) throws SQLException {
        try (Connection conn = Conexao.getConnection()) {
            criarUsuario(conn, u);
        }
    }

    public static void criarUsuario(Connection conn, Usuario u) throws SQLException {
        String sql = "insert into usuario (nome, cpf, login, senha, email, telefone, ativo) values(?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getCpf());
            stmt.setString(3, u.getLogin());
            stmt.setString(4, u.getSenha());
            stmt.setString(5, u.getEmail());
            stmt.setString(6, u.getTelefone());
            stmt.setBoolean(7, u.isAtivo());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setId(rs.getInt(1));
                }
            }
        }
    }

    public static List<Usuario> listarUsuarios() throws SQLException {
        String sql = BASE_SELECT + "order by u.nome";
        List<Usuario> usuarios = new ArrayList<>();

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                usuarios.add(montarUsuario(rs));
            }
        }

        return usuarios;
    }

    public static Usuario encontrarUsuario(int id) throws SQLException {
        String sql = BASE_SELECT + "where u.id = ?";

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarUsuario(rs);
                }
            }
        }

        return null;
    }

    public static Usuario encontrarUsuarioPorLogin(String login) throws SQLException {
        String sql = BASE_SELECT + "where u.login = ?";

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarUsuario(rs);
                }
            }
        }

        return null;
    }

    public static void atualizarUsuario(Usuario usuario) throws SQLException {
        try (Connection conn = Conexao.getConnection()) {
            atualizarUsuario(conn, usuario);
        }
    }

    public static void atualizarUsuario(Connection conn, Usuario usuario) throws SQLException {
        String sql = "update usuario set nome = ?, cpf = ?, login = ?, senha = ?, email = ?, telefone = ?, ativo = ? where id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getLogin());
            stmt.setString(4, usuario.getSenha());
            stmt.setString(5, usuario.getEmail());
            stmt.setString(6, usuario.getTelefone());
            stmt.setBoolean(7, usuario.isAtivo());
            stmt.setInt(8, usuario.getId());

            stmt.executeUpdate();
        }
    }

    @Deprecated
    public static void deletarUsuario(int id) throws SQLException {
        String sql = "DELETE from usuario where id = ?";

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private static Usuario montarUsuario(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String cpf = rs.getString("cpf");
        String login = rs.getString("login");
        String senha = rs.getString("senha");
        String email = rs.getString("email");
        String telefone = rs.getString("telefone");
        boolean ativo = rs.getBoolean("ativo");

        if (rs.getObject("id_aluno") != null) {
            int idTurmaRaw = rs.getInt("id_turma");
            int idTurma = rs.wasNull() ? -1 : idTurmaRaw;
            java.util.Date dataNascimento = rs.getDate("data_nascimento");
            String faixaAtualStr = rs.getString("faixa_atual");
            Faixa faixaAtual = faixaAtualStr != null ? Faixa.valueOf(faixaAtualStr.trim().toUpperCase()) : Faixa.BRANCA;
            return new Aluno(id, nome, cpf, login, senha, email, telefone, ativo, dataNascimento, idTurma, faixaAtual);
        }

        if (rs.getObject("id_instrutor") != null) {
            String cargoStr = rs.getString("cargo");
            CargoInstrutor cargo = cargoStr != null ? CargoInstrutor.valueOf(cargoStr.trim().toUpperCase()) : null;
            String faixaStr = rs.getString("faixa");
            Faixa faixa = faixaStr != null ? Faixa.valueOf(faixaStr.trim().toUpperCase()) : null;
            return new Instrutor(id, nome, cpf, login, senha, email, telefone, ativo, cargo, faixa);
        }

        return new Usuario(id, nome, cpf, login, senha, email, telefone, ativo) {};
    }
}
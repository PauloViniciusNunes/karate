package dao;
import database.Conexao;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.Aluno;
import model.Instrutor;
import model.Usuario;
import model.enums.CargoInstrutor;
import model.enums.Faixa;

public class UsuarioDAO {

    public static void criarUsuario(Usuario u) throws SQLException {
        String sql = "insert into usuario (nome, cpf, login, senha, email, telefone, ativo) values(?, ?, ?, ?, ?, ?, ?)";

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getCpf());
            stmt.setString(3, u.getLogin());
            stmt.setString(4, u.getSenha());
            stmt.setString(5, u.getEmail());
            stmt.setString(6, u.getTelefone());
            stmt.setBoolean(7, u.isAtivo());

            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    u.setId(rs.getInt(1));
                }
            }
        }
    }

    public static List<Usuario> listarUsuarios() throws SQLException {
        String sql = "select u.id, u.nome, u.cpf, u.login, u.senha, u.email, u.telefone, u.ativo, "
                   + "a.id as id_aluno, a.id_turma, a.data_nascimento, a.faixa_atual, "
                   + "i.id as id_instrutor, i.cargo, i.faixa "
                   + "from usuario u "
                   + "left join aluno a on u.id = a.id "
                   + "left join instrutor i on u.id = i.id "
                   + "order by u.nome";
        List<Usuario> usuarios = new ArrayList<>();

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while(rs.next()) {
                usuarios.add(montarUsuario(rs));
            }
        }

        return usuarios;
    }

    public static Usuario encontrarUsuario(int id) throws SQLException {
        String sql = "select u.id, u.nome, u.cpf, u.login, u.senha, u.email, u.telefone, u.ativo, "
                   + "a.id as id_aluno, a.id_turma, a.data_nascimento, a.faixa_atual, "
                   + "i.id as id_instrutor, i.cargo, i.faixa "
                   + "from usuario u "
                   + "left join aluno a on u.id = a.id "
                   + "left join instrutor i on u.id = i.id "
                   + "where u.id = ?";

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return montarUsuario(rs);
                }
            }
        }

        return null;
    }

    public static void atualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "update usuario set nome = ?, cpf = ?, login = ?, senha = ?, email = ?, telefone = ?, ativo = ? where id = ?";

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
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

    public static void deletarUsuario(int id) throws SQLException {
        String sql = "DELETE from usuario where id = ?";

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    private static Usuario montarUsuario(ResultSet rs) throws SQLException {
        Usuario usuario;

        if(rs.getObject("id_aluno") != null) {
            Aluno aluno = Aluno.criarAluno("", "", "", "", "", "", new Date(), -1);
            int idTurma = rs.getInt("id_turma");
            aluno.setIdTurma(rs.wasNull() ? -1 : idTurma);
            aluno.setDataNascimento(rs.getDate("data_nascimento"));
            String faixaAtual = rs.getString("faixa_atual");
            if(faixaAtual != null) {
                aluno.setFaixaAtual(Faixa.valueOf(faixaAtual.trim().toUpperCase()));
            }
            usuario = aluno;
        } else if(rs.getObject("id_instrutor") != null) {
            Instrutor instrutor = Instrutor.criarInstrutor("", "", "", "", "", "", null, null);
            String cargo = rs.getString("cargo");
            if(cargo != null) {
                instrutor.setCargo(CargoInstrutor.valueOf(cargo.trim().toUpperCase()));
            }
            String faixa = rs.getString("faixa");
            if(faixa != null) {
                instrutor.setFaixa(Faixa.valueOf(faixa.trim().toUpperCase()));
            }
            usuario = instrutor;
        } else {
            usuario = new Usuario("", "", "", "", "", "") {};
        }

        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setCpf(rs.getString("cpf"));
        usuario.setLogin(rs.getString("login"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setEmail(rs.getString("email"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setAtivo(rs.getBoolean("ativo"));

        return usuario;
    }

}

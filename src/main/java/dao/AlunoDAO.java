package dao;

import database.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Aluno;
import model.enums.Faixa;

public class AlunoDAO {

    public static void criarAluno(Aluno a) throws SQLException {
        String sql = "insert into aluno (id, id_turma, data_nascimento, faixa_atual) values(?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false);
            try {
                if (a.getId() <= 0) {
                    UsuarioDAO.criarUsuario(conn, a);
                }

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, a.getId());
                    if (a.getIdTurma() > 0) {
                        stmt.setInt(2, a.getIdTurma());
                    } else {
                        stmt.setNull(2, Types.INTEGER);
                    }
                    if (a.getDataNascimento() != null) {
                        stmt.setDate(3, new java.sql.Date(a.getDataNascimento().getTime()));
                    } else {
                        stmt.setNull(3, Types.DATE);
                    }
                    if (a.getFaixaAtual() != null) {
                        stmt.setString(4, a.getFaixaAtual().name());
                    } else {
                        stmt.setNull(4, Types.VARCHAR);
                    }

                    stmt.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public static List<Aluno> listarAlunos() throws SQLException {
        String sql = "select a.id, a.id_turma, a.data_nascimento, a.faixa_atual, "
                   + "u.nome, u.cpf, u.login, u.senha, u.email, u.telefone, u.ativo "
                   + "from aluno a join usuario u on a.id = u.id order by u.nome";
        List<Aluno> alunos = new ArrayList<>();

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                alunos.add(montarAluno(rs));
            }
        }

        return alunos;
    }

    public static Aluno encontrarAluno(int id) throws SQLException {
        String sql = "select a.id, a.id_turma, a.data_nascimento, a.faixa_atual, "
                   + "u.nome, u.cpf, u.login, u.senha, u.email, u.telefone, u.ativo "
                   + "from aluno a join usuario u on a.id = u.id where a.id = ?";

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarAluno(rs);
                }
            }
        }

        return null;
    }

    public static void atualizarAluno(Aluno aluno) throws SQLException {
        String sql = "update aluno set id_turma = ?, data_nascimento = ?, faixa_atual = ? where id = ?";

        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false);
            try {
                UsuarioDAO.atualizarUsuario(conn, aluno);

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    if (aluno.getIdTurma() > 0) {
                        stmt.setInt(1, aluno.getIdTurma());
                    } else {
                        stmt.setNull(1, Types.INTEGER);
                    }
                    if (aluno.getDataNascimento() != null) {
                        stmt.setDate(2, new java.sql.Date(aluno.getDataNascimento().getTime()));
                    } else {
                        stmt.setNull(2, Types.DATE);
                    }
                    if (aluno.getFaixaAtual() != null) {
                        stmt.setString(3, aluno.getFaixaAtual().name());
                    } else {
                        stmt.setNull(3, Types.VARCHAR);
                    }

                    stmt.setInt(4, aluno.getId());
                    stmt.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public static void deletarAluno(int id) throws SQLException {
        String sql = "DELETE from aluno where id = ?";

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private static Aluno montarAluno(ResultSet rs) throws SQLException {
        int idTurmaRaw = rs.getInt("id_turma");
        int idTurma = rs.wasNull() ? -1 : idTurmaRaw;

        Date dataNascimento = rs.getDate("data_nascimento");
        String faixaStr = rs.getString("faixa_atual");
        Faixa faixa = faixaStr != null ? Faixa.valueOf(faixaStr.trim().toUpperCase()) : Faixa.BRANCA;

        return new Aluno(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("cpf"),
            rs.getString("login"),
            rs.getString("senha"),
            rs.getString("email"),
            rs.getString("telefone"),
            rs.getBoolean("ativo"),
            dataNascimento,
            idTurma,
            faixa
        );
    }
}

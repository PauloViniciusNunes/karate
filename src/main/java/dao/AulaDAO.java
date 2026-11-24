package dao;

import database.Conexao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Aula;
import model.enums.StatusAula;
import model.enums.TipoAula;

public class AulaDAO {

    public static void criarAula(Aula a) throws SQLException {
        String sql = "insert into aula (id_turma, data_aula, tipo, status) values(?, ?, ?, ?)";

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setInt(1, a.getIdTurma());
            if (a.getData() != null) {
                stmt.setDate(2, Date.valueOf(a.getData()));
            } else {
                stmt.setNull(2, Types.DATE);
            }
            if (a.getTipo() != null) {
                stmt.setString(3, a.getTipo().name());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }
            if (a.getStatus() != null) {
                stmt.setString(4, a.getStatus().name());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    a.setId(rs.getInt(1));
                }
            }
        }
    }

    public static List<Aula> listarAulas() throws SQLException {
        String sql = "select id, id_turma, data_aula, tipo, status from aula order by data_aula, id";
        List<Aula> aulas = new ArrayList<>();

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                aulas.add(montarAula(conn, rs));
            }
        }

        return aulas;
    }

    public static Aula encontrarAula(int id) throws SQLException {
        String sql = "select id, id_turma, data_aula, tipo, status from aula where id = ?";

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarAula(conn, rs);
                }
            }
        }

        return null;
    }

    public static void atualizarAula(Aula aula) throws SQLException {
        String sql = "update aula set id_turma = ?, data_aula = ?, tipo = ?, status = ? where id = ?";

        try (
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, aula.getIdTurma());
            if (aula.getData() != null) {
                stmt.setDate(2, Date.valueOf(aula.getData()));
            } else {
                stmt.setNull(2, Types.DATE);
            }
            if (aula.getTipo() != null) {
                stmt.setString(3, aula.getTipo().name());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }
            if (aula.getStatus() != null) {
                stmt.setString(4, aula.getStatus().name());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }

            stmt.setInt(5, aula.getId());
            stmt.executeUpdate();
        }
    }

    public static void deletarAula(int id) throws SQLException {
        String sqlPresencas = "delete from aula_presenca where id_aula = ?";
        String sql = "DELETE from aula where id = ?";

        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false);
            try (
                PreparedStatement stmtPresencas = conn.prepareStatement(sqlPresencas);
                PreparedStatement stmt = conn.prepareStatement(sql)
            ) {
                stmtPresencas.setInt(1, id);
                stmtPresencas.executeUpdate();

                stmt.setInt(1, id);
                stmt.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public static void salvarPresenca(int idAula, String cpfAluno, boolean presente) throws SQLException {
        String sqlUpdate = "update aula_presenca set presente = ? where id_aula = ? and cpf_aluno = ?";
        String sqlInsert = "insert into aula_presenca (id_aula, cpf_aluno, presente) values (?, ?, ?)";

        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false);
            try (
                PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
                PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)
            ) {
                stmtUpdate.setBoolean(1, presente);
                stmtUpdate.setInt(2, idAula);
                stmtUpdate.setString(3, cpfAluno);

                if (stmtUpdate.executeUpdate() == 0) {
                    stmtInsert.setInt(1, idAula);
                    stmtInsert.setString(2, cpfAluno);
                    stmtInsert.setBoolean(3, presente);
                    stmtInsert.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private static Aula montarAula(Connection conn, ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int idTurma = rs.getInt("id_turma");

        Date dataAulaSql = rs.getDate("data_aula");
        LocalDate dataAula = dataAulaSql != null ? dataAulaSql.toLocalDate() : null;

        String tipoStr = rs.getString("tipo");
        TipoAula tipo = tipoStr != null ? TipoAula.valueOf(tipoStr.trim().toUpperCase()) : null;

        String statusStr = rs.getString("status");
        StatusAula status = statusStr != null ? StatusAula.valueOf(statusStr.trim().toUpperCase()) : null;

        Aula aula = new Aula(id, idTurma, dataAula, tipo, status, new ArrayList<>(), new ArrayList<>());
        carregarPresencas(conn, aula);

        return aula;
    }

    private static void carregarPresencas(Connection conn, Aula aula) throws SQLException {
        String sql = "select cpf_aluno, presente from aula_presenca where id_aula = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, aula.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String cpf = rs.getString("cpf_aluno");
                    if (rs.getBoolean("presente")) {
                        aula.getCpfsPresentes().add(cpf);
                    } else {
                        aula.getCpfsFaltosos().add(cpf);
                    }
                }
            }
        }
    }
}

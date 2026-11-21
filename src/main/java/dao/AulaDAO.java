package dao;
import database.Conexao;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import model.Aula;
import model.enums.StatusAula;
import model.enums.TipoAula;

public class AulaDAO {

    public static void criarAula(Aula a) throws SQLException {
        String sql = "insert into aula (id_turma, data_aula, tipo, status) values(?, ?, ?, ?)";

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setInt(1, a.getIdTurma());
            if(a.getData() != null) {
                stmt.setDate(2, Date.valueOf(a.getData()));
            } else {
                stmt.setNull(2, Types.DATE);
            }
            if(a.getTipo() != null) {
                stmt.setString(3, a.getTipo().name());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }
            if(a.getStatus() != null) {
                stmt.setString(4, a.getStatus().name());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }

            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    a.setId(rs.getInt(1));
                }
            }
        }
    }

    public static List<Aula> listarAulas() throws SQLException {
        String sql = "select id, id_turma, data_aula, tipo, status from aula order by data_aula, id";
        List<Aula> aulas = new ArrayList<>();

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while(rs.next()) {
                aulas.add(montarAula(rs));
            }
        }

        return aulas;
    }

    public static Aula encontrarAula(int id) throws SQLException {
        String sql = "select id, id_turma, data_aula, tipo, status from aula where id = ?";

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return montarAula(rs);
                }
            }
        }

        return null;
    }

    public static void atualizarAula(Aula aula) throws SQLException {
        String sql = "update aula set id_turma = ?, data_aula = ?, tipo = ?, status = ? where id = ?";

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, aula.getIdTurma());
            if(aula.getData() != null) {
                stmt.setDate(2, Date.valueOf(aula.getData()));
            } else {
                stmt.setNull(2, Types.DATE);
            }
            if(aula.getTipo() != null) {
                stmt.setString(3, aula.getTipo().name());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }
            if(aula.getStatus() != null) {
                stmt.setString(4, aula.getStatus().name());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }

            stmt.setInt(5, aula.getId());

            stmt.executeUpdate();
        }
    }

    public static void deletarAula(int id) throws SQLException {
        String sql = "DELETE from aula where id = ?";

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    private static Aula montarAula(ResultSet rs) throws SQLException {
        Aula aula = new Aula();
        aula.setId(rs.getInt("id"));
        aula.setIdTurma(rs.getInt("id_turma"));

        Date dataAula = rs.getDate("data_aula");
        if(dataAula != null) {
            aula.setData(dataAula.toLocalDate());
        }

        String tipo = rs.getString("tipo");
        if(tipo != null) {
            aula.setTipo(TipoAula.valueOf(tipo.trim().toUpperCase()));
        }

        String status = rs.getString("status");
        if(status != null) {
            aula.setStatus(StatusAula.valueOf(status.trim().toUpperCase()));
        }

        return aula;
    }

}

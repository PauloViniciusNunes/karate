package dao;
import database.Conexao;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import model.Instrutor;
import model.Turma;
import model.enums.CargoInstrutor;
import model.enums.DiaSemana;
import model.enums.Faixa;

public class TurmaDAO {

    public static void criarTurma(Turma t) throws SQLException {
        String sql = "insert into turma (id_instrutor, nome, dias_semanas, hora_inicio, hora_fim, faixa_inicial, faixa_final, ativa) values(?, ?, ?, ?, ?, ?, ?, ?)";

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            int idInstrutor = t.getIdInstrutor() > 0
                    ? t.getIdInstrutor()
                    : (t.getResponsavel() != null ? t.getResponsavel().getId() : 0);

            stmt.setInt(1, idInstrutor);
            stmt.setString(2, t.getNome());
            setDiasSemana(stmt, 3, t.getDiasSemana());
            setLocalTime(stmt, 4, t.getHoraInicio());
            setLocalTime(stmt, 5, t.getHoraFim());
            setFaixa(stmt, 6, t.getFaixaInicial());
            setFaixa(stmt, 7, t.getFaixaFinal());
            stmt.setBoolean(8, t.getAtiva());

            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    t.setIdTurma(rs.getInt(1));
                }
            }
        }
    }

    public static List<Turma> listarTurmas() throws SQLException {
        String sql = "select t.id, t.id_instrutor, t.nome, t.dias_semanas, t.hora_inicio, t.hora_fim, "
                   + "t.faixa_inicial, t.faixa_final, t.ativa, "
                   + "i.cargo as instrutor_cargo, i.faixa as instrutor_faixa, "
                   + "u.nome as instrutor_nome, u.cpf as instrutor_cpf, u.login as instrutor_login, "
                   + "u.senha as instrutor_senha, u.email as instrutor_email, u.telefone as instrutor_telefone, u.ativo as instrutor_ativo "
                   + "from turma t "
                   + "left join instrutor i on t.id_instrutor = i.id "
                   + "left join usuario u on i.id = u.id "
                   + "order by t.nome";
        List<Turma> turmas = new ArrayList<>();

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while(rs.next()) {
                turmas.add(montarTurma(rs));
            }
        }

        return turmas;
    }

    public static Turma encontrarTurma(int id) throws SQLException {
        String sql = "select t.id, t.id_instrutor, t.nome, t.dias_semanas, t.hora_inicio, t.hora_fim, "
                   + "t.faixa_inicial, t.faixa_final, t.ativa, "
                   + "i.cargo as instrutor_cargo, i.faixa as instrutor_faixa, "
                   + "u.nome as instrutor_nome, u.cpf as instrutor_cpf, u.login as instrutor_login, "
                   + "u.senha as instrutor_senha, u.email as instrutor_email, u.telefone as instrutor_telefone, u.ativo as instrutor_ativo "
                   + "from turma t "
                   + "left join instrutor i on t.id_instrutor = i.id "
                   + "left join usuario u on i.id = u.id "
                   + "where t.id = ?";

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return montarTurma(rs);
                }
            }
        }

        return null;
    }

    public static void atualizarTurma(Turma turma) throws SQLException {
        String sql = "update turma set id_instrutor = ?, nome = ?, dias_semanas = ?, hora_inicio = ?, hora_fim = ?, faixa_inicial = ?, faixa_final = ?, ativa = ? where id = ?";

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            int idInstrutor = turma.getIdInstrutor() > 0
                    ? turma.getIdInstrutor()
                    : (turma.getResponsavel() != null ? turma.getResponsavel().getId() : 0);

            stmt.setInt(1, idInstrutor);
            stmt.setString(2, turma.getNome());
            setDiasSemana(stmt, 3, turma.getDiasSemana());
            setLocalTime(stmt, 4, turma.getHoraInicio());
            setLocalTime(stmt, 5, turma.getHoraFim());
            setFaixa(stmt, 6, turma.getFaixaInicial());
            setFaixa(stmt, 7, turma.getFaixaFinal());
            stmt.setBoolean(8, turma.getAtiva());

            stmt.setInt(9, turma.getIdTurma());

            stmt.executeUpdate();
        }
    }

    public static void deletarTurma(int id) throws SQLException {
        String sql = "DELETE from turma where id = ?";

        try(
            Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    private static void setDiasSemana(PreparedStatement stmt, int index, List<DiaSemana> diasSemana) throws SQLException {
        if(diasSemana != null && !diasSemana.isEmpty()) {
            List<String> nomes = new ArrayList<>();
            for(DiaSemana dia : diasSemana) {
                if(dia != null) {
                    nomes.add(dia.name());
                }
            }
            stmt.setString(index, String.join(",", nomes));
        } else {
            stmt.setNull(index, Types.VARCHAR);
        }
    }

    private static void setLocalTime(PreparedStatement stmt, int index, LocalTime time) throws SQLException {
        if(time != null) {
            stmt.setString(index, time.toString());
        } else {
            stmt.setNull(index, Types.VARCHAR);
        }
    }

    private static void setFaixa(PreparedStatement stmt, int index, Faixa faixa) throws SQLException {
        if(faixa != null) {
            stmt.setString(index, faixa.name());
        } else {
            stmt.setNull(index, Types.VARCHAR);
        }
    }

    private static Turma montarTurma(ResultSet rs) throws SQLException {
        Instrutor responsavel = null;
        if(rs.getString("instrutor_nome") != null) {
            responsavel = Instrutor.criarInstrutor("", "", "", "", "", "", null, null);
            responsavel.setId(rs.getInt("id_instrutor"));
            responsavel.setNome(rs.getString("instrutor_nome"));
            responsavel.setCpf(rs.getString("instrutor_cpf"));
            responsavel.setLogin(rs.getString("instrutor_login"));
            responsavel.setSenha(rs.getString("instrutor_senha"));
            responsavel.setEmail(rs.getString("instrutor_email"));
            responsavel.setTelefone(rs.getString("instrutor_telefone"));
            responsavel.setAtivo(rs.getBoolean("instrutor_ativo"));

            String cargo = rs.getString("instrutor_cargo");
            if(cargo != null) {
                responsavel.setCargo(CargoInstrutor.valueOf(cargo.trim().toUpperCase()));
            }
            String faixaInstrutor = rs.getString("instrutor_faixa");
            if(faixaInstrutor != null) {
                responsavel.setFaixa(Faixa.valueOf(faixaInstrutor.trim().toUpperCase()));
            }
        }

        List<DiaSemana> dias = new ArrayList<>();
        String diasStr = rs.getString("dias_semanas");
        if(diasStr != null && !diasStr.isBlank()) {
            String limpo = diasStr.replace("[", "").replace("]", "");
            for(String parte : limpo.split(",")) {
                if(!parte.isBlank()) {
                    dias.add(DiaSemana.valueOf(parte.trim().toUpperCase()));
                }
            }
        }

        LocalTime horaInicio = null;
        String horaInicioStr = rs.getString("hora_inicio");
        if(horaInicioStr != null && !horaInicioStr.isBlank()) {
            horaInicio = LocalTime.parse(horaInicioStr.trim());
        }

        LocalTime horaFim = null;
        String horaFimStr = rs.getString("hora_fim");
        if(horaFimStr != null && !horaFimStr.isBlank()) {
            horaFim = LocalTime.parse(horaFimStr.trim());
        }

        Faixa faixaInicial = null;
        String faixaInicialStr = rs.getString("faixa_inicial");
        if(faixaInicialStr != null) {
            faixaInicial = Faixa.valueOf(faixaInicialStr.trim().toUpperCase());
        }

        Faixa faixaFinal = null;
        String faixaFinalStr = rs.getString("faixa_final");
        if(faixaFinalStr != null) {
            faixaFinal = Faixa.valueOf(faixaFinalStr.trim().toUpperCase());
        }

        Turma turma = Turma.criarTurma(responsavel, rs.getString("nome"), dias, horaInicio, horaFim, faixaInicial, faixaFinal);
        turma.setIdTurma(rs.getInt("id"));
        turma.setIdInstrutor(rs.getInt("id_instrutor"));
        turma.setAtiva(rs.getBoolean("ativa"));

        return turma;
    }

}

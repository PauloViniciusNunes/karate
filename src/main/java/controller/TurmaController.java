package controller;

import dao.TurmaDAO;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import model.Instrutor;
import model.Turma;
import model.enums.DiaSemana;
import model.enums.Faixa;

public class TurmaController {

    public static Turma criarTurma(Instrutor responsavel, String nome, List<DiaSemana> diasSemana,
            LocalTime horaInicio, LocalTime horaFim, Faixa faixaInicial, Faixa faixaFinal) {
        Turma turma = Turma.criarTurma(responsavel, nome, diasSemana, horaInicio, horaFim, faixaInicial, faixaFinal);
        try {
            TurmaDAO.criarTurma(turma);
            return turma;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível criar a turma.", e);
        }
    }

    public static Turma obterTurma(int idTurma) {
        try {
            return TurmaDAO.encontrarTurma(idTurma);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível encontrar a turma de id: " + idTurma, e);
        }
    }

    public static List<Turma> listarTurmas() {
        try {
            return TurmaDAO.listarTurmas();
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível listar as turmas.", e);
        }
    }

    public static Turma buscarTurmaPorFaixa(Faixa faixa) {
        return Turma.buscarTurmaPorFaixa(faixa, listarTurmas());
    }

    public static List<Turma> listarTurmasPorFaixa(Faixa faixa) {
        return Turma.listarTurmasPorFaixa(faixa, listarTurmas());
    }

    public static void editarTurma(Turma turma, String nome, List<DiaSemana> diasSemana,
            LocalTime horaInicio, LocalTime horaFim, Faixa faixaInicial, Faixa faixaFinal, boolean ativa) {
        if (turma == null) {
            throw new IllegalArgumentException("Turma não pode ser nula.");
        }
        Turma.editarTurma(turma, nome, diasSemana, horaInicio, horaFim, faixaInicial, faixaFinal, ativa);
        try {
            TurmaDAO.atualizarTurma(turma);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível atualizar a turma.", e);
        }
    }

    public static void desativarTurma(Turma turma) {
        if (turma == null) {
            throw new IllegalArgumentException("Turma não pode ser nula.");
        }
        Turma.desativarTurma(turma);
        try {
            TurmaDAO.atualizarTurma(turma);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível desativar a turma.", e);
        }
    }

}
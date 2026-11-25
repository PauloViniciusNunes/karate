package controller;

import dao.TurmaDAO;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import model.Aluno;
import model.Instrutor;
import model.Turma;
import model.enums.DiaSemana;
import model.enums.Faixa;

public class TurmaController {

    static void recarregarTurmas() throws SQLException {
        List<Turma> turmas = TurmaDAO.listarTurmas();
        Turma.limparTurmasEmMemoria();
        for (Turma turma : turmas) {
            for (Aluno aluno : Aluno.listarAlunos()) {
                if (aluno.getIdTurma() == turma.getIdTurma()) {
                    aluno.alocarNaTurma(turma);
                }
            }
            Turma.registrarTurmaEmMemoria(turma);
        }
    }

    public static Turma criarTurma(Instrutor responsavel, String nome, List<DiaSemana> diasSemana,
            LocalTime horaInicio, LocalTime horaFim, Faixa faixaInicial, Faixa faixaFinal) {
        Turma turma = Turma.criarTurma(responsavel, nome, diasSemana, horaInicio, horaFim, faixaInicial, faixaFinal);
        try {
            TurmaDAO.criarTurma(turma);
            recarregarTurmas();
            Turma recarregada = obterTurma(turma.getIdTurma());
            return recarregada != null ? recarregada : turma;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível criar a turma.", e);
        }
    }

    public static Turma obterTurma(int idTurma) {
        for (Turma turma : Turma.listarTurmas()) {
            if (turma.getIdTurma() == idTurma) {
                return turma;
            }
        }
        return null;
    }

    public static List<Turma> listarTurmas() {
        return Turma.listarTurmas();
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
            recarregarTurmas();
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
            recarregarTurmas();
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível desativar a turma.", e);
        }
    }

}
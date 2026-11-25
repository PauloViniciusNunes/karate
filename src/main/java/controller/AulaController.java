package controller;

import dao.AulaDAO;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import model.Aluno;
import model.Aula;
import model.Turma;
import model.enums.TipoAula;

public class AulaController {

    public static Aula criarAula(Turma turma, LocalDate data, TipoAula tipo) {
        Aula aula = Aula.criarAula(turma, data, tipo);
        try {
            AulaDAO.criarAula(aula);
            return aula;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível criar a aula.", e);
        }
    }

    public static Aula obterAula(int id) {
        try {
            return AulaDAO.encontrarAula(id);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível encontrar a aula de id: " + id, e);
        }
    }

    public static List<Aula> listarAulas() {
        try {
            return AulaDAO.listarAulas();
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível listar as aulas.", e);
        }
    }

    public static List<Aula> listarAulasPorPeriodo(LocalDate inicio, LocalDate fim) {
        return Aula.listarAulasPorPeriodo(inicio, fim, listarAulas());
    }

    public static List<Aula> listarAulasPorTurma(Turma turma) {
        return Aula.listarAulasPorTurma(turma, listarAulas());
    }

    public static List<Aula> listarAulasPorTipo(TipoAula tipo) {
        return Aula.listarAulasPorTipo(tipo, listarAulas());
    }

    public static List<Aula> listarAulasPorAluno(Aluno aluno) {
        return Aula.listarAulasPorAluno(aluno, listarAulas());
    }

    public static List<Aula> gerarRelatorioAulas(LocalDate inicio, LocalDate fim, Turma turma, TipoAula tipo) {
        return Aula.gerarRelatorioAulas(inicio, fim, turma, tipo, listarAulas());
    }

    public static List<Aula> gerarRelatorioFrequencia(Aluno aluno, LocalDate inicio, LocalDate fim) {
        return Aula.gerarRelatorioFrequencia(aluno, inicio, fim, listarAulas());
    }

    public static void editarAula(Aula aula, LocalDate data, TipoAula tipo) {
        if (aula == null) {
            throw new IllegalArgumentException("Aula não pode ser nula.");
        }
        Aula.editarAula(aula, data, tipo);
        try {
            AulaDAO.atualizarAula(aula);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível atualizar a aula.", e);
        }
    }

    public static void cancelarAula(Aula aula) {
        if (aula == null) {
            throw new IllegalArgumentException("Aula não pode ser nula.");
        }
        aula.cancelarAula();
        try {
            AulaDAO.atualizarAula(aula);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível cancelar a aula.", e);
        }
    }

    public static void encerrarAula(Aula aula) {
        if (aula == null) {
            throw new IllegalArgumentException("Aula não pode ser nula.");
        }
        aula.encerrarAula();
        try {
            AulaDAO.atualizarAula(aula);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível encerrar a aula.", e);
        }
    }

    public static void registrarPresenca(Aula aula, Aluno aluno) {
        if (aula == null || aluno == null) {
            throw new IllegalArgumentException("Aula e aluno não podem ser nulos.");
        }
        aula.registrarPresenca(aluno);
        try {
            AulaDAO.salvarPresenca(aula.getId(), aluno.getCpf(), true);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível registrar a presença.", e);
        }
    }

    public static void registrarFalta(Aula aula, Aluno aluno) {
        if (aula == null || aluno == null) {
            throw new IllegalArgumentException("Aula e aluno não podem ser nulos.");
        }
        aula.registrarFalta(aluno);
        try {
            AulaDAO.salvarPresenca(aula.getId(), aluno.getCpf(), false);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível registrar a falta.", e);
        }
    }
}
package model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import model.enums.DiaSemana;
import model.enums.Faixa;

public class Turma {
    private String nome;
    private List<DiaSemana> diasSemana;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private Faixa faixaInicial;
    private Faixa faixaFinal;
    private boolean ativa;
    private int idTurma;
    private int idInstrutor;
    private Instrutor responsavel;

    private ArrayList<Aluno> alunos = new ArrayList<>();
    private static final List<Turma> listaTurmas = new ArrayList<>();

    public void alocarNaTurma(Aluno aluno) {
        if (aluno == null) {
            return;
        }
        if (!alunos.contains(aluno)) {
            alunos.add(aluno);
        }
        aluno.setIdTurma(this.idTurma);
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void verTurma() {
        System.out.println("Turma: " + nome + " ID da turma: " + idTurma + " Status: " + ativa);
        System.out.println("Instrutor: " + (responsavel != null ? responsavel.getNome() : "Não definido"));
        System.out.println("Horario de aula: " + horaInicio + " as " + horaFim);
        System.out.println("Dias de aula: " + diasSemana);
        System.out.println("Faixas inicial: " + faixaInicial + " e final: " + faixaFinal);
    }

    private Turma(Instrutor responsavel, String nome, List<DiaSemana> diasSemana,
    LocalTime horaInicio, LocalTime horaFim, Faixa faixaInicial, Faixa faixaFinal) {
        this.responsavel = responsavel;
        this.idInstrutor = responsavel != null ? responsavel.getId() : 0;
        this.nome = nome;
        this.diasSemana = diasSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.faixaInicial = faixaInicial;
        this.faixaFinal = faixaFinal;
        this.ativa = true;
    }

    public Turma(int idTurma, int idInstrutor, Instrutor responsavel, String nome, List<DiaSemana> diasSemana,
    LocalTime horaInicio, LocalTime horaFim, Faixa faixaInicial, Faixa faixaFinal, boolean ativa) {
        this.idTurma = idTurma;
        this.idInstrutor = idInstrutor;
        this.responsavel = responsavel;
        this.nome = nome;
        this.diasSemana = diasSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.faixaInicial = faixaInicial;
        this.faixaFinal = faixaFinal;
        this.ativa = ativa;
    }

    public static Turma criarTurma(Instrutor responsavel, String nome, List<DiaSemana> diasSemana,
    LocalTime horaInicio, LocalTime horaFim, Faixa faixaInicial, Faixa faixaFinal) {
        return new Turma(responsavel, nome, diasSemana, horaInicio, horaFim, faixaInicial, faixaFinal);
    }

    public static void editarTurma(Turma turma, String nome, List<DiaSemana> diasSemana,
    LocalTime horaInicio, LocalTime horaFim, Faixa faixaInicial, Faixa faixaFinal) {
        if (turma == null) {
            return;
        }
        turma.nome = nome;
        turma.diasSemana = diasSemana;
        turma.horaInicio = horaInicio;
        turma.horaFim = horaFim;
        turma.faixaInicial = faixaInicial;
        turma.faixaFinal = faixaFinal;
        turma.ativa = true;
    }

    public static Turma buscarTurmaPorFaixa(Faixa faixa) {
        return buscarTurmaPorFaixa(faixa, listarTurmas());
    }

    public static Turma buscarTurmaPorFaixa(Faixa faixa, List<Turma> turmas) {
        if (turmas == null) {
            return null;
        }
        for (Turma turma : turmas) {
            if (turma != null && turma.faixaInicial == faixa) {
                return turma;
            }
        }
        return null;
    }

    public static void desativarTurma(Turma turma) {
        if (turma != null) {
            turma.ativa = false;
        }
    }

    public static List<Turma> listarTurmas() {
        return new ArrayList<>(listaTurmas);
    }

    public static List<Turma> listarTurmasPorFaixa(Faixa faixa) {
        return listarTurmasPorFaixa(faixa, listarTurmas());
    }

    public static List<Turma> listarTurmasPorFaixa(Faixa faixa, List<Turma> turmas) {
        List<Turma> turmasPorFaixa = new ArrayList<>();
        if (turmas == null) {
            return turmasPorFaixa;
        }
        for (Turma turma : turmas) {
            if (turma != null && faixa == turma.faixaInicial) {
                turmasPorFaixa.add(turma);
            }
        }
        return turmasPorFaixa;
    }

    public Turma getTurma(int idTurma) {
        for (Turma turma : listaTurmas) {
            if (turma.getIdTurma() == idTurma) {
                return turma;
            }
        }
        return null;
    }

    public static void registrarTurmaEmMemoria(Turma turma) {
        if (turma != null && !listaTurmas.contains(turma)) {
            listaTurmas.add(turma);
        }
    }

    public String getNome() {
        return nome;
    }

    public List<DiaSemana> getDiasSemana() {
        return diasSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public Faixa getFaixaInicial() {
        return faixaInicial;
    }

    public Faixa getFaixaFinal() {
        return faixaFinal;
    }

    public boolean getAtiva() {
        return ativa;
    }

    public int getIdTurma() {
        return idTurma;
    }

    public int getIdInstrutor() {
        return idInstrutor;
    }

    public Instrutor getResponsavel() {
        return responsavel;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDiasSemana(List<DiaSemana> diasSemana) {
        this.diasSemana = diasSemana;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public void setFaixaInicial(Faixa faixaInicial) {
        this.faixaInicial = faixaInicial;
    }

    public void setFaixaFinal(Faixa faixaFinal) {
        this.faixaFinal = faixaFinal;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public void setIdTurma(int idTurma) {
        this.idTurma = idTurma;
    }

    public void setIdInstrutor(int idInstrutor) {
        this.idInstrutor = idInstrutor;
    }

    public void setResponsavel(Instrutor responsavel) {
        this.responsavel = responsavel;
        if (responsavel != null) {
            this.idInstrutor = responsavel.getId();
        }
    }
}

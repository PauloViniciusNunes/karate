package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.enums.StatusAula;
import model.enums.TipoAula;

public class Aula {
    private int id;
    private int idTurma;
    private LocalDate data = LocalDate.now();
    private TipoAula tipo;
    private StatusAula status;
    private ArrayList<String> cpfsPresentes = new ArrayList<>();
    private ArrayList<String> cpfsFaltosos = new ArrayList<>();

    private static ArrayList<Aula> listaDeAulas = new ArrayList<>();

    public Aula(int id, int idTurma, LocalDate data, TipoAula tipo, StatusAula status, ArrayList<String> cpfPresentes, ArrayList<String> cpfsFaltosos) {
        this.id = id;
        this.idTurma = idTurma;
        this.data = data;
        this.tipo = tipo;
        this.status = status;
        this.cpfsPresentes = (cpfPresentes == null) ? new ArrayList<>() : cpfPresentes;
        this.cpfsFaltosos = (cpfsFaltosos == null) ? new ArrayList<>() : cpfsFaltosos;
    }

    public void verAula() {
        System.out.println("Data: " + data);
        System.out.println("Tipo: " + tipo);
        System.out.println("Status: " + status);
    }

    public void registrarPresenca(Aluno aluno) {
        if (aluno == null || aluno.getCpf() == null) {
            return;
        }
        cpfsFaltosos.remove(aluno.getCpf());
        if (!cpfsPresentes.contains(aluno.getCpf())) {
            cpfsPresentes.add(aluno.getCpf());
        }
    }

    public void registrarFalta(Aluno aluno) {
        if (aluno == null || aluno.getCpf() == null) {
            return;
        }
        cpfsPresentes.remove(aluno.getCpf());
        if (!cpfsFaltosos.contains(aluno.getCpf())) {
            cpfsFaltosos.add(aluno.getCpf());
        }
    }

    public void cancelarAula() {
        this.status = StatusAula.CANCELADA;
    }

    public void encerrarAula() {
        this.status = StatusAula.REALIZADA;
    }

    public static Aula criarAula(Turma turma, LocalDate data, TipoAula tipo) {
        int novoId = listarAulas().size() + 1;
        int idDaTurma = turma != null ? turma.getIdTurma() : 0;
        LocalDate dataAula = (data != null) ? data : LocalDate.now();
        StatusAula status = StatusAula.AGENDADA;
        ArrayList<String> cpfsPresentes = new ArrayList<>();
        ArrayList<String> cpfsFaltosos = new ArrayList<>();

        return new Aula(novoId, idDaTurma, dataAula, tipo, status, cpfsPresentes, cpfsFaltosos);
    }

    public static void editarAula(Aula aula, LocalDate data, TipoAula tipo) {
        if (aula == null) {
            return;
        }
        aula.data = data;
        aula.tipo = tipo;
    }

    public static List<Aula> listarAulas() {
        return new ArrayList<>(listaDeAulas);
    }

    public static void registrarAulaEmMemoria(Aula aula) {
        if (aula != null && !listaDeAulas.contains(aula)) {
            listaDeAulas.add(aula);
        }
    }

    public static List<Aula> listarAulasPorPeriodo(LocalDate inicio, LocalDate fim) {
        return listarAulasPorPeriodo(inicio, fim, listarAulas());
    }

    public static List<Aula> listarAulasPorPeriodo(LocalDate inicio, LocalDate fim, List<Aula> aulas) {
        ArrayList<Aula> listaAulasPeriodo = new ArrayList<>();
        if (aulas == null || inicio == null || fim == null) {
            return listaAulasPeriodo;
        }
        for (Aula aula : aulas) {
            if (aula != null && aula.data != null && !aula.data.isBefore(inicio) && !aula.data.isAfter(fim)) {
                listaAulasPeriodo.add(aula);
            }
        }
        return listaAulasPeriodo;
    }

    public static List<Aula> listarAulasPorTurma(Turma turma) {
        return listarAulasPorTurma(turma, listarAulas());
    }

    public static List<Aula> listarAulasPorTurma(Turma turma, List<Aula> aulas) {
        ArrayList<Aula> listaAulasTurma = new ArrayList<>();
        if (aulas == null || turma == null) {
            return listaAulasTurma;
        }
        for (Aula aula : aulas) {
            if (aula != null && aula.idTurma == turma.getIdTurma()) {
                listaAulasTurma.add(aula);
            }
        }
        return listaAulasTurma;
    }

    public static List<Aula> listarAulasPorTipo(TipoAula tipo) {
        return listarAulasPorTipo(tipo, listarAulas());
    }

    public static List<Aula> listarAulasPorTipo(TipoAula tipo, List<Aula> aulas) {
        ArrayList<Aula> listaAulasTipo = new ArrayList<>();
        if (aulas == null) {
            return listaAulasTipo;
        }
        for (Aula aula : aulas) {
            if (aula != null && tipo == aula.tipo) {
                listaAulasTipo.add(aula);
            }
        }
        return listaAulasTipo;
    }

    public static List<Aula> listarAulasPorAluno(Aluno aluno) {
        return listarAulasPorAluno(aluno, listarAulas());
    }

    public static List<Aula> listarAulasPorAluno(Aluno aluno, List<Aula> aulas) {
        ArrayList<Aula> listaAulasAluno = new ArrayList<>();
        if (aulas == null || aluno == null || aluno.getCpf() == null) {
            return listaAulasAluno;
        }
        for (Aula aula : aulas) {
            if (aula != null && aula.cpfsPresentes.contains(aluno.getCpf())) {
                listaAulasAluno.add(aula);
            }
        }
        return listaAulasAluno;
    }

    public static List<Aula> gerarRelatorioAulas(LocalDate inicio, LocalDate fim, Turma turma, TipoAula tipo) {
        return gerarRelatorioAulas(inicio, fim, turma, tipo, listarAulas());
    }

    public static List<Aula> gerarRelatorioAulas(LocalDate inicio, LocalDate fim, Turma turma, TipoAula tipo, List<Aula> aulas) {
        List<Aula> listaAulasRelatorio = new ArrayList<>();
        if (aulas == null || inicio == null || fim == null || turma == null) {
            return listaAulasRelatorio;
        }
        for (Aula aulaRelatorio : aulas) {
            if (aulaRelatorio == null || aulaRelatorio.data == null) {
                continue;
            }
            boolean noPeriodo = !aulaRelatorio.data.isBefore(inicio) && !aulaRelatorio.data.isAfter(fim);
            boolean daTurma = aulaRelatorio.idTurma == turma.getIdTurma();
            boolean doTipo = aulaRelatorio.tipo == tipo;

            if (noPeriodo && daTurma && doTipo) {
                listaAulasRelatorio.add(aulaRelatorio);
            }
        }
        return listaAulasRelatorio;
    }

    public static List<Aula> gerarRelatorioFrequencia(Aluno aluno, LocalDate inicio, LocalDate fim) {
        return gerarRelatorioFrequencia(aluno, inicio, fim, listarAulas());
    }

    public static List<Aula> gerarRelatorioFrequencia(Aluno aluno, LocalDate inicio, LocalDate fim, List<Aula> aulas) {
        ArrayList<Aula> alunosFrequencia = new ArrayList<>();
        if (aluno == null || aluno.getCpf() == null) {
            return alunosFrequencia;
        }
        List<Aula> listaAulasRelatorioData = listarAulasPorPeriodo(inicio, fim, aulas);
        for (Aula aula : listaAulasRelatorioData) {
            if (aula.cpfsPresentes.contains(aluno.getCpf())) {
                alunosFrequencia.add(aula);
            }
        }
        return alunosFrequencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(int idTurma) {
        this.idTurma = idTurma;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public TipoAula getTipo() {
        return tipo;
    }

    public void setTipo(TipoAula tipo) {
        this.tipo = tipo;
    }

    public StatusAula getStatus() {
        return status;
    }

    public void setStatus(StatusAula status) {
        this.status = status;
    }

    public ArrayList<String> getCpfsPresentes() {
        return cpfsPresentes;
    }

    public ArrayList<String> getCpfsFaltosos() {
        return cpfsFaltosos;
    }
}

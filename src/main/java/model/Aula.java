package model;

import java.util.ArrayList;
import java.util.List;

import dao.AulaDAO;

import java.lang.reflect.Array;
import java.time.LocalDate;
import model.enums.StatusAula;
import model.enums.TipoAula;


public class Aula{
    private int id;
    private int idTurma;
    private LocalDate data = LocalDate.now();
    private TipoAula tipo;
    private StatusAula status;
    private ArrayList<String> cpfsPresentes = new ArrayList<>();
    private ArrayList<String> cpfsFaltosos = new ArrayList<>();


    private static ArrayList<Aula> listaDeAulas = new ArrayList<>();


    public Aula(int id, int idTurma, LocalDate data, TipoAula tipo ,StatusAula status, ArrayList<String> cpfPresentes, ArrayList<String> cpfsFaltosos){
        this.idTurma = idTurma;
        this.data = data;
        this.tipo = tipo;
        this.status = status;
        this.cpfsPresentes = (cpfPresentes == null) ? new ArrayList<>() : cpfPresentes;
        this.cpfsFaltosos = (cpfsFaltosos == null) ? new ArrayList<>() : cpfsFaltosos;


        try {
            AulaDAO.criarAula(this);
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possivel criar a aula ", e);        
        }
    }
    
        
    

    public void verAula(){
        System.out.println("Data:" + data);
        System.out.println("Tipo:" + data);
        System.out.println("Status:" + data);

    }


    public void registrarPresenca(Aluno aluno){
        try {
            AulaDAO.salvarPresenca(aluno.getId(), aluno.getCpf(), true);
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possivel registrar a presença ", e);
        }
    }





    public void registrarFalta(Aluno aluno){
        cpfsFaltosos.add(aluno.getCpf());


    }


    public void cancelarAula(){
        this.status = StatusAula.CANCELADA;
        try {
            AulaDAO.atualizarAula(this);
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possivel atualizar a aula ", e);
        }
    }

    

    public void encerrarAula(){
        this.status = StatusAula.REALIZADA;
        try {
            AulaDAO.atualizarAula(this);
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possivel atualizar a aula ", e);
        }


    }

    public  static Aula criarAula(Turma turma, LocalDate data, TipoAula tipo){
        
        
        int novoId = listarAulas().size() + 1;
        int idDaTurma = turma.getIdTurma();
        data = LocalDate.now();
        StatusAula status = StatusAula.AGENDADA;
        ArrayList<String> cpfsPresentes = new ArrayList<>();
        ArrayList<String> cpfsFaltosos = new ArrayList<>();
        
        

        return new Aula(novoId, idDaTurma, data, tipo, status, cpfsPresentes, cpfsFaltosos);

    }

    public static void editarAula(Aula aula, LocalDate data, TipoAula tipo){
        aula.data = data;
        aula.tipo = tipo;

        try {
            AulaDAO.atualizarAula(aula);
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possivel atualizar a aula ", e);
        }
    }

    
    
    public static List<Aula> listarAulas(){
        try {
            return AulaDAO.listarAulas();
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possivel listar aulas ", e);
        }
       
    }


    
    public static List<Aula> listarAulasPorPeriodo(LocalDate inicio, LocalDate fim){
        ArrayList<Aula> listaAulasPeriodo = new ArrayList<>();

        for (Aula buscaPeriodo : listarAulas()) {
            if(buscaPeriodo.data.isBefore(fim) && buscaPeriodo.data.isAfter(inicio)){
                listaAulasPeriodo.add(buscaPeriodo);
            }
        }
        return listaAulasPeriodo;
    }



    public static List<Aula> listarAulasPorTurma(Turma turma){
        ArrayList<Aula> listaAulasTurma = new ArrayList<>();
        
        for (Aula listaPorTurma : listarAulas()) {
            if(listaPorTurma.idTurma == turma.getIdTurma()){
                listaAulasTurma.add(listaPorTurma);
            }
            
        }

        return listaAulasTurma;
    }

    public static List<Aula> listarAulasPorTipo(TipoAula tipo){
        ArrayList<Aula> listaAulasTipo = new ArrayList<>();

        for (Aula listaPorTipo : listarAulas()) {
            if(tipo == listaPorTipo.tipo){ 
                listaAulasTipo.add(listaPorTipo);
            }
        }

        return listaAulasTipo;
    }

    public static List<Aula> listarAulasPorAluno(Aluno aluno){
        ArrayList<Aula> listaAulasAluno = new ArrayList<>();
        

        for (Aula listandoAluno : listarAulas()){
            if(listandoAluno.cpfsPresentes.contains(aluno.getCpf())){
                listaAulasAluno.add(listandoAluno);

            }
            
        }


        return listaAulasAluno;
    }
    
    public static List<Aula> gerarRelatorioAulas(LocalDate inicio, LocalDate fim, Turma turma, TipoAula tipo){
        List<Aula>listaAulasRelatorio = new ArrayList<>();

        for (Aula aulaRelatorio : listarAulas()) {
            boolean noPeriodo = aulaRelatorio.data.isBefore(fim) && aulaRelatorio.data.isAfter(inicio);
            boolean daTurma   = aulaRelatorio.idTurma == turma.getIdTurma();
            boolean doTipo    = aulaRelatorio.tipo == tipo;

            if(noPeriodo && daTurma && doTipo){
                listaAulasRelatorio.add(aulaRelatorio);
            }
        }

        return listaAulasRelatorio;
    }
    
    

    public static List<Aula> gerarRelatorioFrequencia(Aluno aluno, LocalDate inicio, LocalDate fim){ 
        ArrayList<Aula> alunosFrequencia = new ArrayList<>() ;
        List<Aula> listaAulasRelatorioData = listarAulasPorPeriodo(inicio, fim);
        
        for (Aula aula : listaAulasRelatorioData) {
            if(aula.cpfsPresentes.contains(aluno.getCpf())){
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


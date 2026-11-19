package main.java.model;

import main.java.model.enums.Faixa;
import main.java.model.enums.CargoInstrutor;
import java.util.List;
import java.util.ArrayList;

public final class Instrutor extends Usuario {
    private CargoInstrutor cargo;
    private Faixa faixa;

    private Instrutor(String nome, String cpf, String login, String senha, String email, String telefone, CargoInstrutor cargo, Faixa faixa) {
        super(nome, cpf, login, senha, email, telefone);
        this.cargo = cargo;
        this.faixa = faixa;
    }

    public void verInstrutor() {
        verUsuario();
        System.out.println(cargo);
        System.out.println(faixa);
    }

    public static Instrutor criarInstrutor(String nome, String cpf, String login, String senha, String email, String telefone, CargoInstrutor cargo, Faixa faixa) {
        // Validações de dados

        return new Instrutor(nome, cpf, login, senha, email, telefone, cargo, faixa);
    }

    public static void editarInstrutor(Instrutor instrutor, String nome, String cpf, String login, String senha, String email, String telefone, boolean ativo, CargoInstrutor cargo, Faixa faixa) {
        editarUsuario(instrutor, nome, cpf, login, senha, email, telefone, ativo);
        instrutor.cargo = cargo;
        instrutor.faixa = faixa;
    }

    public static List<Instrutor> listarInstrutores() {
        List<Instrutor> instrutores = new ArrayList<>();

        for(Usuario usuario : listarUsuarios()) {
            if(usuario instanceof Instrutor instrutor) instrutores.add(instrutor);
        }

        return instrutores;
    }

    public static List<Instrutor> listarInstrutoresPorCargo(CargoInstrutor cargo) {
        List<Instrutor> instrutoresCargo = new ArrayList<>();

        for(Instrutor instrutor : listarInstrutores()) {
            if(instrutor.cargo == cargo) instrutoresCargo.add(instrutor);
        }
        return instrutoresCargo;
    }

    
}
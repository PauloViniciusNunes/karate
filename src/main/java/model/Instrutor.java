package model;

import java.util.ArrayList;
import java.util.List;
import model.enums.CargoInstrutor;
import model.enums.Faixa;

public final class Instrutor extends Usuario {
    private CargoInstrutor cargo;
    private Faixa faixa;

    private Instrutor(String nome, String cpf, String login, String senha, String email, String telefone, CargoInstrutor cargo, Faixa faixa) {
        super(nome, cpf, login, senha, email, telefone);
        this.cargo = cargo;
        this.faixa = faixa;
    }

    public Instrutor(int id, String nome, String cpf, String login, String senha, String email, String telefone, boolean ativo, CargoInstrutor cargo, Faixa faixa) {
        super(id, nome, cpf, login, senha, email, telefone, ativo);
        this.cargo = cargo;
        this.faixa = faixa;
    }

    public void verInstrutor() {
        verUsuario();
        System.out.println("Cargo: " + cargo);
        System.out.println("Faixa: " + faixa);
    }

    public static Instrutor criarInstrutor(String nome, String cpf, String login, String senha, String email, String telefone, CargoInstrutor cargo, Faixa faixa) {
        return new Instrutor(nome, cpf, login, senha, email, telefone, cargo, faixa);
    }

    public static void editarInstrutor(Instrutor instrutor, String nome, String cpf, String login, String senha, String email, String telefone, boolean ativo, CargoInstrutor cargo, Faixa faixa) {
        if (instrutor == null) {
            return;
        }
        instrutor.cargo = cargo;
        instrutor.faixa = faixa;
        editarUsuario(instrutor, nome, cpf, login, senha, email, telefone, ativo);
    }

    public static List<Instrutor> listarInstrutores() {
        List<Instrutor> instrutores = new ArrayList<>();
        for (Usuario usuario : Usuario.listarUsuarios()) {
            if (usuario instanceof Instrutor instrutor) {
                instrutores.add(instrutor);
            }
        }
        return instrutores;
    }

    public static List<Instrutor> listarInstrutoresPorCargo(CargoInstrutor cargo) {
        return listarInstrutoresPorCargo(cargo, listarInstrutores());
    }

    public static List<Instrutor> listarInstrutoresPorCargo(CargoInstrutor cargo, List<Instrutor> instrutores) {
        List<Instrutor> instrutoresCargo = new ArrayList<>();
        if (instrutores == null) {
            return instrutoresCargo;
        }
        for (Instrutor instrutor : instrutores) {
            if (instrutor != null && instrutor.cargo == cargo) {
                instrutoresCargo.add(instrutor);
            }
        }
        return instrutoresCargo;
    }

    public static Instrutor getInstrutor(int id) {
        for (Instrutor instrutor : listarInstrutores()) {
            if (instrutor.getId() == id) {
                return instrutor;
            }
        }
        return null;
    }

    public CargoInstrutor getCargo() {
        return cargo;
    }

    public void setCargo(CargoInstrutor cargo) {
        this.cargo = cargo;
    }

    public Faixa getFaixa() {
        return faixa;
    }

    public void setFaixa(Faixa faixa) {
        this.faixa = faixa;
    }
}
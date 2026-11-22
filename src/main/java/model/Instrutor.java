package model;

import model.enums.Faixa;
import model.enums.CargoInstrutor;

import java.util.ArrayList;
import java.util.List;
import dao.InstrutorDAO;
import java.sql.SQLException;

public final class Instrutor extends Usuario {
    private CargoInstrutor cargo;
    private Faixa faixa;

    private Instrutor(String nome, String cpf, String login, String senha, String email, String telefone, CargoInstrutor cargo, Faixa faixa) {
        super(nome, cpf, login, senha, email, telefone);
        this.cargo = cargo;
        this.faixa = faixa;
        try {
            InstrutorDAO.criarInstrutor(this);
        } catch(SQLException e) {
            throw new IllegalStateException("Não foi possivel criar o instrutor", e);
        }
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
        instrutor.cargo = cargo;
        instrutor.faixa = faixa;
        editarUsuario(instrutor, nome, cpf, login, senha, email, telefone, ativo);
    }

    public static List<Instrutor> listarInstrutores() {
        try {
            return InstrutorDAO.listarInstrutores();
        } catch(SQLException e) {
            throw new IllegalStateException("Não foi possivel listar os instrutores", e);
        }
    }

    public static List<Instrutor> listarInstrutoresPorCargo(CargoInstrutor cargo) {
        List<Instrutor> instrutoresCargo = new ArrayList<>();

        for(Instrutor instrutor : listarInstrutores()) {
            if(instrutor.cargo == cargo) instrutoresCargo.add(instrutor);
        }
        return instrutoresCargo;
    }

    public static Instrutor getInstrutor(int id) {
        try {
            return InstrutorDAO.encontrarInstrutor(id);
        } catch(SQLException e) {
            throw new IllegalStateException("Não foi possivel encontrar o instrutor de id: " + id, e);
        }
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
package view;

import controller.UsuarioController;
import model.Aluno;
import model.Instrutor;
import model.Usuario;

public class TelaLogin implements Leitor {
    public static void iniciarLogin() {
        String login, senha;
        System.out.println("==================SISTEMA DE AGENDAMENTO DE AULAS DE KARATÊ==================");
        System.out.print("Login: ");
        login = sc.nextLine();
        System.out.print("Senha: ");
        senha = sc.nextLine();

        if (!UsuarioController.autenticar(login, senha)) {
            System.err.println("Usuario nn registrado ou inativo");
            return;
        }

        Usuario usuarioLogado = UsuarioController.getUsuarioLogado();

        if (usuarioLogado instanceof Aluno aluno) {
            TelaAluno.iniciarMenu(aluno);
        } else if (usuarioLogado instanceof Instrutor instrutor) {
            TelaInstrutor.iniciarMenu(instrutor);
        } else {
            System.err.println("Este usuario não possui função real cadastrada no sistema");
        }
    }
}

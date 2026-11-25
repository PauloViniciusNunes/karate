import dao.AulaDAO;
import dao.TurmaDAO;
import dao.UsuarioDAO;
import database.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Aluno;
import model.Aula;
import model.Turma;
import model.Usuario;
import view.TelaLogin;

public class Main {
    public static void main(String[] args) {
        System.out.println("Tentando conectar ao Supabase...");

        try (Connection conn = Conexao.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("SUCESSO! Conexão estabelecida com o banco.");

                // Executa uma consulta simples para confirmar que o banco responde
                try (PreparedStatement stmt = conn.prepareStatement("SELECT current_database(), version()");
                     ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Banco: " + rs.getString(1));
                        System.out.println("Versão: " + rs.getString(2));
                    }
                }

                carregarRegistrosEmMemoria();
            }
        } catch (SQLException e) {
            System.err.println("ERRO ao conectar ao Supabase:");
            System.err.println("Mensagem: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
        }

        limparTerminal();

        TelaLogin.iniciarLogin();
    }

    private static void carregarRegistrosEmMemoria() throws SQLException {
        Usuario.limparUsuariosEmMemoria();
        for (Usuario usuario : UsuarioDAO.listarUsuarios()) {
            Usuario.registrarUsuarioEmMemoria(usuario);
        }

        Turma.limparTurmasEmMemoria();
        for (Turma turma : TurmaDAO.listarTurmas()) {
            for (Aluno aluno : Aluno.listarAlunos()) {
                if (aluno.getIdTurma() == turma.getIdTurma()) {
                    aluno.alocarNaTurma(turma);
                }
            }
            Turma.registrarTurmaEmMemoria(turma);
        }

        Aula.limparAulasEmMemoria();
        for (Aula aula : AulaDAO.listarAulas()) {
            Aula.registrarAulaEmMemoria(aula);
        }
    }

    public static void limparTerminal() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb = os.contains("win")
                ? new ProcessBuilder("cmd", "/c", "cls")
                : new ProcessBuilder("clear");

            pb.inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.print("\n".repeat(50));
        }
    }

}
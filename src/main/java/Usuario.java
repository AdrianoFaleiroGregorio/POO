import java.util.Scanner;

public class Usuario {
    private String nome;
    private String cpf;

    public void cadastrarUsuario() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nome do usuário: ");
        nome = scanner.nextLine();

        System.out.print("CPF do usuário: ");
        cpf = scanner.nextLine();

        System.out.println("Cadastrado com sucesso!");
        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + cpf);
    }
}
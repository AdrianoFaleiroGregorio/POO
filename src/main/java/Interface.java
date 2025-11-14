import java.util.Scanner;

public class Interface {
    private String tipo; // depósito ou saque
    private double valor;

    public void registrarMovimentacao() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o tipo de movimentação (saque/deposito): ");
        tipo = scanner.nextLine();

        System.out.print("Digite o valor: ");
        valor = Double.parseDouble(scanner.nextLine());

        System.out.println("Movimentação registrada com sucesso!");
        System.out.println("Tipo: " + tipo);
        System.out.println("Valor: " + valor);
    }
}

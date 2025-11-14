package DesafioPratico;

import java.util.Scanner;

public class Conta {
    private int numero;
    private double saldoInicial;

    public void cadastrarConta() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o número da conta: ");
        numero = Integer.parseInt(scanner.nextLine());

        System.out.print("Digite o saldo inicial: ");
        saldoInicial = Double.parseDouble(scanner.nextLine());

        System.out.println("DesafioPratico.Conta cadastrada com sucesso!");
        System.out.println("Número: " + numero);
        System.out.println("Saldo inicial: " + saldoInicial);
    }
}

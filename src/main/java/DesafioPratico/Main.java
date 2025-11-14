package DesafioPratico;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        while (opcao != 4) {
            System.out.println("\n        Escolha uma opção       " +
                    "\n                                " +
                    "\n1- Cadastro de usuário          " +
                    "\n2- Cadastro de conta            " +
                    "\n3- Cadastro de movimentação     " +
                    "\n4- Sair                         \n");

            System.out.print("Digite a uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Inválido");
                continue;
            }

            switch (opcao) {
                case 1:
                    Usuario usuario = new Usuario();
                    usuario.cadastrarUsuario();
                    break;

                case 2:
                    Conta conta = new Conta();
                    conta.cadastrarConta();
                    break;

                case 3:
                    Interface inter = new Interface();
                    inter.registrarMovimentacao();
                    break;

                case 4:
                    System.out.println("Saindo");
                    break;

                default:
                    System.out.println("Opção inválida");
            }
        }
    }
}

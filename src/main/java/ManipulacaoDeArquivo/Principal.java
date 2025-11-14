import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Principal {

    private String nomeDoArquivo;

    public Principal(String nomeArquivo) {
        this.nomeDoArquivo = nomeArquivo;
    }

    public void inserirDados(String registro) {
        File fArquivo = new File(this.nomeDoArquivo);

        try (FileWriter fwArquivo = new FileWriter(fArquivo, true);
             BufferedWriter bw = new BufferedWriter(fwArquivo)) {

            bw.write(registro + "\n");
            System.out.println("Registro adicionado com sucesso...");

        } catch (IOException e) {
            System.err.println("Erro ao inserir linhas no arquivo: " + e.getMessage());
        }
    }

    public void listarDados() {
        File arquivo = new File(this.nomeDoArquivo);
        Scanner lendoArquivo = null;

        try {
            lendoArquivo = new Scanner(arquivo);

            while (lendoArquivo.hasNextLine()) {
                this.processandoLinha(lendoArquivo.nextLine());
            }

        } catch (FileNotFoundException e) {
            System.err.println("Erro: arquivo nao existe. " + arquivo);
        } finally {
            if (lendoArquivo != null) {
                try {
                    lendoArquivo.close();
                } catch (Exception e) {}
            }
        }
    }

    public void buscarDados(String nomeBusca) {
        File arquivo = new File(this.nomeDoArquivo);
        Scanner lendoArquivo = null;
        boolean encontrado = false;

        try {
            lendoArquivo = new Scanner(arquivo);

            while (lendoArquivo.hasNextLine()) {
                String linha = lendoArquivo.nextLine();
                String[] campos = linha.split(":");

                if (campos.length >= 2) {
                    String nome = campos[0].trim();
                    if (nome.equalsIgnoreCase(nomeBusca.trim())) {
                        Contato c = new Contato(nome, campos[1].trim());
                        System.out.println(c);
                        System.out.println("----------------------------------");
                        encontrado = true;
                    }
                }
            }

            if (!encontrado) {
                System.out.println("Contato não encontrado!");
            }

        } catch (FileNotFoundException e) {
            System.err.println("Erro: arquivo nao existe. " + arquivo);
        } finally {
            if (lendoArquivo != null) {
                try {
                    lendoArquivo.close();
                } catch (Exception e) {}
            }
        }
    }

    private void processandoLinha(String linha) {

        if (linha != null && !linha.trim().isEmpty()) {
            try {
                String[] campos = linha.split(":");

                if (campos.length >= 2) {
                    Contato c = new Contato(campos[0].trim(), campos[1].trim());
                    System.out.println(c);
                    System.out.println("--------------------------------------");
                } else {
                    System.err.println("[ERRO] Linha inválida no arquivo: " + linha);
                }

            } catch (Exception e) {
                System.err.println("[ERRO INESPERADO] Falha ao processar linha: " + linha);
            }
        }
    }

    public void menu() {
        Scanner teclado = new Scanner(System.in);
        int op = 0;

        do {
            System.out.println("\n..:: Trabalhando com Arquivos Texto ::..");
            System.out.println("1 - Inserir linha");
            System.out.println("2 - Listar todo arquivo");
            System.out.println("3 - Sair");
            System.out.println("4 - Buscar por Nome");
            System.out.print("Entre com uma opcao: ");
            op = teclado.nextInt();

            switch (op) {
                case 1:
                    teclado.nextLine();
                    System.out.print("Nome: ");
                    String nome = teclado.nextLine();
                    System.out.print("Fone: ");
                    String telefone = teclado.nextLine();
                    inserirDados(nome + ":" + telefone);
                    break;

                case 2:
                    listarDados();
                    break;

                case 4:
                    teclado.nextLine();
                    System.out.print("Entre com o nome para busca: ");
                    String nomeBusca = teclado.nextLine();
                    buscarDados(nomeBusca);
                    break;

                case 3:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } while (op != 3);

        teclado.close();
    }

    public static void main(String[] args) {
        Principal p = new Principal("c:/dev/logs/agenda-poo.txt");
        p.menu();
    }
}

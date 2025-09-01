import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Cliente extends Geral implements CRUD {
    private static String caminhoArquivo = "E:\\ECLIPSE\\Projetos Java\\Projeto Final\\clientes.txt";

    private String endereco;
    private String contato;
    private int planoAssociado;

    public Cliente() throws InputMismatchException {
        System.out.println("Nome: ");
        String nome = sc.nextLine();
        System.out.println("Endereço: ");
        String endereco = sc.nextLine();
        System.out.println("Contato: ");
        String contato = sc.nextLine();
        System.out.println("Plano associado: ");
        int planoAssociado = sc.nextInt();

        this.nome = nome.replace(",", "").trim();
        this.endereco = endereco.replace(",", "").trim();
        this.contato = contato.replace(",", "").trim();
        this.planoAssociado = planoAssociado;
    }

    // cadastra o cliente no arquivo clientes
    public void cadastrar() throws IOException {

        int maiorId = obterIdDinamicamente(caminhoArquivo);

        // escrevendo no arquivo
        FileWriter arquivoWriter = new FileWriter(caminhoArquivo, true);
        arquivoWriter.write(String.format("%d,%s,%s,%s,%d\n", maiorId+1, getNome(), getEndereco(), getContato(), getPlanoAssociado()));
        arquivoWriter.close();

        System.out.println("Cliente cadastrado.");
    }

    // lista todos os clientes do arquivo clientes
    public static void listar() throws IOException {
        sc = new Scanner (new File(caminhoArquivo));

        System.out.printf("+----+------------------------------+------------------------------------------+--------------------------+----------------------+%n");
        System.out.printf("| %-2s | %-28s | %-40s | %-24s | %-20s |%n", "ID", "Nome", "Endereço", "Contato", "Plano Associado");
        System.out.printf("+----+------------------------------+------------------------------------------+--------------------------+----------------------+%n");

        while (sc.hasNextLine()) {
            String[] linha = sc.nextLine().split(",");

            System.out.printf("| %-2s | %-28s | %-40s | %-24s | %-20s | %n", linha[0], linha[1], linha[2], linha[3], getNomePlanoAssociado(linha));
        }

        System.out.printf("+----+------------------------------+------------------------------------------+--------------------------+----------------------+%n");

        sc.close();
    }

    public static void atualizar() throws IOException, InputMismatchException {
        ArrayList<String[]> clientes = new ArrayList<>();

        System.out.print("Digite o ID do cliente que deseja atualizar: ");
        int escolha = sc.nextInt();
        sc.nextLine(); // Consumir quebra de linha após o nextInt

        // Lê os dados do arquivo
        Scanner leitor = new Scanner(new File(caminhoArquivo));
        while (leitor.hasNextLine()) {
            String[] linha = leitor.nextLine().split(",");
            clientes.add(linha);
        }
        leitor.close();

        // Atualiza os dados
        boolean encontrado = false;
        for (int i = 0; i < clientes.size(); i++) {
            int id = parseInt(clientes.get(i)[0]);
            if (id == escolha) {

                System.out.println("Você está editando o cliente: ");
                System.out.printf("ID: %s | Nome: %s | Endereço: %s | Contato: %s | Plano: %s %n",
                        clientes.get(i)[0], clientes.get(i)[1], clientes.get(i)[2], clientes.get(i)[3], clientes.get(i)[4] );

                System.out.print("Novo nome: ");
                String nome = sc.nextLine();
                System.out.print("Novo endereço: ");
                String endereco = sc.nextLine();
                System.out.print("Novo contato: ");
                String contato = sc.nextLine();
                System.out.print("Novo plano: ");
                String plano = sc.nextLine();
                
                clientes.get(i)[1] = nome.replace(",", "").trim();
                clientes.get(i)[2] = endereco.replace(",", "").trim();
                clientes.get(i)[3] = contato.replace(",", "").trim();
                clientes.get(i)[4] = plano.replace(",", "").trim();
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("ID não encontrado.");
            return;
        }

        // Reescreve o arquivo com os dados atualizados
        PrintWriter writer = new PrintWriter(new FileWriter(caminhoArquivo));
        for (String[] cliente : clientes) {
            writer.println(String.join(",", cliente));
        }
        writer.close();

        System.out.println("Cliente atualizado com sucesso.");
    }

    public static void excluir() throws IOException, InputMismatchException {
        ArrayList<String[]> clientes = new ArrayList<>();

        System.out.println("Digite o ID do cliente que deseja remover: ");
        int escolha = sc.nextInt();

        sc = new Scanner (new File(caminhoArquivo));

        while (sc.hasNextLine()) {
            String[] linha = sc.nextLine().split(",");
            clientes.add(linha);
        }

        // Remove a linha com o ID correspondente
        boolean encontrado = false;
        for (int i = 0; i < clientes.size(); i++) {
            int id = parseInt(clientes.get(i)[0]);
            if (id == escolha) {
                clientes.remove(i);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("ID não encontrado.");
            return;
        }

        // Reescreve o arquivo com os dados atualizados
        PrintWriter writer = new PrintWriter(new FileWriter(caminhoArquivo));
        for (String[] cliente : clientes) {
            writer.println(String.join(",", cliente));
        }
        writer.close();

        System.out.println("Cliente excluído com sucesso.");
    }

    public static String getNomePlanoAssociado(String[] linha) throws IOException {
        if (linha.length < 5 || linha[4].isEmpty()) {
            return "Sem plano";
        }

        String idPlano = linha[4];
        String nomePlano = "Sem plano";

        Scanner scPlano = new Scanner(new File("E:\\ECLIPSE\\Projetos Java\\Projeto Final\\planos.txt"));
        while (scPlano.hasNextLine()) {
            String[] linhaPlano = scPlano.nextLine().split(",");
            if (linhaPlano.length > 1 && linhaPlano[0].equals(idPlano)) {
                nomePlano = linhaPlano[1];
                break;
            }
        }
        scPlano.close();

        return nomePlano;
    }


    // ---------------- métodos get e set

    public static String getCaminhoArquivo() {
        return caminhoArquivo;
    }
    public static void setCaminhoArquivo(String caminhoArquivo) {
        Cliente.caminhoArquivo = caminhoArquivo;
    }

    public static Scanner getSc() {
        return sc;
    }
    public static void setSc(Scanner sc) {
        Cliente.sc = sc;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getContato() {
        return contato;
    }
    public void setContato(String contato) {
        this.contato = contato;
    }

    public int getPlanoAssociado() {
        return planoAssociado;
    }
    public void setPlanoAssociado(int planoAssociado) {
        this.planoAssociado = planoAssociado;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                ", contato='" + contato + '\'' +
                '}';
    }
}

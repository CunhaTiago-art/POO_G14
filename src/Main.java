/*
 * Trabalho Prático POO 2026/2027 - Tema T04: Os meus filmes
 * Grupo: G14
 * 
 * Elementos do Grupo:
 * 1. Tiago Cunha - Número: 54272
 * 2. [Nome do Aluno 2] - Número: [Número]
 * 3. [João Leitão] - Número: 55193
 * 4. [Nome do Aluno 4] - Número: [Número]
 */

import dados.DadosIniciais;
import dados.GestorDados;
import dados.RelatorioExport;
import modelo.GestorFilmes;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        // 1. Carregar os dados guardados
        GestorDados dados = new GestorDados();
        GestorFilmes gestor;
        try {
            if (dados.existeFicheiro()) {
                System.out.println("A carregar dados de " + dados.getFicheiro().getAbsolutePath());
                gestor = dados.carregar();
            } else {
                // 2. Primeira execução: começar com dados de exemplo
                System.out.println("Primeira execução: a criar dados de exemplo.");
                gestor = DadosIniciais.criar();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Não foi possível ler os dados (" + e.getMessage() + "). A usar dados de exemplo.");
            gestor = DadosIniciais.criar();
        }

        // 3. Mostrar o relatório na consola
        System.out.println();
        System.out.println(RelatorioExport.gerarTexto(gestor));

        // 4. Exportar o relatório para um ficheiro .txt
        File relatorio = new File(RelatorioExport.NOME_FICHEIRO);
        try {
            RelatorioExport.exportar(gestor, relatorio);
            System.out.println("Relatório exportado para " + relatorio.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Erro ao exportar o relatório: " + e.getMessage());
        }

        // 5. Guardar tudo antes de sair
        try {
            dados.guardar(gestor);
            System.out.println("Dados guardados em " + dados.getFicheiro().getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Erro ao guardar os dados: " + e.getMessage());
        }
    }
}
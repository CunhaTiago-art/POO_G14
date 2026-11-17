/*
 * Trabalho Prático POO 2026/2027 - Tema T04: Os meus filmes
 * Grupo: G14
 * 
 * Elementos do Grupo:
 * 1. Tiago Cunha - Número: 54272
 * 2. [Nome do Aluno 2] - Número: [Número]
 * 3. [Nome do Aluno 3] - Número: [Número]
 * 4. [Nome do Aluno 4] - Número: [Número]
 */

import modelo.Amigo;
import modelo.Categoria;
import modelo.Filme;
import modelo.GestorFilmes;
import modelo.Localizacao;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        
        // 1. Criar o gestor
        GestorFilmes gestor = new GestorFilmes();
        
        // 2. Criar localizações de teste
        Localizacao loc1 = new Localizacao("Prateleira Quarto", "Fila A");
        Localizacao loc2 = new Localizacao("Disco Externo", "Pasta Filmes 2026");
        
        // 3. Criar filmes de teste
        Filme filme1 = new Filme("O Padrinho", "Francis Ford Coppola", 1972, true, 
                                 Categoria.DRAMA, loc1, LocalDate.now());
                                 
        Filme filme2 = new Filme("Matrix", "Wachowskis", 1999, false, 
                                 Categoria.FICCAO_CIENTIFICA, loc2, LocalDate.now());
        
        // 4. Adicionar ao gestor
        gestor.adicionarFilme(filme1);
        gestor.adicionarFilme(filme2);
        
        // 5. Testar amigos e empréstimos
        Amigo amigo1 = new Amigo("Carlos Silva", "912345678");
        gestor.registarAmigo(amigo1);

        System.out.println("--- Coleção Inicial ---");
        gestor.listarFilmes();

        System.out.println("\n--- Realizar Empréstimo ---");
        gestor.emprestarFilme(filme1, amigo1);
        gestor.listarHistoricoEmprestimos();

        System.out.println("\n--- Estado após empréstimo ---");
        gestor.listarFilmes();

        System.out.println("\n--- Devolver Filme ---");
        gestor.devolverFilme(filme1);
        gestor.listarHistoricoEmprestimos();

        System.out.println("\n--- Estado final após devolução ---");
        gestor.listarFilmes();
    }
}

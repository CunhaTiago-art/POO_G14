import modelo.Categoria;
import modelo.Filme;
import modelo.Localizacao;

import java.time.LocalDate;

/**
 * Classe principal para testes do modelo de dados.
 * Grupo G14 - Tiago Cunha (54272)
 */
public class Main {
    public static void main(String[] args) {
        Localizacao loc = new Localizacao("Sala", "Prateleira 3");
        Filme filme = new Filme(
                "Inception",
                "Christopher Nolan",
                2010,
                true,
                Categoria.FICCAO_CIENTIFICA,
                loc,
                LocalDate.of(2023, 5, 20)
        );

        System.out.println("=== Teste do Modelo ===");
        System.out.println(filme);

        filme.setEstaEmprestado(true);
        System.out.println("Após empréstimo: " + filme);
    }
}

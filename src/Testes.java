import dados.DadosIniciais;
import modelo.Amigo;
import modelo.Categoria;
import modelo.Filme;
import modelo.GestorFilmes;
import modelo.Localizacao;

import java.time.LocalDate;

/**
 * Testes ao GestorFilmes (Parte 3).
 * Usa sempre dados de exemplo novos e NÃO mexe no filmes.dat,
 * por isso pode ser corrido as vezes que se quiser.
 *
 * Para correr: botão direito neste ficheiro -> Run As -> Java Application
 */
public class Testes {

    public static void main(String[] args) {

        GestorFilmes gestor = DadosIniciais.criar();

        // Objetos usados nos testes (procurados pelo nome)
        Filme padrinho = gestor.pesquisarPorTitulo("padrinho").get(0);
        Filme matrix = gestor.pesquisarPorTitulo("matrix").get(0);
        Filme gladiador = gestor.pesquisarPorTitulo("gladiador").get(0);
        Filme shining = gestor.pesquisarPorTitulo("shining").get(0);
        Amigo ana = gestor.procurarAmigo("Ana Costa");
        Amigo carlos = gestor.procurarAmigo("Carlos Silva");

        System.out.println("ESTADO INICIAL");
        mostrarResumo(gestor);

        // ------------------------------------------------------------------
        titulo(1, "Emprestar 'O Padrinho' à Ana",
               "fica emprestado; emprestados passa de 3 para 4");
        try {
            gestor.emprestarFilme(padrinho, ana);
            System.out.println("Resultado: OK, emprestado? " + padrinho.isEstaEmprestado());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Resultado: Erro -> " + e.getMessage());
        }
        mostrarResumo(gestor);

        // ------------------------------------------------------------------
        titulo(2, "Emprestar 'O Padrinho' outra vez",
               "ERRO, porque já está emprestado");
        try {
            gestor.emprestarFilme(padrinho, carlos);
            System.out.println("Resultado: OK (isto NÃO devia acontecer!)");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Resultado: Erro -> " + e.getMessage());
        }

        // ------------------------------------------------------------------
        titulo(3, "Devolver a 'Matrix' (estava em atraso)",
               "deixa de haver alertas de atraso");
        System.out.println("Alertas antes:  " + gestor.getAlertas().trim());
        try {
            gestor.devolverFilme(matrix);
            System.out.println("Resultado: OK");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Resultado: Erro -> " + e.getMessage());
        }
        System.out.println("Alertas depois: " + gestor.getAlertas().trim());

        // ------------------------------------------------------------------
        titulo(4, "Emprestar o 'Gladiador' (está perdido)",
               "ERRO, porque o filme está perdido");
        try {
            gestor.emprestarFilme(gladiador, ana);
            System.out.println("Resultado: OK (isto NÃO devia acontecer!)");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Resultado: Erro -> " + e.getMessage());
        }

        // ------------------------------------------------------------------
        titulo(5, "Comprar 'Inception' por 11,50 €",
               "11 filmes e total gasto sobe 11,50 €");
        try {
            Filme inception = new Filme("Inception", "Christopher Nolan", 2010, true,
                    Categoria.FICCAO_CIENTIFICA, new Localizacao("Estante da Sala", "Prateleira 3"),
                    LocalDate.now());
            gestor.adicionarFilme(inception, 11.50, "FNAC");
            System.out.println("Resultado: OK");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Resultado: Erro -> " + e.getMessage());
        }
        mostrarResumo(gestor);

        // ------------------------------------------------------------------
        titulo(6, "Comprar 'Inception' outra vez",
               "ERRO, porque o filme já existe");
        try {
            gestor.adicionarFilme(new Filme("inception", "Nolan", 2010, false,
                    Categoria.FICCAO_CIENTIFICA, null, LocalDate.now()));
            System.out.println("Resultado: OK (isto NÃO devia acontecer!)");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Resultado: Erro -> " + e.getMessage());
        }

        // ------------------------------------------------------------------
        titulo(7, "Remover a Ana (tem filmes emprestados)",
               "ERRO, porque ainda tem filmes por devolver");
        try {
            gestor.removerAmigo(ana);
            System.out.println("Resultado: OK (isto NÃO devia acontecer!)");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Resultado: Erro -> " + e.getMessage());
        }

        // ------------------------------------------------------------------
        titulo(8, "Remover o Carlos (já devolveu a Matrix no teste 3)",
               "OK, amigos passa de 3 para 2");
        try {
            gestor.removerAmigo(carlos);
            System.out.println("Resultado: OK");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Resultado: Erro -> " + e.getMessage());
        }
        mostrarResumo(gestor);

        // ------------------------------------------------------------------
        titulo(9, "A Ana perdeu 'The Shining' (estava emprestado)",
               "filme fica perdido e o empréstimo é fechado");
        try {
            gestor.registarPerda(shining, LocalDate.now(), "A Ana partiu o DVD");
            System.out.println("Resultado: OK, perdido? " + gestor.isPerdido(shining)
                    + " | emprestado? " + shining.isEstaEmprestado());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Resultado: Erro -> " + e.getMessage());
        }
        mostrarResumo(gestor);

        // ------------------------------------------------------------------
        titulo(10, "Pesquisar por realizador 'nolan'",
               "aparecem Interstellar e Inception");
        for (Filme f : gestor.pesquisarPorRealizador("nolan")) {
            System.out.println("  - " + f.getTitulo());
        }

        System.out.println("\nFIM DOS TESTES");
    }

    // Escreve o cabeçalho de cada teste
    private static void titulo(int numero, String descricao, String esperado) {
        System.out.println("\n--------------------------------------------");
        System.out.println("TESTE " + numero + ": " + descricao);
        System.out.println("Esperado:  " + esperado);
    }

    // Mostra os números principais da coleção numa só linha
    private static void mostrarResumo(GestorFilmes g) {
        System.out.println("  [filmes: " + g.getMeusFilmes().size()
                + " | disponíveis: " + g.getFilmesDisponiveis().size()
                + " | emprestados: " + g.getFilmesEmprestados().size()
                + " | perdidos: " + g.getFilmesPerdidos().size()
                + " | amigos: " + g.getMeusAmigos().size()
                + " | gasto: " + String.format("%.2f €", g.getTotalGastoAquisicoes()) + "]");
    }
}
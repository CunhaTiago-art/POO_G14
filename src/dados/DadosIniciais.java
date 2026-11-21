package dados;

import java.time.LocalDate;

import modelo.Amigo;
import modelo.Categoria;
import modelo.Filme;
import modelo.GestorFilmes;
import modelo.Localizacao;

/**
 * Gerador de dados de exemplo (seed data) para testes (Parte 3).
 * Usado quando a aplicação arranca sem ficheiro de dados.
 *
 * As datas são calculadas a partir de hoje, para que haja sempre
 * um empréstimo em atraso e outro dentro do prazo, seja qual for o dia em que se corre.
 */
public class DadosIniciais {

    private DadosIniciais() {
        // classe utilitária: só tem métodos static, não se criam objetos dela
    }

    public static GestorFilmes criar() {
        GestorFilmes g = new GestorFilmes();
        LocalDate hoje = LocalDate.now();

        // --- Localizações ---
        Localizacao estanteSala = new Localizacao("Estante da Sala", "Prateleira 1");
        Localizacao estanteQuarto = new Localizacao("Estante do Quarto", "Prateleira 2");
        Localizacao discoExterno = new Localizacao("Disco Externo", "Filmes/HD");
        Localizacao portatil = new Localizacao("Portátil", "Vídeos/Filmes");

        // --- Filmes (com aquisição: preço e local de compra) ---
        Filme padrinho = new Filme("O Padrinho", "Francis Ford Coppola", 1972, true,
                Categoria.DRAMA, estanteSala, hoje.minusYears(3));
        Filme matrix = new Filme("Matrix", "Lana e Lilly Wachowski", 1999, true,
                Categoria.FICCAO_CIENTIFICA, estanteSala, hoje.minusYears(2));
        Filme interstellar = new Filme("Interstellar", "Christopher Nolan", 2014, false,
                Categoria.FICCAO_CIENTIFICA, discoExterno, hoje.minusMonths(18));
        Filme toyStory = new Filme("Toy Story", "John Lasseter", 1995, true,
                Categoria.ANIMACAO, estanteQuarto, hoje.minusYears(5));
        Filme shining = new Filme("The Shining", "Stanley Kubrick", 1980, true,
                Categoria.TERROR, estanteQuarto, hoje.minusYears(1));
        Filme gladiador = new Filme("Gladiador", "Ridley Scott", 2000, false,
                Categoria.ACAO, portatil, hoje.minusMonths(8));
        Filme superbad = new Filme("Superbad", "Greg Mottola", 2007, true,
                Categoria.COMEDIA, estanteQuarto, hoje.minusMonths(14));
        Filme planeta = new Filme("Planeta Terra", "Alastair Fothergill", 2006, false,
                Categoria.DOCUMENTARIO, discoExterno, hoje.minusMonths(6));
        Filme seven = new Filme("Seven", "David Fincher", 1995, true,
                Categoria.DRAMA, estanteSala, hoje.minusMonths(30));
        Filme upAltamente = new Filme("Up - Altamente", "Pete Docter", 2009, true,
                Categoria.ANIMACAO, estanteQuarto, hoje.minusMonths(20));

        g.adicionarFilme(padrinho, 12.99, "FNAC");
        g.adicionarFilme(matrix, 9.99, "Worten");
        g.adicionarFilme(interstellar, 7.99, "Loja online");
        g.adicionarFilme(toyStory, 5.00, "Feira da Ladra");
        g.adicionarFilme(shining, 8.50, "FNAC");
        g.adicionarFilme(gladiador, 6.99, "Loja online");
        g.adicionarFilme(superbad, 4.00, "Cash Converters");
        g.adicionarFilme(planeta, 0.00, "Oferta");
        g.adicionarFilme(seven, 10.00, "Worten");
        g.adicionarFilme(upAltamente, 7.50, "Continente");

        // --- Amigos ---
        Amigo carlos = new Amigo("Carlos Silva", "Colega de curso", "carlos.silva@ubi.pt", "912345678");
        Amigo ana = new Amigo("Ana Costa", "Vizinha", "ana.costa@gmail.com", "936112233");
        Amigo rui = new Amigo("Rui Martins", "Primo", null, "965443322");
        g.registarAmigo(carlos);
        g.registarAmigo(ana);
        g.registarAmigo(rui);

        // --- Empréstimos ---
        // Já devolvido (histórico)
        g.emprestarFilme(toyStory, ana, hoje.minusDays(60), hoje.minusDays(46));
        g.devolverFilme(toyStory, hoje.minusDays(50));

        // Em atraso: o prazo acabou há 10 dias
        g.emprestarFilme(matrix, carlos, hoje.minusDays(24), hoje.minusDays(10));

        // Ativo, dentro do prazo
        g.emprestarFilme(shining, ana, hoje.minusDays(3), hoje.plusDays(11));

        // Ativo, empréstimo de hoje com o prazo por omissão (14 dias)
        g.emprestarFilme(superbad, rui);

        // --- Perda ---
        // Emprestado ao Rui, que o perdeu: o empréstimo é fechado na data da perda
        g.emprestarFilme(gladiador, rui, hoje.minusDays(40), hoje.minusDays(26));
        g.registarPerda(gladiador, hoje.minusDays(30), "O Rui perdeu a pen onde estava o filme");

        return g;
    }
}
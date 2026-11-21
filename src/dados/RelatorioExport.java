package dados;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import modelo.Amigo;
import modelo.Categoria;
import modelo.Emprestimo;
import modelo.Filme;
import modelo.GestorFilmes;
import modelo.LocalDateUtils;
import modelo.RegistoMovimento;

/**
 * Exportação de um resumo da coleção para um ficheiro de texto (.txt) (Parte 3).
 *
 * gerarTexto() monta o relatório numa String (pode ser mostrado na consola
 * ou numa janela JavaFX); exportar() grava essa String num ficheiro.
 */
public class RelatorioExport {

    public static final String NOME_FICHEIRO = "relatorio.txt";
    private static final String LINHA = "==================================================";

    private RelatorioExport() {
        // classe utilitária: só tem métodos static
    }

    /**
     * Grava o relatório no ficheiro indicado (substitui o conteúdo anterior).
     */
    public static void exportar(GestorFilmes gestor, File ficheiro) throws IOException {
        if (gestor == null || ficheiro == null) {
            throw new IllegalArgumentException("Gestor e ficheiro são obrigatórios.");
        }
        // UTF-8 para os acentos ficarem bem em qualquer computador
        try (PrintWriter out = new PrintWriter(ficheiro, StandardCharsets.UTF_8)) {
            out.print(gerarTexto(gestor));
        }
    }

    /**
     * Monta o texto completo do relatório.
     */
    public static String gerarTexto(GestorFilmes gestor) {
        StringBuilder sb = new StringBuilder();

        sb.append(LINHA).append('\n');
        sb.append("  OS MEUS FILMES - Relatório de ")
          .append(LocalDateUtils.formatar(LocalDate.now())).append('\n');
        sb.append(LINHA).append("\n\n");

        // --- Resumo ---
        sb.append("RESUMO\n");
        sb.append("  Total de filmes:      ").append(gestor.getMeusFilmes().size()).append('\n');
        sb.append("  Disponíveis:          ").append(gestor.getFilmesDisponiveis().size()).append('\n');
        sb.append("  Emprestados:          ").append(gestor.getFilmesEmprestados().size()).append('\n');
        sb.append("  Perdidos:             ").append(gestor.getFilmesPerdidos().size()).append('\n');
        sb.append("  Amigos registados:    ").append(gestor.getMeusAmigos().size()).append('\n');
        sb.append("  Total gasto:          ")
          .append(String.format("%.2f €", gestor.getTotalGastoAquisicoes())).append('\n');
        Amigo top = gestor.getAmigoComMaisEmprestimos();
        if (top != null) {
            sb.append("  Amigo mais frequente: ").append(top.getNome()).append('\n');
        }
        sb.append('\n');

        // --- Coleção, agrupada por categoria e ordenada por título ---
        sb.append("COLEÇÃO POR CATEGORIA\n");
        for (Categoria c : Categoria.values()) {
            List<Filme> filmes = gestor.pesquisarPorCategoria(c);
            if (filmes.isEmpty()) {
                continue;
            }
            filmes.sort(Comparator.comparing(Filme::getTitulo, String.CASE_INSENSITIVE_ORDER));
            sb.append("  [").append(c).append("]\n");
            for (Filme f : filmes) {
                sb.append("    - ").append(f.getTitulo())
                  .append(" (").append(f.getAnoLancamento()).append(")")
                  .append(", ").append(f.getRealizador())
                  .append(" | ").append(f.isFormatoFisico() ? "Físico" : "Digital")
                  .append(" | ").append(f.getLocalizacao() != null ? f.getLocalizacao() : "sem localização")
                  .append(" | ").append(estado(gestor, f))
                  .append('\n');
            }
        }
        sb.append('\n');

        // --- Empréstimos ativos ---
        sb.append("EMPRÉSTIMOS ATIVOS\n");
        List<Emprestimo> ativos = gestor.getEmprestimosAtivos();
        if (ativos.isEmpty()) {
            sb.append("  (nenhum)\n");
        }
        for (Emprestimo e : ativos) {
            sb.append("  - ").append(e).append('\n');
        }
        sb.append('\n');

        // --- Alertas de atraso ---
        sb.append("ALERTAS\n");
        sb.append("  ").append(gestor.getAlertas().trim().replace("\n", "\n  ")).append("\n\n");

        // --- Aquisições e perdas ---
        sb.append("AQUISIÇÕES E PERDAS\n");
        List<RegistoMovimento> movimentos = gestor.getMovimentos();
        if (movimentos.isEmpty()) {
            sb.append("  (nenhum)\n");
        }
        for (RegistoMovimento m : movimentos) {
            sb.append("  - ").append(m).append('\n');
        }

        return sb.toString();
    }

    private static String estado(GestorFilmes gestor, Filme f) {
        if (gestor.isPerdido(f)) {
            return "PERDIDO";
        }
        if (f.isEstaEmprestado()) {
            Emprestimo e = gestor.getEmprestimoAtivo(f);
            return "EMPRESTADO a " + (e != null ? e.getAmigo().getNome() : "?");
        }
        return "Disponível";
    }
}
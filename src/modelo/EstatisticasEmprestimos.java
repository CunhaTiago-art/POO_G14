package modelo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* Alertas de atraso e estatísticas
* Recebe as listas (que vivem no GestorFilmes) e devolve resultados.
*/
public class EstatisticasEmprestimos {

    private EstatisticasEmprestimos() {
    }

    public static List<Emprestimo> ativos(List<Emprestimo> todos) {
        return todos.stream().filter(Emprestimo::isAtivo).collect(Collectors.toList());
    }

    public static List<Emprestimo> emAtraso(List<Emprestimo> todos) {
        return todos.stream().filter(Emprestimo::isAtrasado).collect(Collectors.toList());
    }

    public static long totalAtivos(List<Emprestimo> todos) {
        return todos.stream().filter(Emprestimo::isAtivo).count();
    }

    public static long totalEmAtraso(List<Emprestimo> todos) {
        return todos.stream().filter(Emprestimo::isAtrasado).count();
    }

    public static double totalGastoAquisicoes(List<RegistoMovimento> movimentos) {
        return movimentos.stream()
                .filter(m -> m.getTipo() == TipoMovimento.AQUISICAO)
                .mapToDouble(RegistoMovimento::getPreco)
                .sum();
    }

    public static Amigo amigoComMaisEmprestimos(List<Emprestimo> todos) {
        return todos.stream()
                .collect(Collectors.groupingBy(Emprestimo::getAmigo, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public static String gerarAlertas(List<Emprestimo> todos) {
        List<Emprestimo> atrasados = emAtraso(todos);
        if (atrasados.isEmpty()) {
            return "Sem empréstimos em atraso.";
        }
        StringBuilder sb = new StringBuilder("ATENÇÃO - empréstimos em atraso:\n");
        for (Emprestimo e : atrasados) {
            sb.append(" - ").append(e.getFilme().getTitulo())
              .append(" (").append(e.getAmigo().getNome()).append(") ")
              .append(e.diasDeAtraso()).append(" dias de atraso\n");
        }
        return sb.toString();
    }
}

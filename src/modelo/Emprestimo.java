package modelo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa o empréstimo de um filme a um amigo.
 * Utiliza LocalDate para controlo temporal (empréstimo e devolução).
 * Implementa Serializable para persistência.
 */
public class Emprestimo implements Serializable {
    
    private Filme filme;
    private Amigo amigo;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao; // null enquanto o filme não for devolvido

    public Emprestimo(Filme filme, Amigo amigo) {
        this.filme = filme;
        this.amigo = amigo;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucao = null;
    }

    public Filme getFilme() {
        return filme;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
    }

    public Amigo getAmigo() {
        return amigo;
    }

    public void setAmigo(Amigo amigo) {
        this.amigo = amigo;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public boolean isAtivo() {
        return dataDevolucao == null;
    }

    @Override
    public String toString() {
        return "Empréstimo: '" + (filme != null ? filme.getTitulo() : "Desconhecido") + 
               "' a " + (amigo != null ? amigo.getNome() : "Desconhecido") + 
               " em " + dataEmprestimo + 
               (dataDevolucao != null ? " | Devolvido em: " + dataDevolucao : " | [Ativo / Não Devolvido]");
    }
}

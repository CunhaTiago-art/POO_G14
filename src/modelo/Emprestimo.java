package modelo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa o empréstimo de um filme a um amigo.
 * Utiliza LocalDate para controlo temporal (empréstimo e devolução).
 * Implementa Serializable para persistência.
 */
public class Emprestimo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Filme filme;
    private Amigo amigo;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao; // null enquanto o filme não for devolvido
    
    public static final int PRAZO_POR_DEFEITO_DIAS = 14;
    private LocalDate dataPrevistaDevolucao;
    
    public Emprestimo(Filme filme, Amigo amigo) {
        this(filme, amigo, LocalDate.now(), LocalDate.now().plusDays(PRAZO_POR_DEFEITO_DIAS));
    }

    //  empréstimo hoje, com data prevista escolhida
    public Emprestimo(Filme filme, Amigo amigo, LocalDate dataPrevistaDevolucao) {
        this(filme, amigo, LocalDate.now(), dataPrevistaDevolucao);
    }
    
    // empréstimo com todas as datas escolhidas
    public Emprestimo(Filme filme, Amigo amigo, LocalDate dataEmprestimo, LocalDate dataPrevistaDevolucao) {
        if (filme == null || amigo == null) {
            throw new IllegalArgumentException("Filme e amigo são obrigatórios.");
        }
        if (dataEmprestimo == null || dataPrevistaDevolucao == null) {
            throw new IllegalArgumentException("As datas são obrigatórias.");
        }
        if (dataPrevistaDevolucao.isBefore(dataEmprestimo)) {
            throw new IllegalArgumentException("A data prevista não pode ser anterior à data do empréstimo.");
        }
        this.filme = filme;
        this.amigo = amigo;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.dataDevolucao = null;
        amigo.adicionarAoHistorico(this);
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
    
    
    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }
 
    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        if (dataPrevistaDevolucao == null || dataPrevistaDevolucao.isBefore(dataEmprestimo)) {
            throw new IllegalArgumentException("Data prevista inválida.");
        }
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public void devolver(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("A data de devolução é obrigatória.");
        }
        if (!isAtivo()) {
            throw new IllegalStateException("Este empréstimo já foi devolvido.");
        }
        if (data.isBefore(dataEmprestimo)) {
            throw new IllegalArgumentException("A devolução não pode ser antes do empréstimo.");
        }
        this.dataDevolucao = data;
    }

    public boolean isAtrasado() {
        return isAtivo() && LocalDateUtils.estaAtrasada(dataPrevistaDevolucao);
    }

    public long diasDeAtraso() {
        if (!isAtrasado()) {
            return 0;
        }
        return LocalDateUtils.diasEntre(dataPrevistaDevolucao, LocalDate.now());
    }

    // O estado é calculado (não guardado) para nunca ficar desatualizado
    public EstadoEmprestimo getEstado() {
        if (!isAtivo()) {
            return EstadoEmprestimo.DEVOLVIDO;
        }
        return isAtrasado() ? EstadoEmprestimo.ATRASADO : EstadoEmprestimo.ATIVO;
    }
    @Override
    public String toString() {
        return "Empréstimo: '" + (filme != null ? filme.getTitulo() : "Desconhecido") +
               "' a " + (amigo != null ? amigo.getNome() : "Desconhecido") +
               " em " + LocalDateUtils.formatar(dataEmprestimo) +
               " | Previsto: " + LocalDateUtils.formatar(dataPrevistaDevolucao) +
               (dataDevolucao != null
                       ? " | Devolvido em: " + LocalDateUtils.formatar(dataDevolucao)
                       : " | [" + getEstado() + "]");
    }
}

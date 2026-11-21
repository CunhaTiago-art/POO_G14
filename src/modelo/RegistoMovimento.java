package modelo;

import java.io.Serializable;
import java.time.LocalDate;
/**
 * Regista uma aquisição (preço, data, local) ou uma perda (motivo, data) de um filme. (Parte 2)
 */

public class RegistoMovimento implements Serializable {
	private static final long serialVersionUID = 1L;

    private TipoMovimento tipo;
    private Filme filme;
    private LocalDate data;
    private double preco;   // só para aquisições
    private String local;   // só para aquisições
    private String motivo;  // só para perdas

    private RegistoMovimento(TipoMovimento tipo, Filme filme, LocalDate data) {
        if (filme == null || data == null) {
            throw new IllegalArgumentException("Filme e data são obrigatórios.");
        }
        this.tipo = tipo;
        this.filme = filme;
        this.data = data;
    }

    public static RegistoMovimento aquisicao(Filme filme, LocalDate data, double preco, String local) {
        if (preco < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }
        RegistoMovimento r = new RegistoMovimento(TipoMovimento.AQUISICAO, filme, data);
        r.preco = preco;
        r.local = local;
        return r;
    }

    public static RegistoMovimento perda(Filme filme, LocalDate data, String motivo) {
        RegistoMovimento r = new RegistoMovimento(TipoMovimento.PERDA, filme, data);
        r.motivo = motivo;
        return r;
    }

    public TipoMovimento getTipo() { return tipo; }
    public Filme getFilme() { return filme; }
    public LocalDate getData() { return data; }
    public double getPreco() { return preco; }
    public String getLocal() { return local; }
    public String getMotivo() { return motivo; }

    @Override
    public String toString() {
        String titulo = filme.getTitulo();
        if (tipo == TipoMovimento.AQUISICAO) {
            return "Aquisição: '" + titulo + "' em " + LocalDateUtils.formatar(data)
                    + " | " + String.format("%.2f", preco) + "€ | Local: " + local;
        }
        return "Perda: '" + titulo + "' em " + LocalDateUtils.formatar(data) + " | Motivo: " + motivo;
    }
}

package modelo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Classe principal do modelo de dados.
 * Implementa Serializable pois a persistência será feita via File e ObjectStreams.
 * Aluno: Tiago Cunha (54272)
 */
public class Filme implements Serializable {
    
    private String titulo;
    private String realizador;
    private int anoLancamento;
    private boolean formatoFisico; // true = DVD/BluRay, false = Digital (mp4, mkv)
    
    private Categoria categoria;
    private Localizacao localizacao;
    
    // Uso obrigatório de LocalDate segundo o enunciado
    private LocalDate dataAquisicao; 
    
    // Atributo para gerir se está emprestado ou não
    private boolean estaEmprestado;

    // Construtor principal
    public Filme(String titulo, String realizador, int anoLancamento, boolean formatoFisico, 
                 Categoria categoria, Localizacao localizacao, LocalDate dataAquisicao) {
        this.titulo = titulo;
        this.realizador = realizador;
        this.anoLancamento = anoLancamento;
        this.formatoFisico = formatoFisico;
        this.categoria = categoria;
        this.localizacao = localizacao;
        this.dataAquisicao = dataAquisicao;
        this.estaEmprestado = false; // Por omissão, quando é adicionado, não está emprestado
    }

    // Getters e Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getRealizador() {
        return realizador;
    }

    public void setRealizador(String realizador) {
        this.realizador = realizador;
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public boolean isFormatoFisico() {
        return formatoFisico;
    }

    public void setFormatoFisico(boolean formatoFisico) {
        this.formatoFisico = formatoFisico;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public LocalDate getDataAquisicao() {
        return dataAquisicao;
    }

    public void setDataAquisicao(LocalDate dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }

    public boolean isEstaEmprestado() {
        return estaEmprestado;
    }

    public void setEstaEmprestado(boolean estaEmprestado) {
        this.estaEmprestado = estaEmprestado;
    }

    @Override
    public String toString() {
        return "Filme: '" + titulo + "' | Categoria: " + categoria + 
               " | Localização: " + localizacao + 
               " | Emprestado: " + (estaEmprestado ? "Sim" : "Não");
    }
}

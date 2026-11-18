package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que gere a coleção de filmes, amigos e empréstimos.
 * Implementa Serializable para permitir a persistência completa dos dados.
 */
public class GestorFilmes implements Serializable {
    
    private List<Filme> meusFilmes;
    private List<Emprestimo> historicoEmprestimos;
    private List<Amigo> meusAmigos;
    private List<RegistoMovimento> movimentos;

    
    public GestorFilmes() {
        this.meusFilmes = new ArrayList<>();
        this.historicoEmprestimos = new ArrayList<>();
        this.meusAmigos = new ArrayList<>();
        this.movimentos = new ArrayList<>();
    }

    // Gestão de Filmes
    public void adicionarFilme(Filme filme) {
        if (filme != null) {
            meusFilmes.add(filme);
        }
    }

    // Gestão de Amigos
    public void registarAmigo(Amigo amigo) {
        if (amigo != null && !meusAmigos.contains(amigo)) {
            meusAmigos.add(amigo);
        }
    }

    // Gestão de Empréstimos
    public boolean emprestarFilme(Filme filme, Amigo amigo) {
        if (filme == null || amigo == null) {
            System.out.println("Filme ou amigo inválido.");
            return false;
        }

        if (filme.isEstaEmprestado()) {
            System.out.println("O filme '" + filme.getTitulo() + "' já se encontra emprestado.");
            return false;
        }

        // Se o amigo ainda não estiver registado, adiciona-o automaticamente
        if (!meusAmigos.contains(amigo)) {
            registarAmigo(amigo);
        }

        filme.setEstaEmprestado(true);
        Emprestimo novoEmprestimo = new Emprestimo(filme, amigo);
        historicoEmprestimos.add(novoEmprestimo);
        return true;
    }

    public boolean devolverFilme(Filme filme) {
        if (filme == null) {
            return false;
        }

        // Procurar o empréstimo ativo correspondente a este filme
        for (Emprestimo e : historicoEmprestimos) {
            if (e.getFilme().equals(filme) && e.isAtivo()) {
                e.setDataDevolucao(LocalDate.now());
                filme.setEstaEmprestado(false);
                return true;
            }
        }

        System.out.println("Não foi encontrado nenhum empréstimo ativo para o filme: " + filme.getTitulo());
        return false;
    }

    // Listagens para consola
    public void listarFilmes() {
        if (meusFilmes.isEmpty()) {
            System.out.println("A tua coleção de filmes está vazia.");
        } else {
            System.out.println("--- Os Meus Filmes ---");
            for (Filme f : meusFilmes) {
                System.out.println(f);
            }
        }
    }

    public void listarAmigos() {
        if (meusAmigos.isEmpty()) {
            System.out.println("Nenhum amigo registado.");
        } else {
            System.out.println("--- Lista de Amigos ---");
            for (Amigo a : meusAmigos) {
                System.out.println(a);
            }
        }
    }

    public void listarHistoricoEmprestimos() {
        if (historicoEmprestimos.isEmpty()) {
            System.out.println("Nenhum registo no histórico de empréstimos.");
        } else {
            System.out.println("--- Histórico de Empréstimos ---");
            for (Emprestimo emp : historicoEmprestimos) {
                System.out.println(emp);
            }
        }
    }

    // empréstimos com prazo, alertas, aquisições e perdas
    public boolean emprestarFilme(Filme filme, Amigo amigo, LocalDate dataPrevistaDevolucao) {
        if (filme == null || amigo == null) {
            System.out.println("Filme ou amigo inválido.");
            return false;
        }
        if (filme.isEstaEmprestado()) {
            System.out.println("O filme '" + filme.getTitulo() + "' já se encontra emprestado.");
            return false;
        }
        if (dataPrevistaDevolucao == null || dataPrevistaDevolucao.isBefore(LocalDate.now())) {
            System.out.println("Data prevista de devolução inválida.");
            return false;
        }
        registarAmigo(amigo);
        filme.setEstaEmprestado(true);
        historicoEmprestimos.add(new Emprestimo(filme, amigo, dataPrevistaDevolucao));
        return true;
    }

    public List<Emprestimo> getEmprestimosAtivos() {
        return EstatisticasEmprestimos.ativos(historicoEmprestimos);
    }

    public List<Emprestimo> getEmprestimosEmAtraso() {
        return EstatisticasEmprestimos.emAtraso(historicoEmprestimos);
    }

    public String getAlertas() {
        return EstatisticasEmprestimos.gerarAlertas(historicoEmprestimos);
    }

    public void registarAquisicao(Filme filme, LocalDate data, double preco, String local) {
        movimentos.add(RegistoMovimento.aquisicao(filme, data, preco, local));
    }

    public void registarPerda(Filme filme, LocalDate data, String motivo) {
        movimentos.add(RegistoMovimento.perda(filme, data, motivo));
    }

    public List<RegistoMovimento> getMovimentos() {
        return movimentos;
    }

    public double getTotalGastoAquisicoes() {
        return EstatisticasEmprestimos.totalGastoAquisicoes(movimentos);
    }

    public void listarMovimentos() {
        if (movimentos.isEmpty()) {
            System.out.println("Nenhuma aquisição ou perda registada.");
        } else {
            System.out.println("--- Aquisições e Perdas ---");
            movimentos.forEach(System.out::println);
        }
    }
    
    // Getters
    public List<Filme> getMeusFilmes() {
        return meusFilmes;
    }

    public List<Emprestimo> getHistoricoEmprestimos() {
        return historicoEmprestimos;
    }

    public List<Amigo> getMeusAmigos() {
        return meusAmigos;
    }
}

package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe central da aplicação (Parte 3).
 * Agrega os filmes, amigos, empréstimos e movimentos (aquisições/perdas)
 * e garante as regras de negócio entre eles.
 *
 * Não escreve nada na consola: quando uma operação não é possível,
 * lança uma exceção com a mensagem de erro. Quem chama (Main ou JavaFX)
 * decide como a mostrar ao utilizador.
 *  - IllegalArgumentException -> os dados recebidos são inválidos
 *  - IllegalStateException    -> a operação não é possível no estado atual
 *
 * Implementa Serializable para ser gravada inteira pelo GestorDados.
 */
public class GestorFilmes implements Serializable {

    private static final long serialVersionUID = 1L;

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

    // =====================================================================
    // Filmes
    // =====================================================================

    public void adicionarFilme(Filme filme) {
        if (filme == null) {
            throw new IllegalArgumentException("O filme é obrigatório.");
        }
        if (meusFilmes.contains(filme)) {
            throw new IllegalStateException("Este filme já está na coleção.");
        }
        if (existeFilme(filme.getTitulo(), filme.getAnoLancamento())) {
            throw new IllegalStateException("Já existe o filme '" + filme.getTitulo()
                    + "' (" + filme.getAnoLancamento() + ") na coleção.");
        }
        meusFilmes.add(filme);
    }

    // Adiciona o filme e regista logo a aquisição (preço e local de compra)
    public void adicionarFilme(Filme filme, double preco, String local) {
        adicionarFilme(filme);
        LocalDate data = filme.getDataAquisicao() != null ? filme.getDataAquisicao() : LocalDate.now();
        movimentos.add(RegistoMovimento.aquisicao(filme, data, preco, local));
    }

    // Remove o filme da coleção. Um filme emprestado não pode ser removido.
    // Os empréstimos e movimentos antigos ficam no histórico.
    public void removerFilme(Filme filme) {
        verificarFilmeNaColecao(filme);
        if (filme.isEstaEmprestado()) {
            throw new IllegalStateException("O filme '" + filme.getTitulo()
                    + "' está emprestado e não pode ser removido.");
        }
        meusFilmes.remove(filme);
    }

    public boolean existeFilme(String titulo, int ano) {
        if (titulo == null) {
            return false;
        }
        for (Filme f : meusFilmes) {
            if (f.getTitulo() != null && f.getTitulo().equalsIgnoreCase(titulo.trim())
                    && f.getAnoLancamento() == ano) {
                return true;
            }
        }
        return false;
    }

    // =====================================================================
    // Pesquisas (devolvem listas novas; a coleção não é alterada)
    // =====================================================================

    public List<Filme> pesquisarPorTitulo(String texto) {
        String procura = normalizar(texto);
        return meusFilmes.stream()
                .filter(f -> normalizar(f.getTitulo()).contains(procura))
                .collect(Collectors.toList());
    }

    public List<Filme> pesquisarPorRealizador(String texto) {
        String procura = normalizar(texto);
        return meusFilmes.stream()
                .filter(f -> normalizar(f.getRealizador()).contains(procura))
                .collect(Collectors.toList());
    }

    public List<Filme> pesquisarPorCategoria(Categoria categoria) {
        return meusFilmes.stream()
                .filter(f -> f.getCategoria() == categoria)
                .collect(Collectors.toList());
    }

    public List<Filme> getFilmesDisponiveis() {
        return meusFilmes.stream()
                .filter(f -> !f.isEstaEmprestado() && !isPerdido(f))
                .collect(Collectors.toList());
    }

    public List<Filme> getFilmesEmprestados() {
        return meusFilmes.stream()
                .filter(Filme::isEstaEmprestado)
                .collect(Collectors.toList());
    }

    public List<Filme> getFilmesPerdidos() {
        return meusFilmes.stream()
                .filter(this::isPerdido)
                .collect(Collectors.toList());
    }

    // Um filme está perdido se existir um movimento de PERDA para ele
    public boolean isPerdido(Filme filme) {
        for (RegistoMovimento m : movimentos) {
            if (m.getTipo() == TipoMovimento.PERDA && m.getFilme() == filme) {
                return true;
            }
        }
        return false;
    }

    // =====================================================================
    // Amigos
    // =====================================================================

    public void registarAmigo(Amigo amigo) {
        if (amigo == null) {
            throw new IllegalArgumentException("O amigo é obrigatório.");
        }
        if (meusAmigos.contains(amigo)) {
            return; // já está registado, não há nada a fazer
        }
        if (procurarAmigo(amigo.getNome()) != null) {
            throw new IllegalStateException("Já existe um amigo com o nome '" + amigo.getNome() + "'.");
        }
        meusAmigos.add(amigo);
    }

    // Um amigo com empréstimos por devolver não pode ser removido
    public void removerAmigo(Amigo amigo) {
        if (amigo == null || !meusAmigos.contains(amigo)) {
            throw new IllegalArgumentException("Esse amigo não está registado.");
        }
        if (amigo.contarEmprestimosAtivos() > 0) {
            throw new IllegalStateException(amigo.getNome() + " ainda tem filmes por devolver.");
        }
        meusAmigos.remove(amigo);
    }

    // Devolve o amigo com esse nome (sem distinguir maiúsculas) ou null
    public Amigo procurarAmigo(String nome) {
        if (nome == null) {
            return null;
        }
        for (Amigo a : meusAmigos) {
            if (a.getNome().equalsIgnoreCase(nome.trim())) {
                return a;
            }
        }
        return null;
    }

    // =====================================================================
    // Empréstimos
    // =====================================================================

    // Empréstimo com o prazo por omissão (14 dias)
    public void emprestarFilme(Filme filme, Amigo amigo) {
        emprestarFilme(filme, amigo, LocalDate.now().plusDays(Emprestimo.PRAZO_POR_DEFEITO_DIAS));
    }

    // Empréstimo feito hoje, com data prevista de devolução escolhida
    public void emprestarFilme(Filme filme, Amigo amigo, LocalDate dataPrevistaDevolucao) {
        if (dataPrevistaDevolucao == null || dataPrevistaDevolucao.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data prevista de devolução não pode ser anterior a hoje.");
        }
        emprestarFilme(filme, amigo, LocalDate.now(), dataPrevistaDevolucao);
    }

    // Empréstimo com todas as datas escolhidas.
    // Permite registar um empréstimo feito no passado (ex.: antes de usar a aplicação).
    public void emprestarFilme(Filme filme, Amigo amigo, LocalDate dataEmprestimo, LocalDate dataPrevistaDevolucao) {
        verificarFilmeNaColecao(filme);
        if (amigo == null) {
            throw new IllegalArgumentException("O amigo é obrigatório.");
        }
        if (dataEmprestimo == null || dataEmprestimo.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data do empréstimo não pode ser no futuro.");
        }
        if (filme.isEstaEmprestado()) {
            throw new IllegalStateException("O filme '" + filme.getTitulo() + "' já se encontra emprestado.");
        }
        if (isPerdido(filme)) {
            throw new IllegalStateException("O filme '" + filme.getTitulo() + "' está registado como perdido.");
        }

        registarAmigo(amigo); // se ainda não estiver registado, fica registado

        // O construtor do Emprestimo valida que a data prevista não é anterior à do empréstimo
        Emprestimo novo = new Emprestimo(filme, amigo, dataEmprestimo, dataPrevistaDevolucao);
        historicoEmprestimos.add(novo);
        filme.setEstaEmprestado(true);
    }

    // Devolução com a data de hoje
    public void devolverFilme(Filme filme) {
        devolverFilme(filme, LocalDate.now());
    }

    public void devolverFilme(Filme filme, LocalDate data) {
        Emprestimo ativo = getEmprestimoAtivo(filme);
        if (ativo == null) {
            throw new IllegalStateException("O filme '" + (filme != null ? filme.getTitulo() : "?")
                    + "' não está emprestado.");
        }
        ativo.devolver(data); // valida a data e fecha o empréstimo
        filme.setEstaEmprestado(false);
    }

    // Devolve o empréstimo por devolver desse filme, ou null se não houver
    public Emprestimo getEmprestimoAtivo(Filme filme) {
        for (Emprestimo e : historicoEmprestimos) {
            if (e.getFilme() == filme && e.isAtivo()) {
                return e;
            }
        }
        return null;
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

    public Amigo getAmigoComMaisEmprestimos() {
        return EstatisticasEmprestimos.amigoComMaisEmprestimos(historicoEmprestimos);
    }

    // =====================================================================
    // Aquisições e perdas
    // =====================================================================

    public void registarAquisicao(Filme filme, LocalDate data, double preco, String local) {
        verificarFilmeNaColecao(filme);
        movimentos.add(RegistoMovimento.aquisicao(filme, data, preco, local));
    }

    // Se o filme estava emprestado (o amigo perdeu-o), o empréstimo é fechado
    // na data da perda, para não ficar eternamente "em atraso".
    public void registarPerda(Filme filme, LocalDate data, String motivo) {
        verificarFilmeNaColecao(filme);
        if (isPerdido(filme)) {
            throw new IllegalStateException("O filme '" + filme.getTitulo() + "' já está registado como perdido.");
        }
        RegistoMovimento perda = RegistoMovimento.perda(filme, data, motivo); // valida a data
        Emprestimo ativo = getEmprestimoAtivo(filme);
        if (ativo != null) {
            ativo.devolver(data);
            filme.setEstaEmprestado(false);
        }
        movimentos.add(perda);
    }

    public double getTotalGastoAquisicoes() {
        return EstatisticasEmprestimos.totalGastoAquisicoes(movimentos);
    }

    // =====================================================================
    // Getters (listas só de leitura: alterações têm de passar pelo gestor)
    // =====================================================================

    public List<Filme> getMeusFilmes() {
        return Collections.unmodifiableList(meusFilmes);
    }

    public List<Emprestimo> getHistoricoEmprestimos() {
        return Collections.unmodifiableList(historicoEmprestimos);
    }

    public List<Amigo> getMeusAmigos() {
        return Collections.unmodifiableList(meusAmigos);
    }

    public List<RegistoMovimento> getMovimentos() {
        return Collections.unmodifiableList(movimentos);
    }

    // =====================================================================
    // Métodos auxiliares (privados)
    // =====================================================================

    private void verificarFilmeNaColecao(Filme filme) {
        if (filme == null) {
            throw new IllegalArgumentException("O filme é obrigatório.");
        }
        if (!meusFilmes.contains(filme)) {
            throw new IllegalArgumentException("O filme '" + filme.getTitulo() + "' não pertence à coleção.");
        }
    }

    // Para pesquisas: ignora maiúsculas/minúsculas e espaços nas pontas
    private static String normalizar(String texto) {
        return texto == null ? "" : texto.trim().toLowerCase();
    }
}
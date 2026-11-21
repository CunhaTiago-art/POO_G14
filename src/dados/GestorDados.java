package dados;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import modelo.GestorFilmes;

/**
 * Persistência dos dados da aplicação (Parte 3).
 * Grava e carrega o GestorFilmes inteiro num ficheiro binário,
 * usando File, ObjectOutputStream e ObjectInputStream.
 *
 * O gestor é gravado com um único writeObject: assim, o mesmo Filme
 * referido em várias listas (filmes, empréstimos, movimentos) continua
 * a ser um único objeto depois de carregado.
 */
public class GestorDados {

    public static final String NOME_FICHEIRO = "filmes.dat";

    private final File ficheiro;

    // Usa o ficheiro por omissão, na pasta onde o programa corre
    public GestorDados() {
        this(new File(NOME_FICHEIRO));
    }

    // Permite escolher outro ficheiro (útil para testes ou cópias de segurança)
    public GestorDados(File ficheiro) {
        if (ficheiro == null) {
            throw new IllegalArgumentException("O ficheiro é obrigatório.");
        }
        this.ficheiro = ficheiro;
    }

    /**
     * Grava o gestor no ficheiro (substitui o conteúdo anterior).
     */
    public void guardar(GestorFilmes gestor) throws IOException {
        if (gestor == null) {
            throw new IllegalArgumentException("Não há dados para guardar.");
        }
        // try-with-resources: a stream é fechada automaticamente, mesmo que haja erro
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheiro))) {
            oos.writeObject(gestor);
        }
    }

    /**
     * Lê o gestor do ficheiro.
     * Se o ficheiro ainda não existir (primeira execução), devolve um gestor vazio.
     */
    public GestorFilmes carregar() throws IOException, ClassNotFoundException {
        if (!ficheiro.exists()) {
            return new GestorFilmes();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheiro))) {
            return (GestorFilmes) ois.readObject();
        }
    }

    public boolean existeFicheiro() {
        return ficheiro.exists();
    }

    public File getFicheiro() {
        return ficheiro;
    }
}

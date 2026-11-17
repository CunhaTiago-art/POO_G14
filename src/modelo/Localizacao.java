package modelo;

import java.io.Serializable;

/**
 * Representa a localização física ou digital de um filme.
 * Implementa Serializable para permitir a gravação em ObjectStreams.
 * Aluno: Tiago Cunha (54272)
 */
public class Localizacao implements Serializable {
    
    private String espaco; // ex: "Sala", "Quarto", "Disco Externo 1"
    private String detalhe; // ex: "Prateleira 3", "Pasta Filmes/2026"

    public Localizacao(String espaco, String detalhe) {
        this.espaco = espaco;
        this.detalhe = detalhe;
    }

    // Getters e Setters
    public String getEspaco() { return espaco; }
    public void setEspaco(String espaco) { this.espaco = espaco; }

    public String getDetalhe() { return detalhe; }
    public void setDetalhe(String detalhe) { this.detalhe = detalhe; }

    @Override
    public String toString() {
        return espaco + " (" + detalhe + ")";
    }
}

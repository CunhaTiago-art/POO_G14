package modelo;

import java.io.Serializable;

/**
 * Representa um amigo a quem se podem emprestar filmes.
 * Implementa Serializable para permitir a persistência.
 */
public class Amigo implements Serializable {
    
    private String nome;
    private String contacto;

    public Amigo(String nome, String contacto) {
        this.nome = nome;
        this.contacto = contacto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    @Override
    public String toString() {
        return nome + " (Contacto: " + contacto + ")";
    }
}

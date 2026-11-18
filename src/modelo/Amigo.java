package modelo;


import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Representa um amigo a quem se podem emprestar filmes.
 * Implementa Serializable para permitir a persistência.
 */
public class Amigo implements Serializable {
	
   private static final long serialVersionUID = 1L;
   
    private String nome;
    private String contacto;
    private String email;
    private String telefone;
    private List<Emprestimo> historico = new ArrayList<>();

    public Amigo(String nome, String contacto) {
        this(nome, contacto, null, null);
   
    }
    public Amigo(String nome, String contacto, String email, String telefone) {
        setNome(nome);
        this.contacto = contacto;
        setEmail(email);
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
    	 if (nome == null || nome.isBlank()) {
             throw new IllegalArgumentException("O nome do amigo é obrigatório.");
         }
         this.nome = nome.trim();
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    
    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && !email.isBlank() && !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido.");
        }
        this.email = email;
    }

    public void adicionarAoHistorico(Emprestimo e) {
        if (e != null && !historico.contains(e)) {
            historico.add(e);
        }
    }

    public List<Emprestimo> getHistorico() {
        return new ArrayList<>(historico);
    }

    public long contarEmprestimosAtivos() {
        return historico.stream().filter(Emprestimo::isAtivo).count();
    }

    public long contarEmprestimosTotais() {
        return historico.size();
    }

    @Override
    public String toString() {
        return nome + " (Contacto: " + contacto + ")";
    }
    
}

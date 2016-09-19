package classesBD;

/**
 * Created by JOAO on 07/06/2016.
 */
public class Usuario {

    private String nomeUsuario;
    private String cpfUsuario;
    private String cidade;
    private String estado;
    private String email;
    private String senha;

    public Usuario() {
    }

    public Usuario(String nomeUsuario, String cpfUsuario, String cidade, String estado, String email, String senha) {

        this.nomeUsuario = nomeUsuario;
        this.cpfUsuario = cpfUsuario;
        this.cidade = cidade;
        this.estado = estado;
        this.email = email;
        this.senha = senha;
    }

    public String getCidade() {
        return cidade;
    }

    public String getCpfUsuario() {
        return cpfUsuario;
    }

    public String getEmail() {
        return email;
    }

    public String getEstado() {
        return estado;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setCpfUsuario(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}


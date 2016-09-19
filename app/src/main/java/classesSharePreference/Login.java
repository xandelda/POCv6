package classesSharePreference;

/**
 * Created by JOAO on 07/06/2016.
 */
public class Login {

    private String email;
    private String senha;
    private int id;


    public Login(int id,String email, String senha){
        this.id = id;
        this.email = email;
        this.senha = senha;
            }

    public Login(){

    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }




}

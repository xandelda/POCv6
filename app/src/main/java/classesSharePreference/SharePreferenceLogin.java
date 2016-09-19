package classesSharePreference;

import android.content.SharedPreferences;

/**
 * Created by JOAO on 08/05/2016.
 */
public class SharePreferenceLogin {


    public static final String MyPREFERENCES = "Logon";
    public static final String IDPerfil = "id";
    public static final String Email = "email";
    public static final String Senha = "senha";
    public SharedPreferences shared;
    public SharedPreferences.Editor editor;



    public SharePreferenceLogin(){
        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void salvarShared(Login u){
        this.editor = this.shared.edit();

        editor.putInt(IDPerfil, u.getId());
        editor.putString(Senha, u.getSenha());
        editor.putString(Email, u.getEmail());
        editor.commit();
    }

    public void lerDados (Login u){

        u.setEmail(this.shared.getString(Email,""));
        u.setSenha(this.shared.getString(Senha,""));
        u.setId(this.shared.getInt(IDPerfil,0));
    }


}

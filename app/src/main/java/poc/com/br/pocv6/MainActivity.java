package poc.com.br.pocv6;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import classesSharePreference.Login;
import classesSharePreference.SharePreferenceLogin;

public class MainActivity extends AppCompatActivity {

    private Button btFeedEntrada, btJuntese, btQuemSomos;

    private static final String MyPREFERENCES = "Logon";
    private static final String IDPerfil = "id";
    private static final String Email = "email";
    private static final String Senha = "senha";
    private SharedPreferences sharedPreferences;

    private SharePreferenceLogin shL = new SharePreferenceLogin();
    private Login login = new Login();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // this.sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        this.shL.shared = getSharedPreferences(this.shL.MyPREFERENCES, Context.MODE_PRIVATE);

        shL.lerDados(login);

        if (login.getId()>0){
            startActivity(new Intent(MainActivity.this,MenuActivity.class));
            finish();
        }

        setContentView(R.layout.activity_main);

        this.btJuntese = (Button) findViewById(R.id.btJuntese);
        this.btFeedEntrada = (Button) findViewById(R.id.btFeedEntrada);
        this.btQuemSomos = (Button) findViewById(R.id.btQuemSomos);

        btFeedEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Função não programada.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,MenuActivity.class));
            }
        });

        btJuntese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                //Toast.makeText(MainActivity.this, "Função não programada.", Toast.LENGTH_SHORT).show();
            }
        });

        btQuemSomos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Função não programada.", Toast.LENGTH_SHORT).show();
               startActivity(new Intent(MainActivity.this,QuemSomosActivity.class));
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


}

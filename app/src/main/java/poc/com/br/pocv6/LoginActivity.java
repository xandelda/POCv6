package poc.com.br.pocv6;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import classesBD.Usuario;
import classesJson.AcessoREST;
import classesSharePreference.Login;
import classesSharePreference.SharePreferenceLogin;

public class LoginActivity extends AppCompatActivity {

        private Button btEntrar, btCadastrar;
        private EditText edtEmail, edtSenha;

        private Gson g = new Gson();

        private ProgressDialog progressDialog;

        private AcessoREST ar = new AcessoREST();
        private String retorno;

        private SharePreferenceLogin sp = new SharePreferenceLogin();
        private Login login = new Login();
        private boolean aux;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            setContentView(R.layout.activity_login);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            this.sp.shared = getSharedPreferences(this.sp.MyPREFERENCES, Context.MODE_PRIVATE);


            this.btCadastrar = (Button) findViewById(R.id.btCadastrar);
            this.btEntrar = (Button) findViewById(R.id.btEntrar);

            this.edtEmail = (EditText) findViewById(R.id.edtEmail);
            this.edtSenha = (EditText) findViewById(R.id.edtSenha);



            btEntrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(LoginActivity.this,"Função não programada.",Toast.LENGTH_SHORT).show();
                    login.setEmail(edtEmail.getText().toString());
                    login.setSenha(edtSenha.getText().toString());
                    chamaServidor();
                }
            });

            btCadastrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(LoginActivity.this,"Função não programada.",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,CadastrarActivity.class));
                }
            });


        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            int id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }


    public void chamaServidor(){

        progressDialog = ProgressDialog.show(this, "",
                "Enviando dados. Aguarde um momento...", true);

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                // do the thing that takes a long time
                try {
                    dadosServidor();


                } catch (Exception e) {
                    e.printStackTrace();
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        progressDialog.dismiss();
                        if (!retorno.equals("Falha no POST")){
                            if(!retorno.equals("[]")) {
                                Log.i("POC", "Entrou aqui");
                                Log.i("POC", "" + login.getId());
                                alertMenseger("Pronto. Você agora tem acesso completo.", "Sucesso", null, "Continuar.", null, LoginActivity.this, 1);
                            }else{
                                alertMenseger("Verifique se seu email e senha foi informado corretamente.","Aviso",null,null,"OK, Entendi.",LoginActivity.this,2);
                            }
                        }else{
                            alertMenseger("Houve alguma falha de comunicação.","Aviso",null,null,"OK, Entendi.",LoginActivity.this,3);
                        }

                    }
                });
            }
        }).start();

    }



    public void dadosServidor(){
        String url = "http://192.168.137.1:8080/WebServicePOC/webresources/wbspoc/Usuario/login";
        retorno = ar.chamadaPOST(url,g.toJson(login));
        Log.i("POC","LOGIN: "+retorno);
        if (!retorno.equals("Falha no POST")){
            Log.i("POC","Entrou no dadsoServidor");
            Log.i("POC","RETORNO: "+retorno.equals("[]"));
            Login l = (Login) g.fromJson(retorno,Login.class);
            login.setId(l.getId());
            Log.i("POC","EMAIL: "+login.getEmail()+"\nSENHA: "+login.getSenha()+"\nID: "+login.getId());
            login.setEmail(edtEmail.getText().toString());
            login.setSenha(edtSenha.getText().toString());
            sp.salvarShared(login);
        }

        Log.i("POC","EMAIL: "+login.getEmail()+"\nSENHA: "+login.getSenha()+"\nID: "+login.getId());


    }








    private void alertMenseger(String menseger, String title, String buttonNeutral, String buttonPositive,
                               String buttonNegative, Context context, int opc) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle(title);
        alert.setMessage(menseger);

        if (opc == 1) {
            alert.setIcon(R.mipmap.opa_sim);
            alert.setPositiveButton(buttonPositive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(LoginActivity.this,MenuActivity.class));
                    finish();
                }
            });
        } else if (opc == 2) {
            alert.setIcon(R.mipmap.opa_mais_menos);
            alert.setNegativeButton(buttonNegative, null);
        } else if (opc == 3) {
            alert.setIcon(R.mipmap.opa);
            alert.setNegativeButton(buttonNegative, null);
        } else {
            alert.setIcon(R.mipmap.opa);
            alert.setPositiveButton(buttonPositive, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }
            });
            //define um botão como negativo.
            alert.setNegativeButton(buttonNegative, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
        }

        alert.show();
    }

}



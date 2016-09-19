package poc.com.br.pocv6;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import classesJson.AcessoREST;
import classesSharePreference.Login;
import classesSharePreference.SharePreferenceLogin;

import static java.lang.Thread.sleep;

public class MenuActivity extends AppCompatActivity {

    private TextView txtMenu,txtMenuSobreVoce;
    private ProgressDialog progressDialog;
    private AcessoREST aR = new AcessoREST();
    private String retorno;

    private SharePreferenceLogin sp = new SharePreferenceLogin();
    private Login login = new Login();

    private TextView txtQuemSomos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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

        this.sp.lerDados(login);
        // Necessário para requisições Http
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //if (login.getId()>0){
                   startActivity(new Intent(MenuActivity.this,ObjetoEleitorActivity.class));
               //}else{
                  // alertMenseger("Você ainda não está cadastrado","Aviso",null,null,"Me Cadastrar",MenuActivity.this,2);
               //}

            }
        });

        this.txtMenu = (TextView) findViewById(R.id.txtMenu);
        this.txtMenuSobreVoce = (TextView) findViewById(R.id.txtMenuSobreVoce);


        this.txtQuemSomos = (TextView) findViewById(R.id.txtQuemSomos);

        this.txtQuemSomos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this,QuemSomosActivity.class));
            }
        });

        //String retorno = aR.chamadaGET("http://192.168.1.2:8080/WebServicePOC/webresources/wbspoc/Usuario/selectUsuario");
        //String retorno = aR.chamadaGET("http://192.168.137.1:8080/WebServicePOC/webresources/wbspoc/Usuario/selectUsuario");
        //String retorno = aR.chamadaGET("http://192.168.1.6:8080/WebServicePOC/webresources/wbspoc/Usuario/selectUsuario");
        //launchRingDialog();

       // txtMenu.setText(retorno);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchRingDialog() {

        progressDialog = ProgressDialog.show(MenuActivity.this, "", "Cerragando dados. Aguarde um momento...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //sleep(10000);
                    retorno = aR.chamadaGET("http://192.168.137.1:8080/WebServicePOC/webresources/wbspoc/Usuario/selectUsuario");
                    //retorno = aR.chamadaGET("http://192.168.1.2:8080/WebServicePOC/webresources/wbspoc/Usuario/selectUsuario");
                } catch (Exception e) {
                    Log.e("POC", "Falha no GET: selectUsuario"+e.getMessage());
                }
                progressDialog.dismiss();
            }

        }).start();

    }

    private void alertMenseger(String menseger, String title, String buttonNeutral, String buttonPositive,
                               String buttonNegative, Context context, int opc) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle(title);
        alert.setMessage(menseger);

        if (opc == 1) {
            alert.setIcon(R.mipmap.opa_sim);
            alert.setPositiveButton(buttonPositive, null);
        } else if (opc == 2) {
            alert.setIcon(R.mipmap.opa_mais_menos);
            alert.setNegativeButton(buttonNegative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(MenuActivity.this,LoginActivity.class));
                }
            });
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

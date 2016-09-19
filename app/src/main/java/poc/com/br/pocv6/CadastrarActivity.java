package poc.com.br.pocv6;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import classesBD.Usuario;
import classesJson.AcessoREST;
import classesSharePreference.Login;
import classesSharePreference.SharePreferenceLogin;

public class CadastrarActivity extends AppCompatActivity {

    private Button btSalvarDados, btCancelarCadastro;
    private EditText edtEmail, edtNome, edtCPF, edtEstado, edtCidade, edtSenha;
    private ImageButton btSelecionar;

    private SharePreferenceLogin sp = new SharePreferenceLogin();
    private Login login = new Login();

    private AlertDialog alerta;

    private AcessoREST ar = new AcessoREST();

    private Usuario u = new Usuario();
    private Gson g = new Gson();

    private ProgressDialog progressDialog;

    private boolean aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_cadastrar);
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

        final Intent intent = new Intent(this, EstadosActivity.class);

        this.btSalvarDados = (Button) findViewById(R.id.btSalvarDados);
        this.btCancelarCadastro = (Button) findViewById(R.id.btCancelarCadastro);

        this.edtEmail = (EditText) findViewById(R.id.edtEmail);
        this.edtNome = (EditText) findViewById(R.id.edtNome);
        this.edtCPF = (EditText) findViewById(R.id.edtCPF);
        this.edtEstado = (EditText) findViewById(R.id.edtEstado);
        this.edtCidade = (EditText) findViewById(R.id.edtCidade);
        this.edtSenha = (EditText) findViewById(R.id.edtSenha);

        this.btSelecionar = (ImageButton) findViewById(R.id.btSelecionar);


        btSalvarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validaEdt()) {
                    if (isCPF(edtCPF.getText().toString())) {
                        if (isEmailValid(edtEmail.getText().toString())) {
                            if (isSenhaValid(edtSenha.getText().toString())) {
                                u.setNomeUsuario(edtNome.getText().toString());
                                u.setCpfUsuario(edtCPF.getText().toString());
                                u.setCidade(edtCidade.getText().toString());
                                u.setEstado(edtEstado.getText().toString());
                                u.setEmail(edtEmail.getText().toString());
                                u.setSenha(edtSenha.getText().toString());


                                //launchRingDialog();
                               /* //String url = "http://192.168.1.2:8080/WebServicePOC/webresources/wbspoc/Usuario/inserirUsuario";
                                String url = "http://192.168.137.1:8080/WebServicePOC/webresources/wbspoc/Usuario/inserirUsuario";
                                String json = g.toJson(u);

                                String retorno = ar.chamadaPOST(url,json);

                                Log.i("POC",retorno);
                                */

                               chamaServidor();


                            } else {
                                alertMenseger("Verifique sua senha: não é permitido espaços em branco. ", "Vamos rever alguns dados.",
                                        "OK, Entendi.", "", "", CadastrarActivity.this, 2);
                            }

                        } else {
                            alertMenseger("Confira se seu email está correto. ", "Vamos rever alguns dados.",
                                    "OK, Entendi.", "", "", CadastrarActivity.this, 2);
                        }
                    } else {
                        alertMenseger("Confira se seu CPF está correto. ", "Vamos rever alguns dados.",
                                "OK, Entendi.", "", "", CadastrarActivity.this, 2);
                    }
                } else {
                    alertMenseger("Confira se ficou alguma informação em branco. ", "Vamos rever alguns dados.",
                            "OK, Entendi.", "", "", CadastrarActivity.this, 2);
                }

            }
        });

        btCancelarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertMenseger("Tem certeza que deseja desistir do seu cadastro?","Aviso",null,"Sim, sair.","Não, continuar.",CadastrarActivity.this,4);
            }
        });

        btSelecionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, 1);
            }
        });
    }





   /* private void launchRingDialog() {




            progressDialog = ProgressDialog.show(CadastrarActivity.this, "", "Enviando dados. Aguarde um momento...");
            new Thread(new Runnable() {
               @Override
                public void run() {
                    try {
                        carregaDados();
                    } catch (Exception e) {
                        Log.e("POC", "Thread: " + e.getMessage());
                    }*/

                 /*   //Exibe mensagem apenas informando o fim da execução da thread
                    mhandler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Acabou", Toast.LENGTH_LONG).show();
                        }
                    });


                 //  if (aux){
                     //  progressDialog.dismiss();
                   //}else{
                       //alertMenseger("Falha na comunicação com o servidor","Ocorreu um Erro",null,null,"OK",CadastrarActivity.this,3);
                      // Toast.makeText(CadastrarActivity.this,"Falha na comunicação com o servidor",Toast.LENGTH_SHORT);
                       //progressDialog.dismiss();
                  // }

                }
           }).start();


            //carregaDados();



    }*/





    public void chamaServidor(){
        progressDialog = ProgressDialog.show(this, "",
                "Enviando dados. Aguarde um momento...", true);

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                // do the thing that takes a long time
                try {
                    //dadosServidor();
                    carregaDados();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {

                        progressDialog.dismiss();
                        if (aux){
                            alertMenseger("Pronto. Agora você está cadastrado.","Sucesso",null,"Continuar.",null,CadastrarActivity.this,1);
                        }else{
                            alertMenseger("Houve alguma falha.\n\nVerique se os dados estão corretos ou a sua conexão.","Aviso",null,null,"OK, Entendi.",CadastrarActivity.this,3);
                        }


                    }
                });
            }
        }).start();
    }


    public void dadosServidor(){
        String url = "http://192.168.137.1:8080/WebServicePOC/webresources/wbspoc/Usuario/inserirUsuario";

        String retorno = ar.chamadaPOST(url,g.toJson(u));
        Log.i("POC","R LOGIN: "+retorno);
    }


    private void carregaDados(){
        String url = "http://192.168.137.1/WebServicePOC/webresources/wbspoc/Usuario/inserirUsuario";
        //String url = "http://192.168.1.2:8080/WebServicePOC/webresources/wbspoc/Usuario/inserirUsuario";
        //String url = "http://192.168.1.6:8080/WebServicePOC/webresources/wbspoc/Usuario/inserirUsuario";
        String retorno = ar.chamadaPOST(url,g.toJson(u));
        Log.i("POC","R LOGIN: "+retorno);

        this.aux = Boolean.parseBoolean(retorno);

        if (aux) {
            String url2 = "http://192.168.137.1:8080/WebServicePOC/webresources/wbspoc/Usuario/selectUsuarioID";
            //String url2 = "http://192.168.1.2:8080/WebServicePOC/webresources/wbspoc/Usuario/selectUsuarioID";
            //String url2 = "http://192.168.1.6:8080/WebServicePOC/webresources/wbspoc/Usuario/selectUsuarioID";
            String r2 = ar.chamadaGET(url2);
            Log.i("POC", "Pasosu daqui");

            login.setId(Integer.parseInt(r2));
            login.setEmail(u.getEmail());
            login.setSenha(u.getSenha());

            sp.salvarShared(login);

            Log.i("POC", "ID: " + login.getId());

            Log.i("POC", "-> " + String.valueOf(aux));
            //progressDialog.dismiss();
        }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            String countryName = data.getStringExtra(EstadosActivity.RESULT_CONTRYNAME);
            String countryCode = data.getStringExtra(EstadosActivity.RESULT_CONTRYCODE);
            Toast.makeText(CadastrarActivity.this,countryName,Toast.LENGTH_SHORT).show();
            edtEstado.setText(countryName);
            switch (Integer.parseInt(countryCode)){
                case 1:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.acre,0,0,0);
                    break;
                case 2:
                    Log.i("POC",countryCode);
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.alagoas,0,0,0);
                    break;
                case 3:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.amapa,0,0,0);
                    break;
                case 4:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.amazonas,0,0,0);
                    break;
                case 5:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bahia,0,0,0);
                    break;
                case 6:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ceara,0,0,0);
                    break;
                case 7:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.distrito_federal,0,0,0);
                    break;
                case 8:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.espirito_santo,0,0,0);
                    break;
                case 9:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.goias,0,0,0);
                    break;
                //------------//
                case 10:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.maranhao,0,0,0);
                    break;
                case 11:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mato_grosso,0,0,0);
                    break;
                case 12:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mato_grosso_do_sul,0,0,0);
                    break;
                case 13:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.minas_gerais,0,0,0);
                    break;
                case 14:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.para,0,0,0);
                    break;
                case 15:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.paraiba,0,0,0);
                    break;
                case 16:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.parana,0,0,0);
                    break;
                case 17:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pernambuco,0,0,0);
                    break;
                case 18:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.piaui,0,0,0);
                    break;
                case 19:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rio_de_janeiro,0,0,0);
                    break;
                //-----------//
                case 20:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rio_grande_do_nort,0,0,0);
                    break;
                case 21:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rio_grande_do_sul,0,0,0);
                    break;
                case 22:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rondonia,0,0,0);
                    break;
                case 23:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.roraima,0,0,0);
                    break;
                case 24:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.santa_catarina,0,0,0);
                    break;
                case 25:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sao_paulo,0,0,0);
                    break;
                case 26:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sergipe,0,0,0);
                    break;
                case 27:
                    edtEstado.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tocantins,0,0,0);
                    break;
            }

            edtEstado.setTextColor(getResources().getColor(R.color.colorBlack));
        }
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
                    startActivity(new Intent(CadastrarActivity.this,MenuActivity.class));
                    finish();

                }
            });
        } else if (opc==2) {
            alert.setIcon(R.mipmap.opa_mais_menos);
            alert.setPositiveButton(buttonNeutral, null);
        }else if (opc==3){
            alert.setIcon(R.mipmap.opa);
            alert.setNegativeButton(buttonNegative, null);
        }else{
            alert.setIcon(R.mipmap.opa);
            alert.setPositiveButton(buttonPositive, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    //onStop();
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






//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------//

    private boolean validaEdt(){

        if(edtNome.getText().toString().trim().equals(""))
            return false;
        else if(edtCPF.getText().toString().trim().equals(""))
            return false;
        else if(edtEstado.getText().toString().trim().equals(""))
            return false;
        else if(edtEmail.getText().toString().trim().equals(""))
            return false;
        else if(edtSenha.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }



//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------//
    private boolean isCPF(String CPF) {
        if (CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222")
                || CPF.equals("33333333333") || CPF.equals("44444444444") || CPF.equals("55555555555")
                || CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888")
                || CPF.equals("99999999999") || (CPF.length() != 11)) {
            return (false);
        }
        char dig10, dig11; int sm, i, r, num, peso;
        try {
            sm = 0; peso = 10;
            for (i = 0; i < 9; i++) {
                num = (int) (CPF.charAt(i) - 48); sm = sm + (num * peso); peso = peso - 1;
            }
            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48);
            }
            sm = 0; peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48); sm = sm + (num * peso); peso = peso - 1;
            }
            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) {
                return (true);
            } else {
                return (false);
            }
        } catch (InputMismatchException erro) {
            return (false);
        }
    }
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------//

    private static boolean isEmailValid(String email) {
        if ((email == null) || (email.trim().length() == 0))
            return false;

        String emailPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+" +
                "(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|" +
                "(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";
        Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------//

    private boolean isSenhaValid(String senha){
        if (senha.contains(" ")){
            return false;
        }else{
            return true;
        }
    }


//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------//


}

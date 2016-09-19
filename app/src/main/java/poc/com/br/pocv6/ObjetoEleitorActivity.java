package poc.com.br.pocv6;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import classesAuxiliaresApp.DadosMapa;
import classesBD.Foto;
import classesBD.ObjetoEleitor;
import classesBD.ObjetoEleitor3;
import classesBD.Politico;
import classesBD.TipoObjeto;
import classesJson.AcessoREST;
import classesSharePreference.Mapa;
import classesSharePreference.SharePreferenceMaps;

public class ObjetoEleitorActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ImageView ivFoto1, ivFoto2, ivFoto3, ivFoto4;
    private Bitmap ft1=null, ft2=null, ft3=null, ft4=null;
    private ImageButton btSelecionarPartido, btSelecionarCargo;
    private EditText edtPartido, edtCargo,edtNomeObjEleitor,edtNomePolitico,edtComentario;
    private Button btAutoLocalizacao, btLocalizacaoMapa, btSalvarDadosObjEleitor, btCancelarObjEleitor;
    private TextView txtReferencia, addFoto;

    private AcessoREST aR = new AcessoREST();
    private Gson g = new Gson();

    private TipoObjeto tp = new TipoObjeto();
    private Politico p = new Politico();
    private ObjetoEleitor3 objetoEleitor = new ObjetoEleitor3();

    private GoogleApiClient mGoogleApiClient;

    private double latitude, longitude;
    private Address endereco;
    private DadosMapa dm = new DadosMapa();
    private Mapa m = new Mapa();
    private SharePreferenceMaps spM = new SharePreferenceMaps();

    private ProgressDialog progressDialog;

    private Foto f1 = new Foto();
    private Foto f2 = new Foto();
    private Foto f3 = new Foto();
    private Foto f4 = new Foto();

    //variavel de apoio à thread
    private int count=0,count2=1,count3=0;

    private boolean aux;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_objeto_eleitor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Necessário para requisições Http
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        this.spM.shared2 = getSharedPreferences(this.spM.MyPREFERENCES, Context.MODE_PRIVATE);

        final Intent intentPartidos = new Intent(this, PartidosActivity.class);
        final Intent intentCargo = new Intent(this, CargoActivity.class);

        this.ivFoto1 = (ImageView) findViewById(R.id.ivFoto1);
        this.ivFoto2 = (ImageView) findViewById(R.id.ivFoto2);
        this.ivFoto3 = (ImageView) findViewById(R.id.ivFoto3);
        this.ivFoto4 = (ImageView) findViewById(R.id.ivFoto4);

        this.btSelecionarPartido = (ImageButton) findViewById(R.id.btSelecionarPartido);
        this.btSelecionarCargo = (ImageButton) findViewById(R.id.btSelecionarCargo);

        this.edtPartido = (EditText) findViewById(R.id.edtPartido);
        this.edtCargo = (EditText) findViewById(R.id.edtCargo);
        this.edtNomeObjEleitor = (EditText) findViewById(R.id.edtNomeObjEleitor);
        this.edtNomePolitico = (EditText) findViewById(R.id.edtNomePolitico);
        this.edtComentario = (EditText) findViewById(R.id.edtComentario);

        this.btAutoLocalizacao = (Button) findViewById(R.id.btAutoLocalizacao);
        this.btLocalizacaoMapa = (Button) findViewById(R.id.btLocalizacaoMapa);
        this.btCancelarObjEleitor = (Button) findViewById(R.id.btCancelarObjEleitor);
        this.btSalvarDadosObjEleitor = (Button) findViewById(R.id.btSalvarDadosObjEleitor);

        this.txtReferencia = (TextView) findViewById(R.id.txtReferencia);
        this.addFoto = (TextView) findViewById(R.id.addFoto);


        this.btAutoLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //txtReferencia.setText("carregando dados...");
                //callConnection();
                launchRingDialog(2);
                //m.setLatitude(latitude);
                //m.setLongitude(longitude);
                mostrarEndereco(m,dm,txtReferencia);
                Log.i("POC","AUTO L. LAT: "+m.getLatitude()+" | LONG: "+m.getLatitude());

            }
        });

        this.btLocalizacaoMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // launchRingDialog(2);
                //callConnection();
                callConnection();

                if (latitude!=0 || longitude!=0) {
                    Intent intent = new Intent(v.getContext(), MapsActivity.class);
                    Bundle params = new Bundle();

                    params.putString("Latitude", String.valueOf(latitude));
                    params.putString("Longitude", String.valueOf(longitude));
                    intent.putExtras(params);
                    Log.i("POC", "OBJ - LAT: " + latitude + "\nLONG: " + longitude);
                    startActivity(intent);
                }else{
                    count++;
                    if (count<2)
                        alertMenseger("Verifique sua conexão à internet ou se seu GPS está ativo\n\nLogo em seguida, tente novamente.", "Vamos rever alguns itens", null, null, "OK, Entendi.", ObjetoEleitorActivity.this, 2);
                }

                if (count==2){
                    count=0;
                    startActivity(new Intent(ObjetoEleitorActivity.this, MapsActivity.class));
                }
                   // Log.i("POC","OBJ - LAT: "+latitude+"\nLONG: "+longitude);


                //startActivity(new Intent(ObjetoEleitorActivity.this, MapsActivity.class));
            }
        });


        this.btCancelarObjEleitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertMenseger("Tem certeza que deseja cancelar o registro do objeto?", "Aviso", null, "Sim, sair.", "Não, continuar.", ObjetoEleitorActivity.this, 4);
            }
        });

        this.btSalvarDadosObjEleitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(TextUtils.isEmpty(edtNomeObjEleitor.getText().toString()))){
                    if (!(edtCargo.getText().toString().equals("selecione um cargo"))){
                        if(!(TextUtils.isEmpty(edtNomePolitico.getText().toString()))){
                            if(!(edtPartido.getText().toString().equals("selecione um partido"))){
                                if(m.getLatitude()<0 || m.getLongitude()<0 || m.getLatitude()>0 || m.getLongitude()>0){
                                    if (count3>=1){
                                        tp.setNomeTipoObjeto(edtNomeObjEleitor.getText().toString());

                                        p.setNomePolitico(edtNomePolitico.getText().toString());

                                        objetoEleitor.setLatitude(m.getLatitude());
                                        objetoEleitor.setLongitude(m.getLongitude());

                                        if (TextUtils.isEmpty(edtComentario.getText().toString())){
                                            objetoEleitor.setComentario("nenhum comentario");
                                        }else{
                                            objetoEleitor.setComentario(edtComentario.getText().toString());
                                        }

                                        if (ft1!=null){
                                            String aux1 = bitmapByteStringConverter(ft1);
                                            f1.setImagem1(aux1);
                                        }

                                        if(ft2!=null){
                                            String aux2 = bitmapByteStringConverter(ft2);
                                            f2.setImagem1(aux2);
                                        }

                                        if (ft3!=null){
                                            String aux3 = bitmapByteStringConverter(ft3);
                                            f3.setImagem1(aux3);
                                        }

                                        if (ft4!=null){
                                            String aux4 = bitmapByteStringConverter(ft4);
                                            f4.setImagem1(aux4);
                                        }





                                        launchRingDialog(1);
                                    }else{
                                        alertMenseger("É importante que tire no mínimo uma fotografia do objeto.", "Vamos rever alguns itens", null, null, "OK, Entendi.", ObjetoEleitorActivity.this, 2);
                                    }

                                }else{
                                    alertMenseger("É importante conhecermos a Geolocalização do Objeto Eleitoreiro, portanto devemos consultar o mapa.", "Vamos rever alguns itens", null, null, "OK, Entendi.", ObjetoEleitorActivity.this, 2);
                                }

                            }else{
                                alertMenseger("É importante conhecermos o Partido do Político, portanto devemos completa-lo este campo.", "Vamos rever alguns itens", null, null, "OK, Entendi.", ObjetoEleitorActivity.this, 2);
                            }
                        }else{
                            alertMenseger("É importante conhecermos o Nome do Político, portanto devemos completa-lo este campo.", "Vamos rever alguns itens", null, null, "OK, Entendi.", ObjetoEleitorActivity.this, 2);
                        }


                    }else{
                        alertMenseger("É importante conhecermos o Cargo do Político, portanto devemos completa-lo este campo.", "Vamos rever alguns itens", null, null, "OK, Entendi.", ObjetoEleitorActivity.this, 2);
                    }
                }else{
                    alertMenseger("É importante conhecermos o Tipo do Objeto Eleitoreiro encontrado, portanto devemos completa-lo este campo.", "Vamos rever alguns itens", null, null, "OK, Entendi.", ObjetoEleitorActivity.this, 2);
                }




                /*tp.setNomeTipoObjeto(edtNomeObjEleitor.getText().toString());

                p.setNomePolitico(edtNomePolitico.getText().toString());

                objetoEleitor.setLatitude(m.getLatitude());
                objetoEleitor.setLongitude(m.getLongitude());
                objetoEleitor.setComentario(edtComentario.getText().toString());

                String aux1 = bitmapByteStringConverter(ft1);
                String aux2 = bitmapByteStringConverter(ft2);
                String aux3 = bitmapByteStringConverter(ft3);
                String aux4 = bitmapByteStringConverter(ft4);

                f1.setImagem1(aux1);
                f1.setImagem2(aux2);

                f2.setImagem1(aux3);
                f2.setImagem2(aux4);

                launchRingDialog(1);
                */
                //Log.i("POC",result);

            }
        });

        /*
        this.ivFoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        this.ivFoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 2);
            }
        });

        this.ivFoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 3);
            }
        });

        this.ivFoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 4);

            }
        });


        */


        this.addFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count2<=4){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, count2);
                    count2++;
                    count3++;
                }else{
                    alertMenseger("Você chegou no limite máximo de fotos.","Aviso",null,null,"OK, Entendi.",ObjetoEleitorActivity.this,2);
                }

            }
        });


        this.btSelecionarPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intentPartidos, 0);
            }
        });

        this.btSelecionarCargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intentCargo, 10);
            }
        });



        this.edtPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intentPartidos, 0);
            }
        });

        this.edtCargo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                startActivityForResult(intentCargo, 10);
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

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("POC", "STOP");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("POC", "RESUME");
        m.setReferencia(0);
        spM.salvarShared(m);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("POC", "RESTART");

        spM.lerDados(m);

        if (m.getReferencia()==1){
            mostrarEndereco(m,dm,txtReferencia);
            m.setReferencia(0);
            spM.salvarShared(m);
        }


    }


    //-------------------------//
    //-------------------------//
    //-------------------------//
    //-------------------------//
    //METODO PARA LISTAGEM DE PARTIDOS, CARGOS E O DISPARO DA CAMERA
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            String partidosName = data.getStringExtra(PartidosActivity.RESULT_PARTIDOSNAME);
            String partidosCode = data.getStringExtra(PartidosActivity.RESULT_PARTIDOSCODE);
            Toast.makeText(ObjetoEleitorActivity.this, partidosName, Toast.LENGTH_SHORT).show();
            edtPartido.setText(" " + partidosName + " - " + partidosCode);
            p.setPartidoPolitico(partidosName);
            p.setNumeroLegenda(Integer.parseInt(partidosCode));

            switch (Integer.parseInt(partidosCode)) {
                case 25:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dem, 0, 0, 0);
                    break;
                case 30:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.novo, 0, 0, 0);
                    break;
                case 65:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pc_do_b, 0, 0, 0);
                    break;
                case 21:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pcb, 0, 0, 0);
                    break;
                case 29:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pco, 0, 0, 0);
                    break;
                case 12:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pdt, 0, 0, 0);
                    break;
                case 51:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pen, 0, 0, 0);
                    break;
                case 31:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.phs, 0, 0, 0);
                    break;
                case 35:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pmb, 0, 0, 0);
                    break;
                //------------//
                case 15:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pmdb, 0, 0, 0);
                    break;
                case 33:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pmn, 0, 0, 0);
                    break;
                case 11:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pp, 0, 0, 0);
                    break;
                case 54:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ppl, 0, 0, 0);
                    break;
                case 23:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pps, 0, 0, 0);
                    break;
                case 22:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pr, 0, 0, 0);
                    break;
                case 10:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.prb, 0, 0, 0);
                    break;
                case 90:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pros, 0, 0, 0);
                    break;
                case 44:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.prp, 0, 0, 0);
                    break;
                case 28:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.prtb, 0, 0, 0);
                    break;
                //-----------//
                case 40:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.psb, 0, 0, 0);
                    break;
                case 20:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.psc, 0, 0, 0);
                    break;
                case 55:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.psd, 0, 0, 0);
                    break;
                case 45:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.psdb, 0, 0, 0);
                    break;
                case 27:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.psdc, 0, 0, 0);
                    break;
                case 17:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.psl, 0, 0, 0);
                    break;
                case 50:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.psol, 0, 0, 0);
                    break;
                case 16:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pstu, 0, 0, 0);
                    break;
                case 13:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pt, 0, 0, 0);
                    break;
                case 70:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pt_do_b, 0, 0, 0);
                    break;
                //---------------------------------------------//
                case 14:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ptb, 0, 0, 0);
                    break;
                case 36:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ptc, 0, 0, 0);
                    break;
                case 19:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ptn, 0, 0, 0);
                    break;
                case 43:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pv, 0, 0, 0);
                    break;
                case 18:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rede, 0, 0, 0);
                    break;
                case 77:
                    edtPartido.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sd, 0, 0, 0);
                    break;
            }//switch case


            edtPartido.setTextColor(getResources().getColor(R.color.colorBlack));

        } else if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            String cargoName = data.getStringExtra(CargoActivity.RESULT_CARGONAME);
            String cargoCode = data.getStringExtra(CargoActivity.RESULT_CARGOCODE);
            Toast.makeText(ObjetoEleitorActivity.this, cargoName, Toast.LENGTH_SHORT).show();
            edtCargo.setText(cargoName);
            p.setCargoPolitico(cargoName);

            switch (Integer.parseInt(cargoCode)) {
                case 1:
                    edtCargo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.v, 0, 0, 0);
                    break;
                case 2:
                    edtCargo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.p, 0, 0, 0);
                    break;
            }//switch case

            edtCargo.setTextColor(getResources().getColor(R.color.colorBlack));

        } else {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap btm = (Bitmap) bundle.get("data");
                    if (requestCode == 1) {
                        this.ivFoto1.setImageBitmap(btm);
                        this.ft1 = btm;
                    } else if (requestCode == 2) {
                        this.ivFoto2.setImageBitmap(btm);
                        this.ft2 = btm;
                    } else if (requestCode == 3) {
                        this.ivFoto3.setImageBitmap(btm);
                        this.ft3 = btm;
                    } else if (requestCode == 4) {
                        this.ivFoto4.setImageBitmap(btm);
                        this.ft4 = btm;
                    }
                    Log.i("POC", "reqCODE: " + requestCode + " | rsCODE: " + resultCode + " | aux: ");
                }//if else if interno
            }//if interno
        }//else
    }//onActivityResult


    //-------------------------//
    //-------------------------//
    //-------------------------//
    //-------------------------//
    //-------------------------//
    //-------------------------//
    //MENSAGEM - ALERTAS
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
                    startActivity(new Intent(ObjetoEleitorActivity.this,MenuActivity.class));
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


    //-------------------------//
    //-------------------------//
    //-------------------------//
    //-------------------------//
    //-------------------------//
    //-------------------------//
    //-------------------------//
    //-------------------------//

    //-------------------------------------------------------------------------------------///

    //API LOCATION - METHODS

    private synchronized void callConnection() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("POC", "onConnected(" + bundle + ")");
        if (!(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            Log.i("POC", "OBJ: Entrou no if");
            Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (l != null) {
               // Log.i("POC", "latitude: " + l.getLatitude());
                //Log.i("POC", "longitude: " + l.getLongitude());
                latitude = l.getLatitude();
                longitude = l.getLongitude();
                Log.i("POC", "latitude double: " + l.getLatitude());
                Log.i("POC", "longitude double: " + l.getLongitude());
                m.setLatitude(l.getLatitude());
                m.setLongitude(l.getLongitude());
            }
        }

    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOG", "onConnectionSuspended(" + i + ")");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("LOG", "onConnectionFailed(" + connectionResult + ")");
    }


    //-------------------------//
    //-------------------------//
    //-------------------------//
    //-------------------------//
    //-------------------------//
    //-------------------------//
    //-------------------------//
    //-------------------------//
    //-------------------------//
    //-------------------------//

    //-------------------------------------------------------------------------------//
    //GEOCODER

    private Address buscarEndereco(double latitude, double longitude) {
        Address address = null;
        Geocoder geo;
        List<Address> addressList;

        geo = new Geocoder(ObjetoEleitorActivity.this);

        try {
            addressList = geo.getFromLocation(latitude, longitude, 1);

            if (addressList.size() > 0) {
                address = addressList.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }


    private void launchRingDialog(int opcao) {

        if (opcao==1){
            progressDialog = ProgressDialog.show(ObjetoEleitorActivity.this, "", "Enviando dados. Aguarde um momento...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        carregaDados();
                        //alertMenseger("Todos as informações estão salvas.","Sucesso",null,"Terminar",null,ObjetoEleitorActivity.this,1);
                    } catch (Exception e) {
                        //alertMenseger("Ocorreu alguma falha ao salvar as informações.\n\nVerifique sua conexão ou se deixou de preencher algum campo importante.","Aviso",null,null,"OK, Entendi.",ObjetoEleitorActivity.this,2);
                        Log.e("POC", "Thread: "+e.getMessage());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {

                            progressDialog.dismiss();
                            if (aux){
                                alertMenseger("Pronto, objeto salvo. Obrigado pelas informações.","Sucesso",null,"Continuar.",null,ObjetoEleitorActivity.this,1);
                            }else{
                                alertMenseger("Houve alguma falha.\n\nVerique se os dados estão corretos ou a sua conexão.","Aviso",null,null,"OK, Entendi.",ObjetoEleitorActivity.this,3);
                            }


                        }
                    });

                    //progressDialog.dismiss();
                }
            }).start();


        }else if (opcao==2){
            progressDialog = ProgressDialog.show(ObjetoEleitorActivity.this, "", "Achando a localização mais proxima. Aguarde um momento...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                        callConnection();
                        callConnection();
                        callConnection();
                    } catch (Exception e) {
                        Log.e("POC", "Thread: "+e.getMessage());
                    }
                    progressDialog.dismiss();
                }

            }).start();
        }


    }



    private void carregaDados(){

        String url = "http://192.168.137.1:8080/WebServicePOC/webresources/wbspoc/TipoObjeto/inserirTipoObjeto";
        //String url = "http://192.168.1.2:8080/WebServicePOC/webresources/wbspoc/TipoObjeto/inserirTipoObjeto";
        //String url = "http://192.168.1.6:8080/WebServicePOC/webresources/wbspoc/TipoObjeto/inserirTipoObjeto";
        String postTP= aR.chamadaPOST(url,g.toJson(tp));
        Log.i("POC","Tipo Objeto: "+postTP);
        aux = Boolean.parseBoolean(postTP);

        String url2="http://192.168.137.1:8080/WebServicePOC/webresources/wbspoc/Politico/inserirPolitico";
        //String url2="http://192.168.1.2:8080/WebServicePOC/webresources/wbspoc/Politico/inserirPolitico";
        //String url2="http://192.168.1.6:8080/WebServicePOC/webresources/wbspoc/Politico/inserirPolitico";
        String postP = aR.chamadaPOST(url2,g.toJson(p));
        Log.i("POC","Politico: "+postP);
        aux = Boolean.parseBoolean(postP);

        String url3 = "http://192.168.137.1:8080/WebServicePOC/webresources/wbspoc/Politico/selectPoliticoID";
        //String url3 = "http://192.168.1.2:8080/WebServicePOC/webresources/wbspoc/Politico/selectPoliticoID";
        //String url3 = "http://192.168.1.6:8080/WebServicePOC/webresources/wbspoc/Politico/selectPoliticoID";
        String getPID = aR.chamadaGET(url3);
        Log.i("POC","Politico GET ID: "+getPID);
        p.setIdPolitico(Integer.parseInt(getPID));

        String url4 = "http://192.168.137.1:8080/WebServicePOC/webresources/wbspoc/TipoObjeto/selectTipoObjetoID";
        //String url4 = "http://192.168.1.2:8080/WebServicePOC/webresources/wbspoc/TipoObjeto/selectTipoObjetoID";
        //String url4 = "http://192.168.1.6:8080/WebServicePOC/webresources/wbspoc/TipoObjeto/selectTipoObjetoID";
        String getTOID = aR.chamadaGET(url4);
        tp.setIdTipoObjeto(Integer.parseInt(getTOID));
        Log.i("POC","Tipo Objeto GET ID: "+getTOID);

        String url5 = "http://192.168.137.1:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/inserirObjetoEleitor";
        //String url5 = "http://192.168.1.2:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/inserirObjetoEleitor";
        //String url5 = "http://192.168.1.6:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/inserirObjetoEleitor";
        objetoEleitor.setIdPoliticoFk(Integer.parseInt(getPID));
        objetoEleitor.setIdTipoObjetoFk(Integer.parseInt(getTOID));
        String postOE = aR.chamadaPOST(url5,g.toJson(objetoEleitor));
        Log.i("POC","Objeto Eleitor: "+postOE);

        String url6 = "http://192.168.137.1:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/selectObjetoEleitorID";
        //String url6 = "http://192.168.1.2:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/selectObjetoEleitorID";
        //String url6 = "http://192.168.1.6:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/selectObjetoEleitorID";
        String getOEID = aR.chamadaGET(url6);
        Log.i("POC","Obj ELEI ID: "+getOEID);

        f1.setIdObjeto(Integer.parseInt(getOEID));
        f2.setIdObjeto(Integer.parseInt(getOEID));
        f3.setIdObjeto(Integer.parseInt(getOEID));
        f4.setIdObjeto(Integer.parseInt(getOEID));

        if (count3>=1){
            String url7 = "http://192.168.137.1:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/upFotos1";
            //String url7 = "http://192.168.1.2:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/upFotos1";
            //String url7 = "http://192.168.1.6:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/upFotos1";
            String postUPF1=aR.chamadaPOST(url7,g.toJson(f1));
            Log.i("POC","UP F1: "+postUPF1);
            aux = Boolean.parseBoolean(postUPF1);
        }

        if (count3>=2){
            String url8 = "http://192.168.137.1:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/upFotos2";
            //String url8 = "http://192.168.1.2:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/upFotos2";
            //String url8 = "http://192.168.1.6:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/upFotos2";
            String postUPF2=aR.chamadaPOST(url8,g.toJson(f2));
            Log.i("POC","UP F2: "+postUPF2);
            aux = Boolean.parseBoolean(postUPF2);
        }

        if (count3>=3){
            String url9 = "http://192.168.137.1:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/upFotos3";
            //String url9 = "http://192.168.1.2:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/upFotos3";
            //String url9 = "http://192.168.1.6:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/upFotos3";
            String postUPF3=aR.chamadaPOST(url9,g.toJson(f3));
            Log.i("POC","UP F3: "+postUPF3);
            aux = Boolean.parseBoolean(postUPF3);
        }

        if (count3>=4){
            String url10 = "http://192.168.137.1:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/upFotos4";
            //String url10 = "http://192.168.1.2:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/upFotos4";
            //String url10 = "http://192.168.1.6:8080/WebServicePOC/webresources/wbspoc/ObjetoEleitor/upFotos4";
            String postUPF4=aR.chamadaPOST(url10,g.toJson(f4));
            Log.i("POC","UP F4: "+postUPF4);
            aux = Boolean.parseBoolean(postUPF4);
        }









    }









    private void mostrarEndereco(Mapa m, DadosMapa dm, TextView txt){
        try {
            endereco = buscarEndereco(m.getLatitude(), m.getLongitude());
            dm.setCidade(endereco.getLocality());
            dm.setBairro(endereco.getSubLocality());
            dm.setEndereco(endereco.getThoroughfare());
            dm.setCep(endereco.getPostalCode());
            dm.setEstado(endereco.getAdminArea());
            dm.setLatitude(m.getLatitude());
            dm.setLongitude(m.getLongitude());

            Log.i("POC", "OBJ - LAT: " + m.getLatitude() + " | LONG: " + m.getLongitude());

            txt.setText("Endereço: " + dm.getEndereco() + "\nBairro: " + dm.getBairro() + "\nCidade: " + dm.getCidade() + " - " + dm.getEstado() + "\nCEP: " + dm.getCep());
            txt.setTypeface(Typeface.DEFAULT_BOLD);


        } catch (Exception e) {
            e.printStackTrace();
            alertMenseger("Verifique sua conexão à internet ou se seu GPS está ativo\n\nLogo em seguida, tente novamente.", "Vamos rever alguns itens", null, null, "OK, Entendi.", ObjetoEleitorActivity.this, 2);
            txt.setText("Dica: Verifique sua conexão à internet ou se seu GPS está ativo.");
            txt.setTypeface(Typeface.DEFAULT);
        }
    }






    private String bitmapByteStringConverter (Bitmap bitmap){

        Bitmap btm = bitmap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        btm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bytes = bos.toByteArray();
        Log.i("POC","BYTES: "+bytes.length);
        String aux = Base64.encodeToString(bytes,Base64.DEFAULT);
        Log.i("POC","STRING2: "+aux);
        Log.i("POC","Size: "+aux.length());

        return aux;
    }

}



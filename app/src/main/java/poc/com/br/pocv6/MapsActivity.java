package poc.com.br.pocv6;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import classesAuxiliaresApp.DadosMapa;
import classesSharePreference.Mapa;
import classesSharePreference.SharePreferenceMaps;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button btMaps, btInfoMaps, btMapsConfirm;
    private Marker marker;
    private DadosMapa dm = new DadosMapa();
    private Mapa m = new Mapa();
    private SharePreferenceMaps shM = new SharePreferenceMaps();

    //private Location location;
    //private LocationManager locationManager;

    private LatLng lat;
    private double latitudeToast=-15.796258, longitudeToast=-47.877839;
    //private double latitudeToast, longitudeToast;
    private int zoom = 4;

    private Address endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.shM.shared2 = getSharedPreferences(this.shM.MyPREFERENCES, Context.MODE_PRIVATE);

        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        Log.i("POC","LAT: "+latitudeToast+"\nLONG: "+longitudeToast);
        if(params!=null){
            Log.i("POC","Entoru no if");
            //String mostraTexto = params.getString("mensagem");
            if (Double.valueOf(params.getString("Latitude"))!=0){
                latitudeToast = Double.valueOf(params.getString("Latitude"));
                longitudeToast = Double.valueOf(params.getString("Longitude"));
                Log.i("POC","LAT: "+latitudeToast+"\nLONG: "+longitudeToast);
                zoom = 15;
            }

        }

        this.btMaps = (Button) findViewById(R.id.btMaps);
        this.btInfoMaps = (Button) findViewById(R.id.btInfoMaps);
        this.btMapsConfirm = (Button) findViewById(R.id.btMapsConfirm);

        this.btMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    endereco = buscarEndereco(latitudeToast,longitudeToast);

                    dm.setCidade(endereco.getLocality());
                    dm.setBairro(endereco.getSubLocality());
                    dm.setEndereco(endereco.getThoroughfare());
                    dm.setCep(endereco.getPostalCode());
                    dm.setEstado(endereco.getAdminArea());
                    dm.setLatitude(latitudeToast);
                    dm.setLongitude(longitudeToast);

                    //Toast.makeText(MapsActivity.this,"Latitude: "+dm.getLatitude()+"\nLongitude: "+dm.getLongitude()+"\nEndereço: "+dm.getEndereco()+"\nCEP: "+dm.getCep()
                       //     +"\nEstado: "+dm.getEstado()+"\nCidade: "+dm.getCidade()+"\nBairro: "+dm.getBairro(), Toast.LENGTH_SHORT).show();

                    Toast.makeText(MapsActivity.this,"Endereço: "+dm.getEndereco()+"\nBairro: "+dm.getBairro()+"\nCEP: "+dm.getCep()+"\nCidade: "+dm.getCidade()+"\nEstado: "+dm.getEstado(),Toast.LENGTH_SHORT).show();
                    Log.i("POC","1: "+endereco.getSubAdminArea()+"\n2: "+endereco.getPremises()+"\n3: "+endereco.getAddressLine(0)
                            +"\n4: "+endereco.getCountryCode()+"\n5: "+endereco.getFeatureName()+"\n6: "+endereco.getSubLocality()
                            +"\n7: "+endereco.getLocality()+"\n8: "+endereco.getSubThoroughfare()+"\n9: "+endereco.getUrl()
                            +"10: "+endereco.getLocale().getCountry());
                }catch (Exception e){
                    e.printStackTrace();
                    alertMenseger("Verifique sua conexao à internet ou se seu marcador está posicionado corretamente.","Vamos rever alguns itens",null,null,"OK, Entendi.",MapsActivity.this,3);
                }





            }
        });

        this.btMapsConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latitudeToast==0){
                    alertMenseger("Marque primeiro uma posição no mapa antes de escolher.","Aviso","OK, Entendi.",null,null,MapsActivity.this,2);
                }else{
                    m.setLatitude(latitudeToast);
                    m.setLongitude(longitudeToast);
                    m.setReferencia(1);
                    Log.i("POC","Lat"+ m.getLatitude());
                    shM.salvarShared(m);
                    Log.i("POC","Passou o shared");
                    finish();
                }


            }
        });





        this.btInfoMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertMenseger("-> Click na localização exata onde o objeto eleitoreiro se encontra.\n" +
                        "\n-> Dê zoom no mapa para melhor precisão\n" +
                        "\n-> Pode ocorrer do Google Maps não possui registros em um determinado ponto do mapa. Mesmo assim, a localização irá ser precisa.\n"+
                        "\n\nAguarde um estante enquanto o mapa está carregando.","Algumas orientações",null,"OK, Entendi.",null,MapsActivity.this,1);
            }
        });

        alertMenseger("-> Click na localização exata onde o objeto eleitoreiro se encontra.\n" +
                "\n-> Dê zoom no mapa para melhor precisão\n" +
                "\n-> Pode ocorrer do Google Maps não possui registros em um determinado ponto do mapa. Mesmo assim, a localização irá ser precisa.\n"+
                "\n\nAguarde um estante enquanto o mapa está carregando.","Algumas orientações",null,"OK, Entendi.",null,MapsActivity.this,1);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Log.i("POC","LAT: "+latitudeToast+"\nLONG: "+longitudeToast);
        LatLng latLng = new LatLng(latitudeToast, longitudeToast);

        //TESTAR tilt(90)
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(zoom).bearing(0).tilt(0).build();
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(cameraPosition);

        // mMap.addMarker(new MarkerOptions().position(latLng).title("Mario Werneck").icon(BitmapDescriptorFactory.fromResource(R.mipmap.pin)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        mMap.animateCamera(update, 3000, new GoogleMap.CancelableCallback(){
            @Override
            public void onCancel() {
                Log.i("POC", "CancelableCallback.onCancel()");
            }
            @Override
            public void onFinish() {
                latitudeToast=0;
                longitudeToast=0;
                Log.i("POC", "CancelableCallback.onFinish()");
            }
        });


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i("POC", "setOnMapClickListener()");
                if(marker != null){
                    marker.remove();
                }
                customAddMarker(new LatLng(latLng.latitude, latLng.longitude), "Marcador Posicionado", "Aqui está localizado um objeto eleitoreiro");
                latitudeToast = latLng.latitude;
                longitudeToast = latLng.longitude;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i("POC", "3: Marker: "+marker.getTitle());
                return false;
            }
        });
    }



    private void customAddMarker(LatLng latLng, String title, String snippet){
        MarkerOptions options = new MarkerOptions();
        options.position(latLng).title(title).snippet(snippet).draggable(true);
        options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.pin));

        marker = mMap.addMarker(options);
    }


    private Address buscarEndereco (double latitude, double longitude){
        Address address = null;
        Geocoder geo;
        List<Address> addressList;

        geo = new Geocoder(MapsActivity.this);

        try {
            addressList = geo.getFromLocation(latitude,longitude,1);

            if (addressList.size()>0){
                address = addressList.get(0);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return address;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
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
            alert.setPositiveButton(buttonNeutral, null);
        } else if (opc == 3) {
            alert.setIcon(R.mipmap.opa);
            alert.setNegativeButton(buttonNegative, null);
        } else {
            alert.setIcon(R.mipmap.opa);
            alert.setPositiveButton(buttonPositive, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    onStop();
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



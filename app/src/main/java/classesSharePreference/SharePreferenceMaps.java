package classesSharePreference;

import android.content.SharedPreferences;

/**
 * Created by JOAO on 11/06/2016.
 */
public class SharePreferenceMaps {

    public static final String MyPREFERENCES = "Mapa";

    public static final String Latitude = "latitude";
    public static final String Longitude = "longitude";
    public static final String Referencia = "referencia";

    public SharedPreferences shared2;
    public SharedPreferences.Editor editor2;

    public void salvarShared(Mapa m){
        this.editor2 = this.shared2.edit();

        editor2.putString(Latitude, String.valueOf(m.getLatitude()));
        editor2.putString(Longitude, String.valueOf(m.getLongitude()));
        editor2.putString(Referencia,String.valueOf(m.getReferencia()));
        editor2.commit();
    }

    public void lerDados (Mapa m){
        m.setLatitude(Double.valueOf(this.shared2.getString(Latitude,"")));
        m.setLongitude(Double.valueOf(this.shared2.getString(Longitude,"")));
        m.setReferencia(Integer.parseInt(this.shared2.getString(Referencia,"")));
    }

}


package classesSharePreference;

/**
 * Created by JOAO on 11/06/2016.
 */
public class Mapa{

    private double latitude, longitude;
    private int referencia;


    public Mapa(){

    }

    public Mapa(double latitude, double longitude, int referencia){
        this.latitude = latitude;
        this.longitude = longitude;
        this.referencia=referencia;
    }

    public int getReferencia() {
        return referencia;
    }

    public void setReferencia(int referencia) {
        this.referencia = referencia;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}

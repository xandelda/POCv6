package classesBD;

/**
 * Created by JOAO on 21/06/2016.
 */
public class ObjetoEleitor3 {

    private int id;
    private double latitude;
    private double longitude;
    private int idPoliticoFk;
    private int idTipoObjetoFk;
    private String comentario;

    public ObjetoEleitor3() {
    }

    public ObjetoEleitor3(int id, double latitude, double longitude, int idPoliticoFk, int idTipoObjetoFk, String comentario) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.idPoliticoFk = idPoliticoFk;
        this.idTipoObjetoFk = idTipoObjetoFk;
        this.comentario = comentario;
    }

    public String getComentario() {
        return comentario;
    }

    public int getId() {
        return id;
    }

    public int getIdPoliticoFk() {
        return idPoliticoFk;
    }

    public int getIdTipoObjetoFk() {
        return idTipoObjetoFk;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdPoliticoFk(int idPoliticoFk) {
        this.idPoliticoFk = idPoliticoFk;
    }

    public void setIdTipoObjetoFk(int idTipoObjetoFk) {
        this.idTipoObjetoFk = idTipoObjetoFk;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}

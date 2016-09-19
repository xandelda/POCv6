package classesBD;

/**
 * Created by JOAO on 19/06/2016.
 */
public class ObjetoEleitor {

    private int id;
    private String latitude;
    private String longitude;
    private String imagem1;
    private String imagem2;
    private String imagem3;
    private String imagem4;
    private String idPoliticoFk;
    private String idTipoObjetoFk;
    private String comentario;

    public ObjetoEleitor(int id, String latitude, String longitude, String imagem1, String imagem2, String imagem3, String imagem4, String idPoliticoFk, String idTipoObjetoFk, String comentario) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imagem1 = imagem1;
        this.imagem2 = imagem2;
        this.imagem3 = imagem3;
        this.imagem4 = imagem4;
        this.idPoliticoFk = idPoliticoFk;
        this.idTipoObjetoFk = idTipoObjetoFk;
        this.comentario = comentario;
    }

    public ObjetoEleitor() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImagem1() {
        return imagem1;
    }

    public void setImagem1(String imagem1) {
        this.imagem1 = imagem1;
    }

    public String getImagem2() {
        return imagem2;
    }

    public void setImagem2(String imagem2) {
        this.imagem2 = imagem2;
    }

    public String getImagem3() {
        return imagem3;
    }

    public void setImagem3(String imagem3) {
        this.imagem3 = imagem3;
    }

    public String getImagem4() {
        return imagem4;
    }

    public void setImagem4(String imagem4) {
        this.imagem4 = imagem4;
    }

    public String getIdPoliticoFk() {
        return idPoliticoFk;
    }

    public void setIdPoliticoFk(String idPoliticoFk) {
        this.idPoliticoFk = idPoliticoFk;
    }

    public String getIdTipoObjetoFk() {
        return idTipoObjetoFk;
    }

    public void setIdTipoObjetoFk(String idTipoObjetoFk) {
        this.idTipoObjetoFk = idTipoObjetoFk;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}


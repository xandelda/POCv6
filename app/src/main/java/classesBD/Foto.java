package classesBD;

/**
 * Created by JOAO on 20/06/2016.
 */
public class Foto {

    private String imagem1;
    private int idObjeto;

    public Foto(String imagem1, int idObjeto) {
        this.imagem1 = imagem1;
        this.idObjeto = idObjeto;
    }

    public Foto() {
    }

    public String getImagem1() {
        return imagem1;
    }

    public void setImagem1(String imagem1) {
        this.imagem1 = imagem1;
    }

    public int getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(int idObjeto) {
        this.idObjeto = idObjeto;
    }
}

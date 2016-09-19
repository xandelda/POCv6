package classesBD;

/**
 * Created by JOAO on 19/06/2016.
 */
public class TipoObjeto {

    private String nomeTipoObjeto;
    private int idTipoObjeto;

    public TipoObjeto() {
    }

    public TipoObjeto(String nomeTipoObjeto, int idTipoObjeto) {
        this.nomeTipoObjeto = nomeTipoObjeto;
        this.idTipoObjeto = idTipoObjeto;
    }

    public int getIdTipoObjeto() {
        return idTipoObjeto;
    }

    public String getNomeTipoObjeto() {
        return nomeTipoObjeto;
    }

    public void setIdTipoObjeto(int idTipoObjeto) {
        this.idTipoObjeto = idTipoObjeto;
    }

    public void setNomeTipoObjeto(String nomeTipoObjeto) {
        this.nomeTipoObjeto = nomeTipoObjeto;
    }
}

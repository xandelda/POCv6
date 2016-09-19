package classesBD;

/**
 * Created by JOAO on 19/06/2016.
 */
public class Politico {

    private int idPolitico;
    private String nomePolitico,partidoPolitico,cargoPolitico;
    private int numeroLegenda;

    public Politico() {
    }

    public Politico(int idPolitico, String nomePolitico, String partidoPolitico, String cargoPolitico, int numeroLegenda) {
        this.idPolitico = idPolitico;
        this.nomePolitico = nomePolitico;
        this.partidoPolitico = partidoPolitico;
        this.cargoPolitico = cargoPolitico;
        this.numeroLegenda = numeroLegenda;
    }

    public String getCargoPolitico() {
        return cargoPolitico;
    }

    public int getIdPolitico() {
        return idPolitico;
    }

    public String getNomePolitico() {
        return nomePolitico;
    }

    public int getNumeroLegenda() {
        return numeroLegenda;
    }

    public String getPartidoPolitico() {
        return partidoPolitico;
    }

    public void setCargoPolitico(String cargoPolitico) {
        this.cargoPolitico = cargoPolitico;
    }

    public void setIdPolitico(int idPolitico) {
        this.idPolitico = idPolitico;
    }

    public void setNomePolitico(String nomePolitico) {
        this.nomePolitico = nomePolitico;
    }

    public void setNumeroLegenda(int numeroLegenda) {
        this.numeroLegenda = numeroLegenda;
    }

    public void setPartidoPolitico(String partidoPolitico) {
        this.partidoPolitico = partidoPolitico;
    }



}

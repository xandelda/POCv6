package classesAuxiliaresApp;

/**
 * Created by JOAO on 09/06/2016.
 */
public class DadosMapa {

    private String bairro, cidade, endereco, cep, estado;
    private double latitude, longitude;

    public DadosMapa(){

    }

    public DadosMapa(String cidade, String bairro, String endereco, String cep, String estado, double latitude, double longitude){
        this.bairro = bairro;
        this.cidade = cidade;
        this.endereco = endereco;
        this.cep=cep;
        this.estado=estado;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCep() {
        return cep;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getEstado() {
        return estado;
    }

    public void setBairro(String bairro) {
        if (bairro==null){
            this.bairro="não registrado";
        }else{
            this.bairro = bairro;
        }
    }

    public void setCep(String cep) {
        if (cep==null){
            this.cep="não registrado";
        }else{
            this.cep = cep;
        }
    }

    public void setCidade(String cidade) {
        if (cidade==null){
            this.cidade="não registrado";
        }else{
            this.cidade = cidade;
        }
    }

    public void setEndereco(String endereco) {
        if (endereco==null){
            this.endereco="não registrado";
        }else{
            this.endereco = endereco;
        }
    }

    public void setEstado(String estado) {
        if (estado==null){
            this.estado="não registrado";
        }else{
            this.estado = estado;
        }
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}

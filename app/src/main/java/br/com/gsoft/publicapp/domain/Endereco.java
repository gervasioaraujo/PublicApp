package br.com.gsoft.publicapp.domain;

/**
 * Created by gervasio on 29/07/2017.
 */

@org.parceler.Parcel
public class Endereco {

    private static final long serialVersionUID = 1L;

    public Long id;
    public String rua;
    public String cep;
    public String numero;
    public String bairro;
    public String cidade;
    public String uf;
    public String complemento;

    @Override
    public String toString() {
        String endereco = rua + ", " + numero + ", " + bairro + ", " + cidade + "-" + uf;
        return endereco;
    }
}

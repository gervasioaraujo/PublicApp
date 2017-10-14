package br.com.gsoft.publicapp.domain;

/**
 * Created by gervasio on 12/09/2017.
 */

@org.parceler.Parcel
public class Anunciante {

    private static final long serialVersionUID = 1L;

    public Long id;
    public String nome;
    public String descricao;
    public String urlFoto;
    public Endereco endereco;
    public Contato contato;
    public SegmentoComercial segmentoComercial;

}

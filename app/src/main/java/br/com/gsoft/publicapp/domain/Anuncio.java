package br.com.gsoft.publicapp.domain;

import java.util.Date;

/**
 * Created by gervasio on 12/09/2017.
 */
@org.parceler.Parcel
public class Anuncio {

    private static final long serialVersionUID = 1L;

    public Long id;
    public Date dataCriacao;
    public Date dataUltimaAtualizacao;
    public Produto produto;
    public Anunciante anunciante;

    // ******************************************
    public Anuncio(){
        produto = new Produto();
        anunciante = new Anunciante();
    }

}

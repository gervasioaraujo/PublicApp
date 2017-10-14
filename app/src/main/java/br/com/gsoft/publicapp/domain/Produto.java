package br.com.gsoft.publicapp.domain;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by gervasio on 27/06/2017.
 */
@org.parceler.Parcel
public class Produto {

    private static final long serialVersionUID = 1L;

    public Long id;
    public String nome;
    public BigDecimal preco;
    public String urlFoto;

//    public boolean selected; // Flag para indicar que o produto está selecionado

    public String getPrecoString() {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String formatado = nf.format (this.preco);
        return formatado;
    }

    @Override
    public String toString() {
        return this.nome;
    }

}

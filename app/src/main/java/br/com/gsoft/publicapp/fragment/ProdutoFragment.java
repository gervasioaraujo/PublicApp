package br.com.gsoft.publicapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import br.com.gsoft.publicapp.R;
import br.com.gsoft.publicapp.domain.Anuncio;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProdutoFragment extends BaseFragment {

    private Anuncio anuncio;

    public ProdutoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produto, container, false);
        //produto = (Produto) getArguments().getSerializable("produto");
        anuncio = Parcels.unwrap(getArguments().getParcelable("anuncio"));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Atualiza a view do fragment com os dados do evento
        setTextString(R.id.tDesc, anuncio.produto.nome);
        final ImageView imgView = (ImageView) getView().findViewById(R.id.img);
        Picasso.with(getContext()).load(anuncio.produto.urlFoto).fit().into(imgView);
    }
}

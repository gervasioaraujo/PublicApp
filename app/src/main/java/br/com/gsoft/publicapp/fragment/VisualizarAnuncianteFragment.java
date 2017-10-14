package br.com.gsoft.publicapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import br.com.gsoft.publicapp.R;
import br.com.gsoft.publicapp.activity.AnuncianteActivity;
import br.com.gsoft.publicapp.domain.Anunciante;

/**
 * A simple {@link Fragment} subclass.
 */
public class VisualizarAnuncianteFragment extends BaseFragment implements AnuncianteActivity.ClickHeaderListener {

    protected TextView tNomeAnunciante;
    protected TextView tEndereco;
    private Anunciante anunciante;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lê os argumentos
        anunciante = Parcels.unwrap(getArguments().getParcelable("anunciante"));

        setHasOptionsMenu(true); // ********* Para que os botões de editar e deletar na Action Bar apareçam

        AnuncianteActivity activity = (AnuncianteActivity) getActivity();
        activity.setClickHeaderListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_visualizar_anunciante, container, false);

        initViews(view);

        if (anunciante != null) {
            setAnuncio(anunciante);
        }

        return view;
    }

    private void initViews(View view) {
        //img = (ImageView) view.findViewById(R.id.img);
        tNomeAnunciante = (TextView) view.findViewById(R.id.tNomeAnunciante);
        tEndereco = (TextView) view.findViewById(R.id.tEndereco);
    }

    private void setAnuncio(Anunciante a) {
        if (a != null) {
            tNomeAnunciante.setText(a.nome);
            tEndereco.setText(a.endereco.toString());
        }

        // Imagem do Header na Toolbar
        AnuncianteActivity activity = (AnuncianteActivity) getActivity();
        activity.setAppBarInfo(a);
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        // Atualiza a view do fragment com os dados do evento
//        setTextString(R.id.tDesc, anunciante.nome);
//        final ImageView imgView = (ImageView) getView().findViewById(R.id.img);
//        Picasso.with(getContext()).load(anunciante.urlFoto).fit().into(imgView);
//    }

    // ************* IMPLEMENTAÇÃO DO MÉTODO DA INTERFACE AnuncioActivity.ClickHeaderListener ***************
    @Override
    public void onHeaderClicked() {
//        toast("Para alterar a foto precisa editar o anuncio.");
    }

}

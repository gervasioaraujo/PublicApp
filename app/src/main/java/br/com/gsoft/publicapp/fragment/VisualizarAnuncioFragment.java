package br.com.gsoft.publicapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import br.com.gsoft.publicapp.R;
import br.com.gsoft.publicapp.activity.AnuncioActivity;
import br.com.gsoft.publicapp.domain.Anuncio;
import br.com.gsoft.publicapp.service.AnuncioService;

public class VisualizarAnuncioFragment extends BaseFragment implements AnuncioActivity.ClickHeaderListener {

    private static final int REQUEST_CODE_SALVAR = 1;

    protected TextView tNomeProduto;
    protected TextView tPrecoProduto;
    private Anuncio anuncio;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lê os argumentos
        anuncio = Parcels.unwrap(getArguments().getParcelable("anuncio"));

        setHasOptionsMenu(true); // ********* Para que os botões de editar e deletar na Action Bar apareçam

        AnuncioActivity activity = (AnuncioActivity) getActivity();
        activity.setClickHeaderListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_visualizar_anuncio, container, false);

        initViews(view);

        if (anuncio != null) {
            setAnuncio(anuncio);
        }

        return view;
    }


    private void initViews(View view) {
        //img = (ImageView) view.findViewById(R.id.img);
        tNomeProduto = (TextView) view.findViewById(R.id.tNomeProdutoV);
        tPrecoProduto = (TextView) view.findViewById(R.id.tPrecoProdutoV);
    }

    private void setAnuncio(Anuncio a) {
        if (a != null) {
            tNomeProduto.setText(a.produto.nome);
            tPrecoProduto.setText(a.produto.getPrecoString());
        }

        // Imagem do Header na Toolbar
        AnuncioActivity activity = (AnuncioActivity) getActivity();
        activity.setAppBarInfo(a);
    }


    // ************* IMPLEMENTAÇÃO DO MÉTODO DA INTERFACE AnuncioActivity.ClickHeaderListener ***************
    @Override
    public void onHeaderClicked() {
        toast("Para alterar a foto precisa editar o anuncio.");
    }


    // ************* INSERÇÃO DO MENU COM AS OPÇÕES DE EDITAR E DELETAR O ANÚNCIO ******************
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_frag_visualizar_anuncio, menu);
    }

    // ************* MÉTODO QUE TRATA O CLIQUE NO BOTÃO DE EDITAR E DELETAR ***************
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            Intent intent = new Intent(getActivity(), AnuncioActivity.class);
            intent.putExtra("anuncio", Parcels.wrap(anuncio));
            intent.putExtra("modoCadastroEdicao", true);
            ActivityOptionsCompat opts = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity());
            ActivityCompat.startActivityForResult(getActivity(), intent, REQUEST_CODE_SALVAR, opts.toBundle());
            // Por definição, vamos fechar esta tela para ficar somente a de editar.
            getActivity().finish();
            return true;
        } else if (id == R.id.action_remove) {

            // ******* DELETAR ANÚNCIO **********!!!!!!!!!!!!!!!!!!!!!!!!!!
            deletarAnuncio();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Deletar carros selecionados ao abrir a CAB
    private void deletarAnuncio() {

            startTask("deletar",new BaseTask(){
                @Override
                public Object execute() throws Exception {
                    boolean ok = AnuncioService.delete(anuncio);
                    if(ok) {
//                        // Se excluiu do banco, remove da lista da tela.
//                        for (Carro c : selectedCarros) {
//                            carros.remove(c);
//                        }

                        // Fechar a tela
                        getActivity().finish();
                    }
                    return null;
                }

                @Override
                public void updateView(Object count) {
                    super.updateView(count);
                    // Mostra mensagem de sucesso
//                    snack(recyclerView, selectedCarros.size() + " Anúncio excluído com sucesso");
                    // Atualiza a lista de carros
                    //taskCarros(true);
                    toast("Anúncio deletado com sucesso!");
                    // Atualiza a lista
//                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            });
    }


}

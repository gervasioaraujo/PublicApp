package br.com.gsoft.publicapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import br.com.gsoft.publicapp.R;
import br.com.gsoft.publicapp.activity.AnuncioActivity;
import br.com.gsoft.publicapp.activity.BaseActivity;
import br.com.gsoft.publicapp.adapter.AnuncioAdapter;
import br.com.gsoft.publicapp.domain.Anuncio;
import br.com.gsoft.publicapp.service.AnuncioService;
import livroandroid.lib.utils.AndroidUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProdutosFragment extends BaseFragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private BaseActivity activity;

    private RecyclerView recyclerView;
    private List<Anuncio> anuncios;

    private String query;
    private String segmentoComercial;

    public static ProdutosFragment newInstance(String segmentoComercial, String query) {

        Bundle args = new Bundle();
        args.putString("segmentoComercial", segmentoComercial);
        args.putString("query", query);

        ProdutosFragment f = new ProdutosFragment();
        f.setArguments(args);

        return f;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.segmentoComercial = getArguments().getString("segmentoComercial");
            this.query = getArguments().getString("query");
        }

        setHasOptionsMenu(true); // ################## PARA QUE OS MENUS SE TORNEM VISÍVEIS NA TELA #################

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_produtos, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); // o tamanho do RecyclerView vai ser sempre o mesmo (com 1 ou 30 itens, não importa)
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Swipe to Refresh
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener());
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);

        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_frag_anuncios, menu);

        // SearchView
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(onSearch());
//        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    closeMenuSearch = false;
//                } else {
//                    closeMenuSearch = true;
//                }
//            }
//        });

    }

    private SearchView.OnQueryTextListener onSearch() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscaAnuncios(newText);
                return true; // ************************************* true ??????????????
            }
        };
    }

    private void buscaAnuncios(String nome) {
        // Atualiza ao fazer o gesto Swipe To Refresh
        if (AndroidUtils.isNetworkAvailable(getContext())) {
            startTask("anuncios", new GetAnunciosTask(nome), R.id.progress);
        } else {
            alert(R.string.error_conexao_indisponivel);
        }
    }


    private SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Valida se existe conexão ao fazer o gesto Pull to Refresh
                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    taskAnuncios(true);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    snack(recyclerView, R.string.error_conexao_indisponivel);
                }
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskAnuncios(false);
    }

    private void taskAnuncios(boolean pullToRefresh) {
        startTask("anuncios", new GetAnunciosTask(null), pullToRefresh ? R.id.swipeToRefresh : R.id.progress);
    }

    // Task para buscar os produtos
    private class GetAnunciosTask implements TaskListener<List<Anuncio>> {

        private String nome;

        public GetAnunciosTask(String nome) {
            this.nome = nome;
        }

        @Override
        public List<Anuncio> execute() throws Exception {
//            return AnuncioService.getTodosAnuncios(getContext());
            if (nome != null) {
                return AnuncioService.getAnunciosByNomeProduto(getContext(), segmentoComercial, nome);
            }else {
                return AnuncioService.getAnuncios(getContext(), segmentoComercial, query);
            }
        }

        @Override
        public void updateView(List<Anuncio> anuncios) {
            if (anuncios != null) {
                ProdutosFragment.this.anuncios = anuncios;
                // Atualiza a view na UI Thread
                recyclerView.setAdapter(new AnuncioAdapter(getContext(), anuncios, onClickAnuncio()));
            } else {
//                Anuncio a = new Anuncio();
//                a.produto.nome = "Deslize para baixo para atualizar a lista!";
//                anuncios.add(a);
//                recyclerView.setAdapter(new AnuncioAdapter(getContext(), anuncios, onClickAnuncio()));
            }
        }

        @Override
        public void onError(Exception exception) {
            alert("Ocorreu algum erro ao buscar os anúncios. Atualize a lista novamente!");
        }

        @Override
        public void onCancelled(String cod) {

        }

    }

    private AnuncioAdapter.AnuncioOnClickListener onClickAnuncio() {
        return new AnuncioAdapter.AnuncioOnClickListener() {
            @Override
            public void onClickAnuncio(View view, int idx) {
                Anuncio a = anuncios.get(idx);
                Intent intent = new Intent(getContext(), AnuncioActivity.class);
                intent.putExtra("anuncio", Parcels.wrap(a));
                startActivity(intent);
            }

            @Override
            public void onLongClickAnuncio(View view, int idx) {
                toast("Clique Longo no Anúncio!");
            }
        };
    }

}

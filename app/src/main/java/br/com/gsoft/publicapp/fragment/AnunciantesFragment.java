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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import java.util.List;

import br.com.gsoft.publicapp.R;
import br.com.gsoft.publicapp.activity.AnuncianteActivity;
import br.com.gsoft.publicapp.adapter.AnuncianteAdapter;
import br.com.gsoft.publicapp.domain.Anunciante;
import br.com.gsoft.publicapp.service.AnuncianteService;
import livroandroid.lib.utils.AndroidUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnunciantesFragment extends BaseFragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean closeMenuSearch;

    protected RecyclerView recyclerView;
    private List<Anunciante> anunciantes;

    public AnunciantesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // ################## PARA QUE OS MENUS SE TORNEM VISÍVEIS NA TELA #################

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_anunciantes, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewAnunciantes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        // Swipe to Refresh
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefreshAnunciantes);
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
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    closeMenuSearch = false;
                } else {
                    closeMenuSearch = true;
                }
            }
        });

    }

    private SearchView.OnQueryTextListener onSearch() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                buscaAnunciantes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscaAnunciantes(newText);
                return false; // ************************************* true ??????????????
            }
        };
    }

    private void buscaAnunciantes(String nome) {
        // Atualiza ao fazer o gesto Swipe To Refresh
        if (AndroidUtils.isNetworkAvailable(getContext())) {
            startTask("anuncios", new GetAnunciantesTask(nome), R.id.progress);
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
                    taskAnunciantes(true);
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
        taskAnunciantes(false);
    }

    private void taskAnunciantes(boolean pullToRefresh) {
        startTask("anunciantes", new GetAnunciantesTask(null), pullToRefresh ? R.id.swipeToRefreshAnunciantes : R.id.progress);
    }

    // Task para buscar os anunciantes
    private class GetAnunciantesTask implements TaskListener<List<Anunciante>> {

        private String nome;

        public GetAnunciantesTask(String nome) {
            this.nome = nome;
        }

        @Override
        public List<Anunciante> execute() throws Exception {

            if (nome != null) {
                return AnuncianteService.getAnunciantesByNome(nome);
            }else {
                return AnuncianteService.getAnunciantes();
            }

        }

        @Override
        public void updateView(List<Anunciante> anunciantes) {
            if (anunciantes != null) {
                AnunciantesFragment.this.anunciantes = anunciantes;
                // Atualiza a view na UI Thread
                recyclerView.setAdapter(new AnuncianteAdapter(getContext(), anunciantes, onClickAnunciante()));
            }
        }

        @Override
        public void onError(Exception exception) {
            alert("Ocorreu algum erro ao buscar os anunciantes. Atualize a lista novamente!");
        }

        @Override
        public void onCancelled(String cod) {

        }

    }

    private AnuncianteAdapter.AnuncianteOnClickListener onClickAnunciante() {
        return new AnuncianteAdapter.AnuncianteOnClickListener() {

            @Override
            public void onClickAnunciante(View view, int idx) {
                Anunciante a = anunciantes.get(idx);
                //Toast.makeText(getContext(), "Evento: " + e.toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), AnuncianteActivity.class);
                intent.putExtra("anunciante", Parcels.wrap(a));
                startActivity(intent);
            }

            @Override
            public void onLongClickAnunciante(View view, int idx) {

            }

        };
    }

}

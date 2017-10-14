package br.com.gsoft.publicapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.gsoft.publicapp.R;
import br.com.gsoft.publicapp.adapter.SegmentoComercialListViewAdapter;
import br.com.gsoft.publicapp.domain.SegmentoComercial;
import br.com.gsoft.publicapp.service.SegmentoComercialService;


/**
 * Created by gervasio on 02/06/2017.
 */

public class BaseActivity extends livroandroid.lib.activity.BaseActivity {

    protected DrawerLayout drawerLayout;
    protected ListView drawerList;
    protected List<SegmentoComercial> segmentosComerciais = new ArrayList<SegmentoComercial>();

    protected String segmentoComercial = "";
    protected String query = "";

    protected boolean closeMenuSearch;

    protected void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

//    // Configura o Nav Drawer
//    protected void setupNavDrawer() {
//        // Drawer Layout
//        final ActionBar actionBar = getSupportActionBar();
//        // Ícone do menu do nav drawer
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        if (navigationView != null && drawerLayout != null) {
//            // Atualiza a imagem e textos do header
//            setNavViewValues(navigationView, R.string.nav_drawer_username, R.string.nav_drawer_email, R.mipmap.ic_launcher);
//            // Trata o evento de clique no menu.
//            navigationView.setNavigationItemSelectedListener(
//                    new NavigationView.OnNavigationItemSelectedListener() {
//                        @Override
//                        public boolean onNavigationItemSelected(MenuItem menuItem) {
//                            // Seleciona a linha
//                            menuItem.setChecked(true);
//                            // Fecha o menu
//                            drawerLayout.closeDrawers();
//                            // Trata o evento do menu
//                            onNavDrawerItemSelected(menuItem);
//                            return true;
//                        }
//                    });
//        }
//    }

    // Configura o Nav Drawer
    protected void setupNavDrawer() {

        // Drawer Layout
        final ActionBar actionBar = getSupportActionBar();

        // Ícone do menu do nav drawer
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

//        recyclerViewNavDrawer = (RecyclerView) findViewById(R.id.recyclerViewNavDrawer);
//        recyclerViewNavDrawer.setLayoutManager(new LinearLayoutManager(this));
//        recyclerViewNavDrawer.setItemAnimator(new DefaultItemAnimator());
//        recyclerViewNavDrawer.setHasFixedSize(true);

        taskSegmentosComerciais(); // ******************************************************

        drawerList.setOnItemClickListener(new DrawerItemClickListener());
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        Intent intent = new Intent(getContext(), AnunciosActivity.class);
        intent.putExtra("segmentoComercial", segmentosComerciais.get(position).nome);
        startActivity(intent);

        // Highlight the selected item, update the title, and close the drawer
        drawerList.setItemChecked(position, true);
        drawerLayout.closeDrawer(drawerList);
    }

//    protected SegmentoComercialAdapter.SegmentoComercialOnClickListener onClickSegmentoComercial() {
//        return new SegmentoComercialAdapter.SegmentoComercialOnClickListener() {
//            @Override
//            public void onClickSegmentoComercial(View view, int idx) {
//
//            }
//
//            @Override
//            public void onLongClickSegmentoComercial(View view, int idx) {
//
//            }
//        };
//    }


    /*
    // ***************************** THREAD *******************************
    */
    private void taskSegmentosComerciais() {
        new GetSegmentosComerciaisTask().execute();
    }

    private class GetSegmentosComerciaisTask extends AsyncTask<Void, Void, List<SegmentoComercial>> {

        @Override
        protected List<SegmentoComercial> doInBackground(Void... params) {
            try {
//                menu = SegmentoComercialService.getNomesSegmentosComerciais();
                segmentosComerciais = SegmentoComercialService.getSegmentosComerciais();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return segmentosComerciais;
        }

        @Override
        protected void onPostExecute(List<SegmentoComercial> result) {
            if (result != null){
//                drawerList.setAdapter(new ArrayAdapter<SegmentoComercial>(getContext(), android.R.layout.simple_list_item_1, result));
                drawerList.setAdapter(new SegmentoComercialListViewAdapter(getContext(), result));
//                recyclerViewNavDrawer.setAdapter(new SegmentoComercialAdapter(getActivity(), result, onClickSegmentoComercial()));
            }
        }

    }
    /*
    *********************************************************************
    */


//    static void setNavViewValues(NavigationView navView, int nome, int email, int img) {
//        View headerView = navView.getHeaderView(0);
//        TextView tNome = (TextView) headerView.findViewById(R.id.tUserName);
//        TextView tEmail = (TextView) headerView.findViewById(R.id.tUserEmail);
//        ImageView imgView = (ImageView) headerView.findViewById(R.id.imgUserPhoto);
//        tNome.setText(nome);
//        tEmail.setText(email);
//        imgView.setImageResource(img);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Trata o clique no botão que abre o menu.
                if (drawerLayout != null) {
                    openDrawer();
                    return true;
                }

        }
        return super.onOptionsItemSelected(item);
    }

    // Abre o menu lateral
    protected void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    // Fecha o menu lateral
    protected void closeDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    protected void replaceFragment(Fragment frag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag, "TAG").commit();
    }

}

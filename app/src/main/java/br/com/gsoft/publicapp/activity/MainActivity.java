package br.com.gsoft.publicapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.com.gsoft.publicapp.R;
import br.com.gsoft.publicapp.adapter.TabsAdapter;
import br.com.gsoft.publicapp.fragment.ProdutosFragment;

import static android.R.attr.key;
import static br.com.gsoft.publicapp.R.id.img;

public class MainActivity extends BaseActivity {

//    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main_nova);

        setUpToolbar(); // Ativa a Toolbar nesta Activity

        setupNavDrawer();

        setupViewPagerTabs();

        findViewById(R.id.btAddAnuncio).setOnClickListener(onClickAddAnuncio());

    }

    // **************** MÉTODO QUE TRATA O CLIQUE DO BOTÃO + PARA CADASTRAR UM ANÚNCIO ***************
    private View.OnClickListener onClickAddAnuncio() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Compat
                Intent intent = new Intent(getActivity(), AnuncioActivity.class);
                intent.putExtra("modoCadastroEdicao", true);
                // ??????????????????????????????????????????????????????????????????????????
//                ActivityOptionsCompat opts = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), img, key);
                ActivityCompat.startActivity(getActivity(), intent, null);
            }
        };
    }

    private void setupViewPagerTabs() {
        // ViewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new TabsAdapter(getContext(), getSupportFragmentManager(), segmentoComercial, query));

        // Tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        // Cria as tabas com o mesmo adapter utilizado pelo ViewPager
        tabLayout.setupWithViewPager(viewPager);
        int cor = ContextCompat.getColor(getContext(), R.color.white);
        // Cor branca no texto (o fundo azul foi definido no layout)
        tabLayout.setTabTextColors(cor, cor);
    }


    @Override
    public void onBackPressed() { // Ao pressionar o botão VOLTAR

        boolean fecharApp = true;

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
            fecharApp = false;
        }

//        if (searchView.isShown() && closeMenuSearch){
//            searchView.onActionViewCollapsed();
//            searchView.setQuery("", false);
//            closeMenuSearch = false;
//            fecharApp = false;
//        }

        if (fecharApp) {
            super.onBackPressed();
        }

    }

}

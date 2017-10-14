package br.com.gsoft.publicapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.gsoft.publicapp.R;
import br.com.gsoft.publicapp.fragment.ProdutosFragment;

public class AnunciosActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);

        setUpToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        segmentoComercial = getIntent().getStringExtra("segmentoComercial");
//        query = getIntent().getStringExtra("query");

        // Título
        getSupportActionBar().setTitle(segmentoComercial);

        // Adiciona o fragment com o mesmo Bundle (args) da intent
        if (savedInstanceState == null) {
            ProdutosFragment frag = ProdutosFragment.newInstance(segmentoComercial, query);;
//            frag.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container, frag).commit();
        }
    }
}

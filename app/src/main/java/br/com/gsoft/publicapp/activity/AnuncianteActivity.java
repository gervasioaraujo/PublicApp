package br.com.gsoft.publicapp.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import org.parceler.Parcels;

import java.io.File;

import br.com.gsoft.publicapp.R;
import br.com.gsoft.publicapp.domain.Anunciante;
import br.com.gsoft.publicapp.fragment.VisualizarAnuncianteFragment;
import br.com.gsoft.publicapp.fragment.BaseFragment;
import br.com.gsoft.publicapp.util.ImageUtils;

public class AnuncianteActivity extends BaseActivity {

    CollapsingToolbarLayout collapsingToolbar;

    private Anunciante anunciante;
    private ImageView appBarImg;
    private File file;
    //private FloatingActionButton fabButton;
    private AnuncianteActivity.ClickHeaderListener clickHeaderListener;


    // *********** Interface Interna à classe AnuncioActivity ************
    public interface ClickHeaderListener {
        void onHeaderClicked();
        //void onFabButtonClicked(Anuncio anuncio);
    }

    // ?????????????????????????????????
    public void setClickHeaderListener(AnuncianteActivity.ClickHeaderListener clickHeaderListener) {
        this.clickHeaderListener = clickHeaderListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anunciante);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Título da CollapsingToolbarLayout
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        // Header
        appBarImg = (ImageView) findViewById(R.id.appBarImg);
        appBarImg.setOnClickListener(onClickImgHeader());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.header_appbar);

        // Palleta cores
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int color = ContextCompat.getColor(getContext(), R.color.colorPrimary);
                int mutedColor = palette.getMutedColor(color);
                collapsingToolbar.setContentScrimColor(mutedColor);
            }
        });

        // Args
        this.anunciante = Parcels.unwrap(getIntent().getParcelableExtra("anunciante"));
        setAppBarInfo(anunciante);


        // Definindo qual o Fragment vai ser aberto na tela (Cadastro e Edição ou somente Visualização)
        if (savedInstanceState == null) {
            BaseFragment frag = new VisualizarAnuncianteFragment();
            frag.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.layoutFrag, frag,"frag").commit();
        }

    }

    // Método que configura a tela para Cadastro ou Edição de um anúncio
    public void setAppBarInfo(Anunciante a) {
        if(a != null) {
            String nome = a.nome;
            String url = a.urlFoto;

            collapsingToolbar.setTitle(nome);
            setImage(url);
        } else {
            // Novo Anúncio
            collapsingToolbar.setTitle(getString(R.string.novo_anuncio));
        }
    }

    public void setImage(String url) {
        ImageUtils.setImage(this,url, appBarImg);
    }

    // Método listener para o clique na imagem do anúncio
    private View.OnClickListener onClickImgHeader() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickHeaderListener != null) {
                    // Delegate para notificar o fragment que teve clique.
                    clickHeaderListener.onHeaderClicked();
                }
            }
        };
    }

}

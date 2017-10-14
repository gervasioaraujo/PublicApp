package br.com.gsoft.publicapp.adapter;

/**
 * Created by rlech on 9/26/2015.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.gsoft.publicapp.R;
import br.com.gsoft.publicapp.domain.Anuncio;


public class AnuncioAdapter extends RecyclerView.Adapter<AnuncioAdapter.AnunciosViewHolder> {

    //protected static final String TAG = "projeto preços";
    private final List<Anuncio> anuncios;
    private final Context context;
    private AnuncioOnClickListener anuncioOnClickListener;

    // ********** INTERFACE **********
    public interface AnuncioOnClickListener {
        public void onClickAnuncio(View view, int idx);
        public void onLongClickAnuncio(View view, int idx);
    }

    public AnuncioAdapter(Context context, List<Anuncio> anuncios, AnuncioOnClickListener anuncioOnClickListener) {
        this.context = context;
        this.anuncios = anuncios;
        this.anuncioOnClickListener = anuncioOnClickListener;
    }


    @Override
    public int getItemCount() {
        return this.anuncios != null ? this.anuncios.size() : 0;
    }


    @Override
    public AnunciosViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_produto_2, viewGroup, false);
        AnunciosViewHolder holder = new AnunciosViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(final AnunciosViewHolder holder, final int position) {
        // Atualiza a view
        Anuncio a = anuncios.get(position);
        holder.tvNomeProduto.setText(a.produto.nome);
        holder.tvPrecoProduto.setText(a.produto.getPrecoString());
        holder.tvNomeAnunciante.setText(a.anunciante.nome);
        holder.progress.setVisibility(View.VISIBLE);

        // Faz o download da foto e mostra o ProgressBar
        Picasso.with(context).load(a.produto.urlFoto).fit().into(holder.imgFotoProduto,
                new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progress.setVisibility(View.GONE); // download ok
                    }

                    @Override
                    public void onError() {
                        holder.progress.setVisibility(View.GONE);
                    }
                });

        // Click normal
        if (anuncioOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // A variável position é final
                    anuncioOnClickListener.onClickAnuncio(holder.itemView, position);
                }
            });
        }
        // Click longo
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                anuncioOnClickListener.onLongClickAnuncio(holder.itemView, position);
                return true;
            }
        });

        // Pinta o fundo da cor primária se a linha estiver selecionada
        //int corFundo = context.getResources().getColor(p.isSelected() ? R.color.colorPrimary : R.color.white);

        //holder.cardView.setCardBackgroundColor(corFundo); // ***********************************************************

        // A cor do texto é branca ou azul, depende da cor do fundo
//        int corFonte = context.getResources().getColor(a.produto.selected ? R.color.white : R.color.black);
//        holder.tvNomeProduto.setTextColor(corFonte);

    }

    // ViewHolder com as views
    public static class AnunciosViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNomeProduto;
        ImageView imgFotoProduto;
        public TextView tvPrecoProduto;
        public TextView tvNomeAnunciante;
        ProgressBar progress;

        public AnunciosViewHolder(View view) {
            super(view);
            // Cria as views para salvar no ViewHolder
            tvNomeProduto = (TextView) view.findViewById(R.id.nome_do_produto_2);
            imgFotoProduto = (ImageView) view.findViewById(R.id.foto_do_produto_2);
            tvPrecoProduto = (TextView) view.findViewById(R.id.preco_do_produto_2);
            tvNomeAnunciante = (TextView) view.findViewById(R.id.estabelecimento);
            progress = (ProgressBar) view.findViewById(R.id.progressImg);
        }

    }

}


package br.com.gsoft.publicapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
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
import br.com.gsoft.publicapp.domain.Anunciante;


/**
 * Created by gervasio on 07/07/2017.
 */

public class AnuncianteAdapter extends RecyclerView.Adapter<AnuncianteAdapter.AnunciantesViewHolder> {

    private final List<Anunciante> anunciantes;
    private final Context context;
    private AnuncianteOnClickListener anuncianteOnClickListener;

    public AnuncianteAdapter(Context context, List<Anunciante> anunciantes, AnuncianteOnClickListener
            anuncianteOnClickListener) {
        this.context = context;
        this.anunciantes = anunciantes;
        this.anuncianteOnClickListener = anuncianteOnClickListener;
    }

    // ********** INTERFACE **********
    public interface AnuncianteOnClickListener {
        public void onClickAnunciante(View view, int idx);
        public void onLongClickAnunciante(View view, int idx);
    }

    @Override
    public int getItemCount() {
        return this.anunciantes != null ? this.anunciantes.size() : 0;
    }

    @Override
    public AnunciantesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_anunciante, viewGroup, false);
        AnunciantesViewHolder holder = new AnunciantesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AnunciantesViewHolder holder, final int position) {
        // Atualiza a view
        Anunciante a = anunciantes.get(position);
        holder.tNome.setText(a.nome);
        holder.progress.setVisibility(View.VISIBLE);
        // Faz o download da foto e mostra o ProgressBar
        Picasso.with(context).load(a.urlFoto).fit().into(holder.img,
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
        if (anuncianteOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // A variável position é final
                    anuncianteOnClickListener.onClickAnunciante(holder.itemView, position);
                }
            });
        }
        // Click longo
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                anuncianteOnClickListener.onLongClickAnunciante(holder.itemView, position);
                return true;
            }
        });
        // Pinta o fundo de azul se a linha estiver selecionada
//        int corFundo = context.getResources().getColor(ec.isSelected() ? R.color.colorPrimary : R.color.white);
//        holder.cardView.setCardBackgroundColor(corFundo);
//        // A cor do texto é branca ou azul, depende da cor do fundo
//        int corFonte = context.getResources().getColor(ec.isSelected() ? R.color.white : R.color.colorPrimary);
//        holder.tNome.setTextColor(corFonte);
    }

    // ViewHolder com as views
    public static class AnunciantesViewHolder extends RecyclerView.ViewHolder {

        public TextView tNome;
        ImageView img;
        ProgressBar progress;
        CardView cardView;

        public AnunciantesViewHolder(View view) {
            super(view);
            // Cria as views para salvar no ViewHolder
            tNome = (TextView) view.findViewById(R.id.text);
            img = (ImageView) view.findViewById(R.id.img);
            progress = (ProgressBar) view.findViewById(R.id.progressImg);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }

}

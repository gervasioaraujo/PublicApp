package br.com.gsoft.publicapp.adapter;

/**
 * Created by rlech on 9/26/2015.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.gsoft.publicapp.R;
import br.com.gsoft.publicapp.domain.SegmentoComercial;


public class SegmentoComercialAdapter extends RecyclerView.Adapter<SegmentoComercialAdapter.SegmentoComercialViewHolder> {

    private final List<SegmentoComercial> segmentosComerciais;
    private final Context context;
    private SegmentoComercialOnClickListener segmentoComercialOnClickListener;

    // ********** INTERFACE **********
    public interface SegmentoComercialOnClickListener {
        public void onClickSegmentoComercial(View view, int idx);
        public void onLongClickSegmentoComercial(View view, int idx);
    }

    public SegmentoComercialAdapter(Context context, List<SegmentoComercial> segmentosComerciais, SegmentoComercialOnClickListener segmentoComercialOnClickListener) {
        this.context = context;
        this.segmentosComerciais = segmentosComerciais;
        this.segmentoComercialOnClickListener = segmentoComercialOnClickListener;
    }


    @Override
    public int getItemCount() {
        return this.segmentosComerciais != null ? this.segmentosComerciais.size() : 0;
    }


    @Override
    public SegmentoComercialViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_navdrawer, viewGroup, false);
        SegmentoComercialViewHolder holder = new SegmentoComercialViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(final SegmentoComercialViewHolder holder, final int position) {
        // Atualiza a view
        SegmentoComercial s = segmentosComerciais.get(position);
        holder.tvNomeSegmento.setText(s.nome);

        // Click normal
        if (segmentoComercialOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // A variável position é final
                    segmentoComercialOnClickListener.onClickSegmentoComercial(holder.itemView, position);
                }
            });
        }
        // Click longo
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                segmentoComercialOnClickListener.onLongClickSegmentoComercial(holder.itemView, position);
                return true;
            }
        });

    }

    // ViewHolder com as views
    public static class SegmentoComercialViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNomeSegmento;

        public SegmentoComercialViewHolder(View view) {
            super(view);
            // Cria as views para salvar no ViewHolder
            tvNomeSegmento = (TextView) view.findViewById(R.id.nomeSegmentoComercial);
        }

    }

}


package br.com.gsoft.publicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import br.com.gsoft.publicapp.R;
import br.com.gsoft.publicapp.domain.SegmentoComercial;

/**
 * Created by gervasio on 03/10/2017.
 */

public class SegmentoComercialListViewAdapter extends BaseAdapter {

    private final Context context;
    private final List<SegmentoComercial> segmentosComerciais;

    public SegmentoComercialListViewAdapter(Context context, List<SegmentoComercial> segmentosComerciais) {
        this.context = context;
        this.segmentosComerciais = segmentosComerciais;
    }

    @Override
    public int getCount() {
        return segmentosComerciais != null ? segmentosComerciais.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return segmentosComerciais.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_navdrawer, parent, false);
        TextView t = (TextView) view.findViewById(R.id.nomeSegmentoComercial);
        SegmentoComercial s = segmentosComerciais.get(position);
        t.setText(s.nome);
        return view;
    }

}

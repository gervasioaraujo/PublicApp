package br.com.gsoft.publicapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import br.com.gsoft.publicapp.R;
import br.com.gsoft.publicapp.fragment.AnunciantesFragment;
import br.com.gsoft.publicapp.fragment.ProdutosFragment;


/**
 * Created by gervasio on 05/06/2017.
 */

public class TabsAdapter extends FragmentPagerAdapter {

    private Context context;
    private String segmentoComercial;
    private String query;

    public TabsAdapter(Context context, FragmentManager fm, String segmentoComercial, String query) {
        super(fm);
        this.context = context;
        this.segmentoComercial = segmentoComercial;
        this.query = query;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.tab1);
        }
        return context.getString(R.string.tab2);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        if (position == 0) {
            f = ProdutosFragment.newInstance(this.segmentoComercial, this.query);
        } else {
            f = new AnunciantesFragment();
        }
        return f;
    }
}

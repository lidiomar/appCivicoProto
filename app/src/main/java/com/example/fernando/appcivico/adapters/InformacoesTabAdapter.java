package com.example.fernando.appcivico.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.fernando.appcivico.fragments.AvaliarFragment;
import com.example.fernando.appcivico.fragments.InformacoesFragment;


/**
 * Created by fernando on 23/05/16.
 */
public class InformacoesTabAdapter extends FragmentStatePagerAdapter
{
    private Context context;

    public InformacoesTabAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(position == 0) {
            fragment = new InformacoesFragment();

        }else if(position == 1) {
            fragment = new InformacoesFragment();
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        if(position == 0) {
            title = "Informações";
        }else if (position == 1) {
            title = "Avaliações";
        }

        return title;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(Object object) {
        return 1;
    }
}
package com.example.fernando.appcivico.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.adapters.InformacoesTabAdapter;


/**
 * Created by fernando on 23/05/16.
 */
public class InformacoesFragmentTab extends Fragment {
    private TabLayout tab;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.informacoes_fragment_tab,container,false);

        tab = (TabLayout)view.findViewById(R.id.avaliacaoTab);

        viewPager = (ViewPager)view.findViewById(R.id.avaliacaoViewPager);

        viewPager.setAdapter(new InformacoesTabAdapter(getContext(),getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(0);
        tab.setSelectedTabIndicatorColor(Color.WHITE);
        tab.setTabTextColors(Color.WHITE, Color.WHITE);
        tab.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        tab.setupWithViewPager(viewPager);

        return view;
    }

}

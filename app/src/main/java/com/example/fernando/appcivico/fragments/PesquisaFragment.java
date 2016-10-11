package com.example.fernando.appcivico.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.utils.Constants;

import java.util.Arrays;

/**
 * Created by fernando on 11/10/16.
 */
public class PesquisaFragment extends Fragment{
    private Spinner spinnerCategoria;
    private SeekBar seekBar;
    private TextView seekBarValue;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesquisa, container,false);
        spinnerCategoria = (Spinner) view.findViewById(R.id.spinner_pesquisa_categoria);
        this.inicializaCategoria();
        seekBar = (SeekBar)view.findViewById(R.id.seekbar_pesquisa_km);
        seekBarValue = (TextView)view.findViewById(R.id.seekbar_value);
        this.configuraSeekBar();
        return view;
    }


    private void inicializaCategoria() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_item ,Arrays.asList(Constants.CATEGORIAS));
        spinnerCategoria.setAdapter(arrayAdapter);
    }

    private void configuraSeekBar() {
        seekBar.setProgress(1);
        seekBar.incrementProgressBy(10);
        seekBar.setMax(500);
        seekBarValue.setText(String.format(this.getActivity().getResources().getString(R.string.x_km), String.valueOf(seekBar.getProgress())));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue.setText(String.format(PesquisaFragment.this.getActivity().getResources().getString(R.string.x_km),String.valueOf(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}

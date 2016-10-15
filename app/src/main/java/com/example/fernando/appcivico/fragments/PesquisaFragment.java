package com.example.fernando.appcivico.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.activities.MapsActivity;
import com.example.fernando.appcivico.estrutura.Categoria;
import com.example.fernando.appcivico.estrutura.Estabelecimento;
import com.example.fernando.appcivico.servicos.Servicos;
import com.example.fernando.appcivico.utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by fernando on 11/10/16.
 */
public class PesquisaFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Spinner spinnerCategoria;
    private SeekBar seekBar;
    private TextView seekBarValue;
    private EditText buscaTexto;
    private Button buttonPesquisar;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private double latitude;
    private double longitude;
    private Boolean fail = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesquisa, container, false);
        spinnerCategoria = (Spinner) view.findViewById(R.id.spinner_pesquisa_categoria);
        this.inicializaCategoria();
        seekBar = (SeekBar) view.findViewById(R.id.seekbar_pesquisa_km);
        seekBarValue = (TextView) view.findViewById(R.id.seekbar_value);
        this.configuraSeekBar();

        if (this.googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this.getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        buscaTexto = (EditText) view.findViewById(R.id.edt_busca_texto);

        final BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(this.getActivity()).inflate(R.layout.layout_popup, null);
        final PopupWindow popupWindow = BubblePopupHelper.create(this.getActivity(), bubbleLayout);


        buscaTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location = new int[2];
                v.getLocationInWindow(location);
                bubbleLayout.setArrowDirection(ArrowDirection.TOP);
                TextView txtPopup = (TextView) bubbleLayout.findViewById(R.id.layout_popup_text);
                txtPopup.setText("Informe um termo para auxiliar na busca. \n\n " +
                        "Exemplos: \n " +
                        " - Endoscopia \n " +
                        " - Fisioterapia \n " +
                        " - Pediatra \n " +
                        " - Nutricionista \n " +
                        "\n etc.");
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], v.getHeight() + location[1]);
            }
        });

        buttonPesquisar = (Button) view.findViewById(R.id.buscar_pesquisa);
        buttonPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonPesquisar();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    private void inicializaCategoria() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, Arrays.asList(Constants.CATEGORIAS));
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
                seekBarValue.setText(String.format(PesquisaFragment.this.getActivity().getResources().getString(R.string.x_km), String.valueOf(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(PesquisaFragment.this.getActivity(), "É necessário permitir o envio das informações de localização", Toast.LENGTH_SHORT).show();
            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
        }else {
            fail = true;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        fail = true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        fail = true;
    }

    public void onClickButtonPesquisar() {

        if(fail) {
            Toast.makeText(PesquisaFragment.this.getActivity(), this.getActivity().getString(R.string.algo_deu_errado), Toast.LENGTH_SHORT).show();
            return;
        }

        String categoriaId = "";
        String texto = "";
        try {
            categoriaId = URLEncoder.encode((((Categoria) spinnerCategoria.getSelectedItem()).getId()), "UTF-8");
            texto = URLEncoder.encode(buscaTexto.getText().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        int raio = seekBar.getProgress();
        final double lat = this.latitude;
        final double lng = this.longitude;

        Response.Listener respListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                String stringJson = String.valueOf(response).replace("\"long\":", "\"longitude\":");
                Estabelecimento[] estabelecimentos = gson.fromJson(stringJson, Estabelecimento[].class);
                Intent intent = new Intent(PesquisaFragment.this.getActivity(), MapsActivity.class);
                intent.putExtra("estabelecimentos", estabelecimentos);
                intent.putExtra("latitudeUsuario", lat);
                intent.putExtra("longitudeUsuario", lng);

                startActivity(intent);
            }
        };

        Servicos servicos = new Servicos(PesquisaFragment.this.getActivity());
        servicos.consultaEstabelecimentoLatLong(respListener, lat, lng, raio, texto, categoriaId);

    }

    public void onClickOld() {
        String categoriaId = "";
        String texto = "";
        try {
            categoriaId = URLEncoder.encode((((Categoria) spinnerCategoria.getSelectedItem()).getId()), "UTF-8");
            texto = URLEncoder.encode(buscaTexto.getText().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        int progress = seekBar.getProgress();
        final double lat;
        final double lng;

        LocationManager locationManager = (LocationManager) PesquisaFragment.this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(PesquisaFragment.this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PesquisaFragment.this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(PesquisaFragment.this.getActivity(), "É necessário permitir o envio das informações de localização", Toast.LENGTH_SHORT).show();
        } else {
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            lat = location.getLatitude();
            lng = location.getLongitude();

            Response.Listener respListener = new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Gson gson = new Gson();
                    String stringJson = String.valueOf(response).replace("\"long\":", "\"longitude\":");
                    Estabelecimento[] estabelecimentos = gson.fromJson(stringJson, Estabelecimento[].class);
                    Intent intent = new Intent(PesquisaFragment.this.getActivity(), MapsActivity.class);
                    intent.putExtra("estabelecimentos", estabelecimentos);
                    intent.putExtra("latitudeUsuario", lat);
                    intent.putExtra("longitudeUsuario", lng);

                    startActivity(intent);
                }
            };

            Servicos servicos = new Servicos(PesquisaFragment.this.getActivity());
            servicos.consultaEstabelecimentoLatLong(respListener, lat, lng, progress, texto, categoriaId);

        }
    }
}

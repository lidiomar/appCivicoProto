package com.example.fernando.appcivico.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.activities.MapsActivity;
import com.example.fernando.appcivico.estrutura.Categoria;
import com.example.fernando.appcivico.estrutura.Estabelecimento;
import com.example.fernando.appcivico.servicos.Servicos;
import com.example.fernando.appcivico.utils.Constants;
import com.example.fernando.appcivico.utils.MyAlertDialogFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * Created by fernando on 11/10/16.
 */
public class PesquisaMapaFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int MY_PERMISSION_REQUEST = 1;
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
    private ImageView imageCidade;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesquisa_mapa, container, false);
        spinnerCategoria = (Spinner) view.findViewById(R.id.spinner_pesquisa_categoria);
        imageCidade = (ImageView) view.findViewById(R.id.image_cidade);

        Picasso.with(this.getActivity()).load(R.drawable.localizacao).fit().into(imageCidade);

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

    @Override
    public void onResume() {
        buttonPesquisar.setEnabled(true);
        super.onResume();
    }

    private void inicializaCategoria() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, Arrays.asList(Constants.CATEGORIAS));
        spinnerCategoria.setAdapter(arrayAdapter);
    }

    private void configuraSeekBar() {
        seekBar.setProgress(1);
        seekBar.incrementProgressBy(10);
        seekBar.setMax(100);
        seekBarValue.setText(String.format(this.getActivity().getResources().getString(R.string.x_km), String.valueOf(seekBar.getProgress())));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue.setText(String.format(PesquisaMapaFragment.this.getActivity().getResources().getString(R.string.x_km), String.valueOf(progress)));
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

        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST);
        }else {
            getCoordinates();
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

        if (fail) {
            Toast.makeText(PesquisaMapaFragment.this.getActivity(), this.getActivity().getString(R.string.algo_deu_errado), Toast.LENGTH_SHORT).show();
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

                if (estabelecimentos == null || estabelecimentos.length <= 0) {
                    Toast.makeText(PesquisaMapaFragment.this.getActivity(), PesquisaMapaFragment.this.getString(R.string.nao_ha_resultados), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(PesquisaMapaFragment.this.getActivity(), MapsActivity.class);

                    intent.putExtra("estabelecimentos", estabelecimentos);
                    intent.putExtra("latitudeUsuario", lat);
                    intent.putExtra("longitudeUsuario", lng);
                    buttonPesquisar.setEnabled(false);
                    startActivity(intent);
                    PesquisaMapaFragment.this.getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PesquisaMapaFragment.this.getActivity(), PesquisaMapaFragment.this.getActivity().getString(R.string.algo_deu_errado), Toast.LENGTH_LONG).show();
            }
        };

        Servicos servicos = new Servicos(PesquisaMapaFragment.this.getActivity());

        final MyAlertDialogFragment myAlertDialogFragment = MyAlertDialogFragment.newInstance("", "");
        myAlertDialogFragment.show(getFragmentManager(), "");

        JsonArrayRequest jsonArrayRequest = servicos.consultaEstabelecimentoLatLong(lat, lng, raio, texto, categoriaId, respListener, errorListener);
        RequestQueue requestQueue = servicos.getRequestQueue();
        requestQueue.add(jsonArrayRequest);

        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                if (myAlertDialogFragment != null) {
                    myAlertDialogFragment.dismiss();
                }
            }
        });
    }

    public void getCoordinates() {

        if (ActivityCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
        } else {
            fail = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCoordinates();
                } else {
                    Toast.makeText(PesquisaMapaFragment.this.getActivity(), "Sem permissão", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}


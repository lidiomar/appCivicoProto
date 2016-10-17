package com.example.fernando.appcivico.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.estrutura.Estabelecimento;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Estabelecimento[] estabelecimentos = null;
    private double lat;
    private double lng;
    private TextView textoInformativo;
    String clickedMarkerId = null;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        Bundle extras = getIntent().getExtras();

        textoInformativo = (TextView) findViewById(R.id.texto_informativo);

        Object[] estabelecimentosObj = (Object[]) extras.get("estabelecimentos");
        lat = (double) extras.get("latitudeUsuario");
        lng = (double) extras.get("longitudeUsuario");

        if (estabelecimentosObj != null) {
            estabelecimentos = new Estabelecimento[estabelecimentosObj.length];
            System.arraycopy(estabelecimentosObj, 0, estabelecimentos, 0, estabelecimentos.length);
        }


        if (estabelecimentos == null || estabelecimentos.length <= 0) {
            Toast.makeText(MapsActivity.this, "Não há resultados para a busca", Toast.LENGTH_SHORT).show();
        } else {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final HashMap<String, Estabelecimento> hashEstabelecimentos = new HashMap<>();

        for (Estabelecimento estabelecimento : estabelecimentos) {
            LatLng latLng = new LatLng(estabelecimento.getLat(), estabelecimento.getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(estabelecimento.getNomeFantasia()).snippet("> Mais informações"));
            hashEstabelecimentos.put(marker.getId(), estabelecimento);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String id = marker.getId();
                Estabelecimento estabelecimento = hashEstabelecimentos.get(id);
                if(estabelecimento != null) {
                    if(clickedMarkerId == null || !clickedMarkerId.equals(id)) {
                        clickedMarkerId = id;

                    }else {
                        clickedMarkerId = null;
                        startActivityInfo(estabelecimento);
                    }
                }
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String id = marker.getId();
                Estabelecimento estabelecimento = hashEstabelecimentos.get(id);
                if(estabelecimento != null) {
                    startActivityInfo(estabelecimento);
                }
            }
        });
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(13)
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        LatLng latLng = new LatLng(lat, lng);
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Sua localização").snippet("Encontre estabelecimentos próximos a você"));
        marker.showInfoWindow();
    }


    private void startActivityInfo(final Estabelecimento estabelecimento) {
        Intent intent = new Intent(MapsActivity.this, InformacoesActivity.class);
        intent.putExtra("estabelecimento", estabelecimento);
        startActivity(intent);
    }
}

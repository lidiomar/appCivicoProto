package com.example.fernando.appcivico.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.activities.InformacoesActivity;
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

/**
 * Created by fernando on 17/10/16.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Estabelecimento[] estabelecimentos = null;
    private double lat;
    private double lng;
    private TextView textoInformativo;
    String clickedMarkerId = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps,container,false);


        Bundle extras = getActivity().getIntent().getExtras();

        textoInformativo = (TextView)view.findViewById(R.id.texto_informativo);

        Object[] estabelecimentosObj = (Object[]) extras.get("estabelecimentos");
        lat = (double) extras.get("latitudeUsuario");
        lng = (double) extras.get("longitudeUsuario");

        if (estabelecimentosObj != null) {
            estabelecimentos = new Estabelecimento[estabelecimentosObj.length];
            System.arraycopy(estabelecimentosObj, 0, estabelecimentos, 0, estabelecimentos.length);
        }


        if (estabelecimentos == null || estabelecimentos.length <= 0) {
            Toast.makeText(MapsFragment.this.getActivity(), "Não há resultados para a busca", Toast.LENGTH_SHORT).show();
        } else {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);
        }

        return view;
    }

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
        Intent intent = new Intent(MapsFragment.this.getActivity(), InformacoesActivity.class);
        intent.putExtra("estabelecimento", estabelecimento);
        startActivity(intent);
    }
}

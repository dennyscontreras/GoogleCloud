package com.example.googlecloud;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    GoogleMap mMap;
    List<LatLng> lstlongitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lstlongitud=new ArrayList<>();
    }
    ///46.53553340462967, 102.49990890627434
    //63.90163828409896, -18.994459814402173
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setZoomControlsEnabled(true);


        CameraUpdate camUpd1 =
                CameraUpdateFactory
                        .newLatLngZoom(new LatLng(40.68931426278643, -74.04459172111524), 18);
        mMap.moveCamera(camUpd1);
        mMap.setOnMapClickListener(this);


    }
    private String calcularDistanciaTotal(List<LatLng> puntos) {
        double distanciaTotal = 0;
        if (puntos.size() >= 2) {
            for (int i = 0; i < puntos.size() - 1; i++) {
                LatLng punto1 = puntos.get(i);
                LatLng punto2 = puntos.get(i + 1);

                Location loc1 = new Location("");
                loc1.setLatitude(punto1.latitude);
                loc1.setLongitude(punto1.longitude);

                Location loc2 = new Location("");
                loc2.setLatitude(punto2.latitude);
                loc2.setLongitude(punto2.longitude);

                distanciaTotal += loc1.distanceTo(loc2);
            }
        }


        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(distanciaTotal);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        LatLng punto = new LatLng(latLng.latitude, latLng.longitude);
        MarkerOptions marcador = new MarkerOptions();
        marcador.position(latLng);
        marcador.title("Punto");
        mMap.addMarker(marcador);
        lstlongitud.add(latLng);

        if (lstlongitud.size() == 6) {
            String distanciaTotalFormatted = calcularDistanciaTotal(lstlongitud);
            Toast.makeText(this, "Distancia total acumulada: " + distanciaTotalFormatted + " metros", Toast.LENGTH_LONG).show();

            PolylineOptions lineas = new PolylineOptions();
            for (LatLng puntoMarcado : lstlongitud) {
                lineas.add(puntoMarcado);
            }
            lineas.add(lstlongitud.get(0));
            lineas.width(8);
            lineas.color(Color.RED);
            mMap.addPolyline(lineas);
        }
    }


}

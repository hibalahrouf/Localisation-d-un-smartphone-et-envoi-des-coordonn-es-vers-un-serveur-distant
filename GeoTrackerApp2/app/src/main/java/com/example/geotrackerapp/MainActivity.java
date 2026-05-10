package com.example.geotrackerapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private double lat;
    private double lng;
    private double alt;
    private float  precision;
    private RequestQueue requestQueue;
    private TextView tvStatus;

    private String serverEndpoint = "http://10.0.2.2/geotracker/saveGpsPoint.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus     = findViewById(R.id.tvStatus);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, 1);
            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                45000,
                100,
                new LocationListener() {

                    @Override
                    public void onLocationChanged(Location location) {
                        lat       = location.getLatitude();
                        lng       = location.getLongitude();
                        alt       = location.getAltitude();
                        precision = location.getAccuracy();

                        String display =
                                "Latitude  : " + lat +
                                        "\nLongitude : " + lng +
                                        "\nAltitude  : " + alt +
                                        "\nAccuracy  : " + precision + " m";

                        tvStatus.setText(display);
                        Toast.makeText(getApplicationContext(), display,
                                Toast.LENGTH_LONG).show();

                        sendGpsData(lat, lng);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        String label;
                        switch (status) {
                            case LocationProvider.OUT_OF_SERVICE:
                                label = "OUT_OF_SERVICE"; break;
                            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                                label = "TEMPORARILY_UNAVAILABLE"; break;
                            case LocationProvider.AVAILABLE:
                                label = "AVAILABLE"; break;
                            default:
                                label = "UNKNOWN";
                        }
                        Toast.makeText(getApplicationContext(),
                                "Provider " + provider + " status: " + label,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        Toast.makeText(getApplicationContext(),
                                "Provider enabled: " + provider,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Toast.makeText(getApplicationContext(),
                                "Provider disabled: " + provider,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void sendGpsData(final double fixLat, final double fixLng) {

        Log.d("GEOTRACKER", "Trying to send: " + fixLat + " / " + fixLng);
        Log.d("GEOTRACKER", "URL: " + serverEndpoint);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                serverEndpoint,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("GEOTRACKER", "Server response: " + response);
                        Toast.makeText(getApplicationContext(),
                                response, Toast.LENGTH_SHORT).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("GEOTRACKER", "Volley error: " + error.toString());
                        if (error.networkResponse != null) {
                            Log.e("GEOTRACKER", "Status code: " + error.networkResponse.statusCode);
                        }
                        Toast.makeText(getApplicationContext(),
                                "Error: " + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                // Safe device ID — works on all Android versions, no permission needed
                String deviceId = Settings.Secure.getString(
                        getContentResolver(),
                        Settings.Secure.ANDROID_ID
                );

                params.put("latitude",    String.valueOf(fixLat));
                params.put("longitude",   String.valueOf(fixLng));
                params.put("captured_at", sdf.format(new Date()));
                params.put("device_id",   deviceId);

                Log.d("GEOTRACKER", "Params: " + params.toString());
                return params;
            }
        };

        requestQueue.add(request);
    }
}
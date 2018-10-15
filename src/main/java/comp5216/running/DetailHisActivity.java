package comp5216.running;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailHisActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<LatLng> latlngs  = new ArrayList<LatLng>();
    private String duration, date, distance, speed, pace;
    private TextView duration_text, date_text, distance_text, speed_text, pace_text;
    List<Map<String,Object>> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historydetail);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map3);
        mapFragment.getMapAsync(this);

        datalist = new ArrayList<Map<String,Object>>() ;


        duration = getIntent().getStringExtra("duration");
        date = getIntent().getStringExtra("date");
        distance=getIntent().getStringExtra("distance");
        speed=getIntent().getStringExtra("speed");
        pace=getIntent().getStringExtra("pace");



        duration_text = (TextView) findViewById(R.id.result_time);
        date_text = (TextView) findViewById(R.id.result_date);
        distance_text = (TextView) findViewById(R.id.result_distance);
        speed_text = (TextView) findViewById(R.id.result_speed);
        pace_text = (TextView) findViewById(R.id.result_pace);

        duration_text.setText("Total Time: "+ duration);
        date_text.setText("Date: " + date);
        distance_text.setText("Distance: " + distance);
        speed_text.setText("Speed: " + speed);
        pace_text.setText("Pace: " + pace);

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
        String routeString = (String) getIntent().getStringExtra("list") ;
        String [] list1 = routeString.split(";");
        for (String s:list1){
            String [] list2 = s.split(",");
            if (list2[0] != null && list2[1] != null){
                double lat = Double.parseDouble(list2[0]);
                double lng = Double.parseDouble(list2[1]);

                LatLng loc = new LatLng(lat,lng);
                latlngs.add(loc);
            }

        }

        PolylineOptions rectOptions = new PolylineOptions();
        rectOptions.addAll(latlngs);
        rectOptions.color(Color.BLUE);
        Polyline polyline = mMap.addPolyline(rectOptions);

        // Add a marker in Sydney and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngs.get(0),15));
    }






    public void Back(View view) {
        finish();
    }
}

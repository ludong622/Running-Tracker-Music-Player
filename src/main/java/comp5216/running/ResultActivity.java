package comp5216.running;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ResultActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<LatLng> latlngs ;
    private String duration;
    private TextView duration_text, date_text, distance_text,speed_text, pace_text;
    private String date;
    private String item;
    private String distance;
    private String speed;
    private String pace;
    private String key = "lyx";
    List<Map<String,Object>> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        datalist = new ArrayList<Map<String,Object>>() ;
        readItemsFromFile();

        latlngs=(ArrayList<LatLng>) getIntent().getSerializableExtra("list");
        duration = getIntent().getStringExtra("duration");
        date = getIntent().getStringExtra("date");
        distance=getIntent().getStringExtra("distance");
        speed=getIntent().getStringExtra("speed");
        pace=getIntent().getStringExtra("pace");
        item = "Running History: "+"\n\t"+"Duration   "+duration+"\n\t"+"Distance  "+distance+"\n\t"+"Speed  "+speed+"\n\t"+"Pace  "+pace;



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


        Savehash();






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
        List<LatLng> latlngs=(ArrayList<LatLng>) getIntent().getSerializableExtra("list");
        PolylineOptions rectOptions = new PolylineOptions();
        rectOptions.addAll(latlngs);
        rectOptions.color(Color.BLUE);
        Polyline polyline = mMap.addPolyline(rectOptions);
        mMap.addMarker(new MarkerOptions().position(latlngs.get(0)).title("Marker"));
        mMap.addMarker(new MarkerOptions().position(latlngs.get(latlngs.size()-1)).title("Marker"));


        // Add a marker in Sydney and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngs.get(0),15));
    }

    public void Homepage(View view) {
        finish();
    }

    public void Savehash(){
        Map<String,Object> map = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();


        // put List to string
        for (LatLng loc :latlngs){
            double lat = loc.latitude;
            double lng = loc.longitude;
            sb.append(lat + ","+lng+";" ) ;
        }
        String routeString = sb.toString();


        map.put("duration", duration);
        map.put("date",date);
        map.put("route",routeString);
        map.put("item",item);
        map.put("distance",distance);
        map.put("speed",speed);
        map.put("pace",pace);
        datalist.add(map);
        saveItemsToFile();

    }

    private void readItemsFromFile(){


        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        SharedPreferences sp = this.getSharedPreferences("finals", Context.MODE_PRIVATE);
        String result = sp.getString(key, "");
        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject itemObject = array.getJSONObject(i);
                Map<String, Object> itemMap = new HashMap<String, Object>();
                JSONArray names = itemObject.names();
                if (names != null) {
                    for (int j = 0; j < names.length(); j++) {
                        String name = names.getString(j);
                        String value = itemObject.getString(name);
                        itemMap.put(name, value);
                    }
                }
                datas.add(itemMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        datalist = datas;

    }
    private void saveItemsToFile(){
        JSONArray mJsonArray = new JSONArray();
        for (int i = 0; i < datalist.size(); i++) {
            Map<String, Object> itemMap = datalist.get(i);
            Iterator<Map.Entry<String, Object>> iterator = itemMap.entrySet().iterator();

            JSONObject object = new JSONObject();

            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();

                try {
                    object.put(entry.getKey(), entry.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mJsonArray.put(object);
        }

        SharedPreferences sp = this.getSharedPreferences("finals", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, mJsonArray.toString());
        editor.commit();
    }

}

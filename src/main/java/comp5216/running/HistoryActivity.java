package comp5216.running;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class HistoryActivity extends AppCompatActivity {
    ListView listview;
    SimpleAdapter simp_adapter;
    List<Map<String,Object>> datalist;
    Context mContext;
    String key = "lyx";
    TextView tv ;
    double tDuration, tDistance, aSpeed, aPace;
    String AS, AP, TDi, TDu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        datalist = new ArrayList<Map<String,Object>>() ;

        listview = (ListView) findViewById(R.id.listview);
        tv = (TextView) findViewById(R.id.monthavg);
        readItemsFromFile();
        simp_adapter = new SimpleAdapter(this,datalist,R.layout.item,new String[]{"item","date"},new int[]{R.id.item,R.id.date});
        listview.setAdapter(simp_adapter);


        //button
        mContext = this;

        if (datalist!=null) {
            for (Map<String,Object> mp : datalist) {
                String dur = (String)mp.get("duration");
                String dis = (String)mp.get("distance");
                double dura = Double.parseDouble(dur.substring(0, dur.length()-2));
                double dist = Double.parseDouble(dis.substring(0, dis.length()-2));
                tDuration+=dura;
                tDistance+=dist;
            }
            DecimalFormat decimalFormat=new DecimalFormat("0.00");
            DecimalFormat decimalFormat1=new DecimalFormat("0.000");

            aSpeed = tDistance/tDuration/1000*3600;
            aPace = tDuration/tDistance/60*1000;
            AS=decimalFormat.format(aSpeed);
            AP=decimalFormat.format(aPace);
            TDu=decimalFormat.format(tDuration/60);
            TDi=decimalFormat1.format(tDistance/1000);

            tv.setText("Weekly Analysis: "+"\n\t  "+"Total Distance: "+TDi+" km"+"\n\t  "+"Total Duration: "+TDu+" min"+"\n\t  "+"Average Speed: "+AS+" km/h"+"\n\t  "+"Average Pace: "+AP+" min/km");
        }else {
            tv.setText("Weekly Analysis: "+"\n\t  "+"Total Distance: "+0+"\n\t  "+"Total Duration: "+0+"\n\t  "+"Average Speed: "+0+"\n\t  "+"Average Pace: "+0);
        }
        setupListViewListener();


    }
    private void setupListViewListener() {

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String date = (String) datalist.get(position).get("date");
                String duration = (String) datalist.get(position).get("duration");
                String routeString = (String) datalist.get(position).get("route");
                String distance = (String) datalist.get(position).get("distance");
                String speed = (String) datalist.get(position).get("speed");
                String pace = (String) datalist.get(position).get("pace");

                Intent intent = new Intent(HistoryActivity.this, DetailHisActivity.class);
                    // put "extras" into the bundle for access in the edit activity
                intent.putExtra("list", routeString);
                intent.putExtra("duration",duration);
                intent.putExtra("date",date);
                intent.putExtra("distance",distance);
                intent.putExtra("speed",speed);
                intent.putExtra("pace",pace);
                startActivity(intent);

            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long rowId) {
                Log.i("MainActivity", "Long Clicked item " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //delete the item
                                datalist.remove(position);
                                simp_adapter.notifyDataSetChanged();
                                saveItemsToFile();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //User cancelled the dialog
                                //nothing happens
                            }
                        });

                builder.create().show();
                return true;
            }
        });
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

    public void Home(View view) {
        finish();
    }
}

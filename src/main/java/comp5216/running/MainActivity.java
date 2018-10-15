package comp5216.running;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    public static final String MA = "MainActivity";
    private EditText distance, duration;
    private TextView speed_text, pace_text;
    private String speed, pace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        distance = (EditText)findViewById(R.id.distance);
        duration = (EditText)findViewById(R.id.duration);
        speed_text = (TextView)findViewById(R.id.speed);
        pace_text = (TextView)findViewById(R.id.pace);
    }

    protected void onStart(){
        super.onStart();
        Log.w(MA, "inside MainActivity:onStart \n");
    }

    protected void onRestart(){
        super.onRestart();
        Log.w(MA, "inside MainActivity:onRestart \n");
    }

    protected void onResume(){
        super.onResume();
        Log.w(MA, "inside MainActivity:onResume \n");
    }

    protected void onPause(){
        super.onPause();
        Log.w(MA, "inside MainActivity:onPause \n");
    }

    protected void onStop(){
        super.onStop();
        Log.w(MA, "inside MainActivity:onStop \n");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.w(MA, "inside MainActivity:onDestroy \n");
    }

    public void modifyData(View v){
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        speed = decimalFormat.format(Double.valueOf(distance.getText().toString())/Double.valueOf(duration.getText().toString()));
        pace = decimalFormat.format(Double.valueOf(duration.getText().toString())/Double.valueOf(distance.getText().toString()));
        speed_text.setText(speed);
        pace_text.setText(pace);
    }

    public void goHomepage(View view){
        finish();
    }


}

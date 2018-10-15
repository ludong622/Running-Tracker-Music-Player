package comp5216.running;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FirstPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
    }


    public void toMusic(View view) {
        Intent intent = new Intent(this,MusicActivity.class );
        startActivity(intent);
    }

    public void toRun(View view) {
        Intent intent = new Intent(this,MapsActivity.class );
        startActivity(intent);
    }

    public void toHistory(View view) {
        Intent intent = new Intent(this,HistoryActivity.class);
        startActivity(intent);
    }
    public void toCalclutor(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}

package comp5216.running;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener{
    static MediaPlayer mp;
    ArrayList<File>mySongs;
    int position;
    Uri u;
    Thread updateSeekBar;
    SeekBar sb;
    Button btPlay, btPause, btNxt, btPv, back;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btPlay = (Button)findViewById(R.id.btPlay);
        btPause = (Button)findViewById(R.id.btPause);
        btNxt=(Button)findViewById(R.id.btNxt);
        btPv=(Button)findViewById(R.id.btPv);
        back=(Button)findViewById(R.id.back);
        name=(TextView)findViewById(R.id.name);

        btPlay.setOnClickListener(this);
        btPause.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btPv.setOnClickListener(this);
        back.setOnClickListener(this);

        sb=(SeekBar) findViewById(R.id.seekBar);
        updateSeekBar = new Thread(){
            public void  run(){

                int totalDuration =mp.getDuration();
                int currentPosition= 0;

                while(currentPosition<=totalDuration){

                    try{
                        sleep(500);
                        currentPosition=mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                }
                //super.run();
            }
        };

        if(mp!=null){
            mp.stop();
            mp.release();
        }

        Intent i= getIntent();
        Bundle b=i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songList");
        position = b.getInt("pos",0);

        u = Uri.parse(mySongs.get(position).toString());
        Log.i("123",Integer.toString(position)+"  "+u);
        mp=MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        sb.setMax(mp.getDuration());

        updateSeekBar.start();


        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mp.seekTo(seekBar.getProgress());
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id= v.getId();
        switch(id){
            case R.id.btPlay:
                btPlay.setVisibility(View.INVISIBLE);
                btPause.setVisibility(View.VISIBLE);
                mp.pause();
                break;

            case R.id.btPause:
                btPause.setVisibility(View.INVISIBLE);
                btPlay.setVisibility(View.VISIBLE);
                mp.start();
                break;

            case R.id.btNxt:

                if (position+1<=mySongs.size()-1) {
                    mp.stop();
                    mp.release();
                    position=position+1;
                    u = Uri.parse(mySongs.get(position).toString());
                    mp=MediaPlayer.create(getApplicationContext(),u);
                    mp.start();
                    sb.setMax(mp.getDuration());
                    break;
                }

            case R.id.btPv:

                if(position-1>=0) {
                    mp.stop();
                    mp.release();
                    position = position - 1;
                    u = Uri.parse(mySongs.get(position).toString());
                    mp = MediaPlayer.create(getApplicationContext(), u);
                    mp.start();
                    sb.setMax(mp.getDuration());

                }else {
                    mp.stop();
                    mp.release();
                    u = Uri.parse(mySongs.get(position).toString());
                    mp = MediaPlayer.create(getApplicationContext(), u);
                    mp.start();
                    sb.setMax(mp.getDuration());
                }

                break;

            case R.id.back:
                finish();
                break;

        }
    }

    public static MediaPlayer getMp() {
        return mp;
    }
}

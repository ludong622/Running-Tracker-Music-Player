package comp5216.running;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class RunMusicActivity extends AppCompatActivity {

    ListView lv;
    String[] items;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_music);
        lv=(ListView)findViewById(R.id.lvPlaylist);

        final ArrayList<File> mySongs=findSongs(Environment.getExternalStorageDirectory());

        items= new String[mySongs.size()];

        for(int i =0;i<mySongs.size();i++){
            //toast(mySongs.get(i).getName().toString());
            items[i]=mySongs.get(i).getName().toString().replace(".mp3","");

        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(),R.layout.song_layout,R.id.textView,items);

        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(getApplicationContext(),RunPlayer.class).putExtra("pos",position).putExtra("songList",mySongs));
            }
        });


    }

    public ArrayList<File> findSongs(File root){

        ArrayList<File>al =new ArrayList<File>();
        File[]files = root.listFiles();
        for(File singleFile : files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                al.addAll(findSongs(singleFile));

            }
            else{
                if(singleFile.getName().endsWith(".mp3")||singleFile.getName().endsWith(".wav")){
                    al.add(singleFile);

                }
            }
        }
        return al;
    }

    public void Home(View view) {
        finish();
    }

}

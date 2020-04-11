package com.example.task7;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ArrayList<Music> arrayList;
    private CustomMusicAdapter adapter;
    private ListView songList;
    private MediaPlayer mediaPlayer;
    private boolean flag = true;
    private long check_id;
    private boolean isPause = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songList = findViewById(R.id.songList);
        arrayList = new ArrayList<>();


        //Adding song-items to list
        arrayList.add(new Music("Symphony No. 2 in D major", "Berlin Symphony Orchestra", R.raw.symphony));
        arrayList.add(new Music("Интерлюдия Сказки Гофмана", "Жак Оффенбах", R.raw.swiss));
        arrayList.add(new Music("Warrior of the rest", "Various artists", R.raw.warriors));


        adapter = new CustomMusicAdapter(this, R.layout.custom_item_music, arrayList);



        songList.setAdapter(adapter);

        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //Fist click on song will create MediaPlayer and will start playing clicked song
                if (flag == true ) {
                    Toast.makeText(MainActivity.this, "First launch", Toast.LENGTH_SHORT).show();
                    mediaPlayer = MediaPlayer.create(MainActivity.this, arrayList.get(position).getSong());
                    flag = false;
                }
                //If positions of clicked tracks don't match and song is playing
                //or if song is on pause, MediaPlayer will be playing new song that we clicked
                if(check_id != position && mediaPlayer.isPlaying() || isPause && check_id != position) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = MediaPlayer.create(MainActivity.this, arrayList.get(position).getSong());
                    flag = false;
                   
                }
                //Sets song on pause
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    isPause = true;


                //This else block will definitely run on first click
                }else{
                    //Getting position of playing track so we can define which song to play
                    check_id = position;
                    //Starting player
                    mediaPlayer.start();
                    //Setting onCompleteListener, when the song will be completed, MediaPlayer will be released
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            flag = true;
                        }
                    });

                }
            }
        });

        //On LongClick MediaPlayer will stop playing
        songList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!flag) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    flag = true;
                }

                return true;
            }
        });
    }
}
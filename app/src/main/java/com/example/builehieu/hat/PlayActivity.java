package com.example.builehieu.hat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import wseemann.media.FFmpegMediaMetadataRetriever;

import static com.example.builehieu.hat.R.*;
import static com.example.builehieu.hat.R.drawable.*;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer mediaPlayer;
    TextView txtSongName, txtSingerName, txtDuration, txtPlaytime;
    ArrayList<File> mySong;
    int position;
    Button btPlay, btNext, btPrevious;
    Uri uri;

    int index, minTime, secondTime,i;

    ImageView imgAvartaSong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_play);

        init();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mySong = (ArrayList) bundle.getParcelableArrayList("song");
        position = bundle.getInt("pos", 0);
        Uri uri = Uri.parse(mySong.get(position).toString());

        getImg(mySong.get(position).toString());

        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);


        mediaPlayer.start();

        new Thread(new TimeSongPlay()).start();
    }

    private void setTypeFont() {

        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/icielBrandonText-Bold.ttf");

        txtSongName.setTypeface(typeface);
        txtSingerName.setTypeface(typeface);
        txtDuration.setTypeface(typeface);
        txtPlaytime.setTypeface(typeface);


    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.btPlay:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    index = 0;
                    btPlay.setBackgroundResource(play);
                } else {
                    mediaPlayer.start();
                    btPlay.setBackgroundResource(ic_pause_black_24dp);
                }
                break;
            case R.id.btNext:
                ChangeSong(1);
                break;
            case R.id.btPrevious:
                ChangeSong(-1);
                break;

        }

    }


    void getImg(String s) {

        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(s);

        secondTime = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000+1;


        byte[] data = mmr.getEmbeddedPicture();
        txtSingerName.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        txtSingerName.setSelected(true);

        txtSongName.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        txtSongName.setSelected(true);



        txtDuration.setText(secondTime / 60 + (":" + secondTime % 60+"00" ).substring(0, 3));
        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            imgAvartaSong.setImageBitmap(bitmap); //associated cover art in bitmap
            imgAvartaSong.setAdjustViewBounds(true);
        } else {
            imgAvartaSong.setImageResource(none);
        }


    }

    private void init() {

        index = minTime = 0;
        txtSingerName = (TextView) findViewById(id.txtSingerName);
        txtSongName = (TextView) findViewById(id.txtSongName);
        txtDuration = (TextView) findViewById(id.txtDuration);
        txtPlaytime = (TextView) findViewById(id.txtPlaytime);
        imgAvartaSong = (ImageView) findViewById(id.img_avatar_song);


        btPlay = (Button) findViewById(id.btPlay);
        btNext = (Button) findViewById(id.btNext);
        btPrevious = (Button) findViewById(id.btPrevious);

        btPlay.setOnClickListener(this);
        btPrevious.setOnClickListener(this);
        btNext.setOnClickListener(this);

        setTypeFont();
    }


    public class TimeSongPlay implements Runnable {
        @Override
        public void run() {

            for (i = 0; i < secondTime; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if ((i + 1) % 60 == 0 && i != 0) {
                    index = -1;
                    minTime++;
                }

                index++;

                runOnUiThread(new Thread(new IUTimeSongPlay()));
            }

        }
    }

    public class IUTimeSongPlay implements Runnable {
        @Override
        public void run() {

            if (i == secondTime) {
                index = minTime=0;
                mediaPlayer.stop();
                mediaPlayer.release();

                position = (position + 1) % mySong.size();
                uri = Uri.parse(mySong.get(position).toString());
                getImg(mySong.get(position).toString());

                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);

                mediaPlayer.start();
            }

            if(minTime<10)
            {
                if(index<10)
                {
                    txtPlaytime.setText("0"+minTime + ":" + "0" + index);
                }
                else
                {
                    txtPlaytime.setText("0"+minTime + ":" +  index);

                }
            }
            else
            {
                if(index<10)
                {
                    txtPlaytime.setText(minTime + ":" + "0" + index);
                }
                else
                {
                    txtPlaytime.setText(minTime + ":" +  index);

                }            }




        }
    }

    private void ChangeSong(int p) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();

        }
        index =minTime=i= 0;
        txtPlaytime.setText("00:00");
        mediaPlayer.release();
        btPlay.setBackgroundResource(ic_pause_black_24dp);

        position = (position + p) % mySong.size();
        uri = Uri.parse(mySong.get(position).toString());
        getImg(mySong.get(position).toString());

        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);

        mediaPlayer.start();




    }


}

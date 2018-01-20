package com.example.julius.mp3_soitin;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.julius.mp3_soitin.entities.Track;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerFragment extends Fragment {

    private Button b1,b2,b3,b4;
    private ImageView iv;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    private double startTime = 0;
    private double finalTime = 0;

    private Handler myHandler;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1,tx2,tx3;

    private Track currentTrack;

    private boolean handlerCreated, initialized;

    public PlayerFragment() {
        initialized = false;
        handlerCreated = false;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

    }

    @Override
    public void onPause(){
        super.onPause();
        /*try {
            myHandler.getLooper().wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        b1 = (Button) v.findViewById(R.id.button);
        b2 = (Button) v.findViewById(R.id.button2);
        b3 = (Button)v.findViewById(R.id.button3);
        b4 = (Button)v.findViewById(R.id.button4);
        iv = (ImageView)v.findViewById(R.id.imageView);

        tx1 = (TextView)v.findViewById(R.id.textView2);
        tx2 = (TextView)v.findViewById(R.id.textView3);
        tx3 = (TextView)v.findViewById(R.id.textView4);
        tx3.setText("Song.mp3");
        //MediaPlayer.c
        //mediaPlayer = MediaPlayer.create(this, R.raw.song);
        seekbar = (SeekBar)v.findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        if(initialized) {
            b2.setEnabled(true);
            b3.setEnabled(false);
            finalTime = mediaPlayer.getDuration();
            startTime = mediaPlayer.getCurrentPosition();//Kun mediaplyer reset tämä on taas nolla
            seekbar.setMax((int) finalTime);
            seekbar.setProgress((int)startTime);
            initialized = false;
        }else{
            b2.setEnabled(false);
        }
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Playing sound",Toast.LENGTH_SHORT).show();
                mediaPlayer.start();

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();//Kun mediaplyer reset tämä on taas nolla

                seekbar.setMax((int) finalTime);

                tx2.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        finalTime)))
                );

                tx1.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        startTime)))
                );

                setUpHandler();

                seekbar.setProgress((int)startTime);
                b2.setEnabled(true);
                b3.setEnabled(false);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Pausing sound",Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
                b2.setEnabled(false);
                b3.setEnabled(true);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp+forwardTime)<=finalTime){
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getContext(),"You have Jumped forward 5 seconds",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"Cannot jump forward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp-backwardTime)>0){
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getContext(),"You have Jumped backward 5 seconds",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"Cannot jump backward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tx1.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
            Log.d("UUUU", Thread.currentThread().getName());
        }
    };

    public void setTrack(Track track){
        if(currentTrack != null && currentTrack.getPath().equals(track.getPath())){
            //Käyttäjä toistettavan musiikin uudelleen
            initialized = true;
        }else{
            //Käyttäjä valinnu eri kappaleen
            currentTrack = track;
            if(mountSong()){

            }
        }
    }

    private boolean mountSong(){
        try {
            mediaPlayer.reset(); //Oleellinen
            Log.d("UUUU" , "Path : " + currentTrack.getPath());
            mediaPlayer.setDataSource(currentTrack.getPath());
            mediaPlayer.prepare();
            //mediaPlayer.start();
        } catch (IOException e) {
            Log.d("UUUU", "Virhe " + e.getMessage());
            return false;
        }
        return true;
    }

    private void setUpHandler(){
        if(!handlerCreated){
            myHandler = new Handler();
            myHandler.postDelayed(UpdateSongTime,100);
            handlerCreated = true;
        }
    }
}

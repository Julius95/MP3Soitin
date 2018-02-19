package com.example.julius.mp3_soitin;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

    private Button b1, pauseB, playB,b4;
    private ImageView iv;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    private double startTime = 0;
    private double finalTime = 0;

    private Handler myHandler;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tekstiNykyinenKohta, tekstiKokoAika,tx3;

    private Track currentTrack;

    private boolean update;

    private boolean touchingSeekbar;

    private boolean handlerCreated, trackChanged;

    public PlayerFragment() {
        trackChanged = false;
        update = false;
        setUpHandler();
        touchingSeekbar = false;
        //handlerCreated = false;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
    }

    @Override
    public void onStop(){
        super.onStop();
        update = false;
        myHandler.removeCallbacks(UpdateSongTime);
        //myHandler = null;
        Log.d("UUUU", "STOPPED");
    }

    @Override
    public void onResume(){
        //Tarkistetaan onko raita ladattu onnistuneesti
        if(currentTrack == null){
            //Virhe ladattaessa raitaa
            playB.setEnabled(false);
            pauseB.setEnabled(false);
            b4.setEnabled(false);
            b1.setEnabled(false);
            update = false;
            super.onResume();
        }
        //Raita on ladattu
        //Päivitä näkymää ja aseta päivitys päälle
        update = true;
        tx3.setText(currentTrack.getName());

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();//Kun mediaplyer reset tämä on taas nolla
        seekbar.setMax((int) finalTime);
        seekbar.setProgress((int)startTime);

        //Aseta Tekstit kokoajalle ja nykyiselle kohdalle
        tekstiKokoAika.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)))
        );

        tekstiNykyinenKohta.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );
        /*if(pauseB.isEnabled()){
            playB.setEnabled(false);
        }else{
            playB.setEnabled(true);
        }*/
        Log.d("UUUU", "ONRESUME");
        if(myHandler!=null)
            myHandler.postDelayed(UpdateSongTime,100);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        Log.d("UUUU", "BUILD VIEW");
        b1 = (Button) v.findViewById(R.id.forward_button);
        pauseB = (Button) v.findViewById(R.id.button_pause);
        playB = (Button)v.findViewById(R.id.play_button);
        b4 = (Button)v.findViewById(R.id.backwards_button);
        iv = (ImageView)v.findViewById(R.id.imageView);

        tekstiNykyinenKohta = (TextView)v.findViewById(R.id.textCurrentPosition);
        tekstiKokoAika = (TextView)v.findViewById(R.id.textKokoAika);
        tx3 = (TextView)v.findViewById(R.id.textView4);
        seekbar = (SeekBar)v.findViewById(R.id.seekBar);
        seekbar.setClickable(true);
        Log.d("UUUU", " listeners------------------------------- " + seekbar.hasOnClickListeners());
        //Aseta kuuntelija seekbarille
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            public void onStopTrackingTouch(SeekBar seekBar){
                mediaPlayer.seekTo(seekbar.getProgress());
                touchingSeekbar = false;
            }

            public void onStartTrackingTouch(SeekBar seekBar){
                touchingSeekbar = true;
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                if(fromUser){
                    startTime = mediaPlayer.getCurrentPosition();
                    seekbar.setProgress(seekbar.getProgress());
                }
            }
        });

        if(!trackChanged) {
            if(mediaPlayer.isPlaying()){
                //Näkymä rakennettiin kun samaan aikaan soitetaan musiikkia
                playB.setEnabled(false);
            }else{
                pauseB.setEnabled(false);
            }
        }else{
            pauseB.setEnabled(false);
        }

        //Painikkeiden kuuntelijat
        playB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Playing sound",Toast.LENGTH_SHORT).show();
                mediaPlayer.start();

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();//Kun mediaplyer reset tämä on taas nolla

                seekbar.setMax((int) finalTime);

                update = true;
                seekbar.setProgress((int)startTime);
                pauseB.setEnabled(true);
                playB.setEnabled(false);
            }
        });

        pauseB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Pausing sound",Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
                pauseB.setEnabled(false);
                playB.setEnabled(true);
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
            if(seekbar==null)
                return;
            Log.d("UUUU", Thread.currentThread().getName());
            if(update){
                startTime = mediaPlayer.getCurrentPosition();
                tekstiNykyinenKohta.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime)))
                );
                if(!touchingSeekbar)
                    seekbar.setProgress((int)startTime);
                myHandler.postDelayed(this, 100);
            }
        }
    };

    public void setTrack(Track track){
        if(currentTrack != null && currentTrack.getPath().equals(track.getPath())){
            //Käyttäjä valitsee toistettavan musiikin uudelleen
            trackChanged = false;
        }else{
            //Käyttäjä valinnu eri kappaleen
            currentTrack = track;
            trackChanged = true;
            if(!mountSong()){
                //Virhe ladattaessa kappaletta
                currentTrack = null;
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
        if(myHandler == null){
            myHandler = new Handler();
            //handlerCreated = true;
            myHandler.postDelayed(UpdateSongTime,100);
        }
    }
}

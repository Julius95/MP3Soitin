package com.example.julius.mp3_soitin.views.player;

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

import com.example.julius.mp3_soitin.R;

import java.util.concurrent.TimeUnit;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerFragment extends Fragment implements PlayerContract.View{

    private Button b1, pauseB, playB,b4;
    private ImageView iv;

    private SeekBar seekbar;
    private TextView tekstiNykyinenKohta, tekstiKokoAika,tx3, title;

    private boolean touchingSeekbar;

    private PlayerContract.Presenter presenter;

    public PlayerFragment() {
        touchingSeekbar = false;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
    }

    @Override
    public void onStop(){
        super.onStop();
        presenter.stop();
        Log.d("UUUU", "STOPPED");
    }

    @Override
    public void onResume(){
        //Tarkistetaan onko raita ladattu onnistuneesti
        /*if(currentTrack == null){
            //Virhe ladattaessa raitaa
            playB.setEnabled(false);
            pauseB.setEnabled(false);
            b4.setEnabled(false);
            b1.setEnabled(false);
            update = false;
            super.onResume();
        }*/
        //Raita on ladattu
        //Päivitä näkymää ja aseta päivitys päälle
//        tx3.setText(currentTrack.getName());

        //Aseta Tekstit kokoajalle ja nykyiselle kohdalle


        /*if(pauseB.isEnabled()){
            playB.setEnabled(false);
        }else{
            playB.setEnabled(true);
        }*/
        Log.d("UUUU", "ONRESUME");
        super.onResume();
        presenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        b1 = v.findViewById(R.id.forward_button);
        pauseB = v.findViewById(R.id.button_pause);
        playB = v.findViewById(R.id.play_button);
        b4 = v.findViewById(R.id.backwards_button);
        iv = v.findViewById(R.id.imageView);

        tekstiNykyinenKohta = v.findViewById(R.id.textCurrentPosition);
        tekstiKokoAika = v.findViewById(R.id.textKokoAika);
        tx3 = v.findViewById(R.id.textView4);
        seekbar = v.findViewById(R.id.seekBar);
        title = v.findViewById(R.id.title);
        seekbar.setClickable(true);
        Log.d("UUUU", " listeners------------------------------- " + seekbar.hasOnClickListeners());
        //Aseta kuuntelija seekbarille
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            public void onStopTrackingTouch(SeekBar seekBar){
                //mediaPlayer.seekTo(seekbar.getProgress());//jumpTo
                presenter.jumpTo(seekBar.getProgress());
                touchingSeekbar = false;
            }

            public void onStartTrackingTouch(SeekBar seekBar){
                touchingSeekbar = true;
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                if(fromUser){
                    //startTime = mediaPlayer.getCurrentPosition();
                    seekbar.setProgress(seekbar.getProgress());
                }
            }
        });

        //if(!trackChanged) {
        /*if(presenter.isPlaying()){
            //Näkymä rakennettiin kun samaan aikaan soitetaan musiikkia
            playB.setEnabled(false);
        }else{
            pauseB.setEnabled(false);
        }*/

        //Painikkeiden kuuntelijat
        playB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Playing sound",Toast.LENGTH_SHORT).show();
                presenter.play();
            }
        });

        pauseB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Pausing sound",Toast.LENGTH_SHORT).show();
                presenter.pausePlaying();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.nextSong();
            }
        });

        b4.setOnClickListener((View view) -> presenter.previousSong());
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setPresenter(PlayerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setCurrentTimeText(double currentTime) {
        tekstiNykyinenKohta.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) currentTime),
                TimeUnit.MILLISECONDS.toSeconds((long) currentTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                currentTime)))
        );
    }

    @Override
    public void setEndTimeText(double endTime) {
        tekstiKokoAika.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) endTime),
                TimeUnit.MILLISECONDS.toSeconds((long) endTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                endTime)))
        );
    }

    @Override
    public void setCurrentTimeSeekBar(double currentTime) {
        if(!touchingSeekbar)
            seekbar.setProgress((int)currentTime);
    }

    @Override
    public void setEndTimeSeekBar(double endTime) {
        seekbar.setMax((int) endTime);
    }

    @Override
    public void setTitleText(String title) {
        this.title.setText(title);
    }

    @Override
    public void setToPlayStatus() {
        playB.setEnabled(false);
        pauseB.setEnabled(true);
    }

    @Override
    public void setToPauseStatus() {
        playB.setEnabled(true);
        pauseB.setEnabled(false);
    }

    @Override
    public void notifyUser(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void disableButtons() {
        playB.setEnabled(false);
        pauseB.setEnabled(false);
    }
}

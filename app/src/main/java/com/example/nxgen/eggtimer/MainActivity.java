package com.example.nxgen.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.ikovac.timepickerwithseconds.TimePicker;

import java.util.Calendar;


//TODO make app adapt with landscape mode
//TODO make app show in taskbar when minimized
//TODO make the alarm loud automatically
//TODO create menu for additional customizations

public class MainActivity extends AppCompatActivity {

    int hour = 0;
    int muinute = 0;
    int second = 0;
    CountDownTimer countDownTimer;
    boolean counterIsActive = false;
    ImageView img;
    MediaPlayer mplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.start_button);
        img.setImageResource(R.drawable.play_button);
        mplayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void timePickerDisplay(View view){
        final TextView timeDisplay = (TextView) findViewById(R.id.time);

        MyTimePickerDialog mTimePicker = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {


            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {

				timeDisplay.setText(String.format("%02d", hourOfDay)+
						":" + String.format("%02d", minute) +
						":" + String.format("%02d", seconds));

				Log.i("stat",String.valueOf(hourOfDay)+":"+String.valueOf(minute)+":"+String.valueOf(seconds));
				hour = hourOfDay;
				muinute = minute;
				second = seconds;
            }
        }, hour, muinute, second, true);
        mTimePicker.show();
    }

    public void startTimer(View view){
        if(counterIsActive ==false) {
            long totalTime = ((hour * 60 * 60) + (muinute * 60) + second) * 1000;
            counterIsActive = true;
            img = (ImageView) findViewById(R.id.start_button);
            img.setImageResource(R.drawable.stop_button);
            if(mplayer.isPlaying()){
                mplayer.stop();
            }


            countDownTimer = new CountDownTimer(totalTime + 100, 1000) {
                public void onTick(long millisecondsUntilDone) {
                    //Code when timer is ticking
                    updateTimer((int) millisecondsUntilDone / 1000);
                }

                public void onFinish() {
                    //code when timer is done
                    Log.i("FINISHED!", "the timer is finished");
                    updateTimer(0);
                    hour = 0;
                    muinute = 0;
                    second = 0;
                    mplayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
                    mplayer.start();
                    counterIsActive = false;
                    img = (ImageView) findViewById(R.id.start_button);
                    img.setImageResource(R.drawable.play_button);
                }
            }.start();
        } else{
            stopTimer();
        }

    }

    public void updateTimer(long seconds){
        final TextView timeDisplay = (TextView) findViewById(R.id.time);
        int tHour = (int) seconds/3600;
        int tMinute = (int) ((seconds-(tHour*3600))/60);
        int tSeconds = (int) (seconds-((tMinute*60)+(tHour*3600)));

        timeDisplay.setText(String.format("%02d",tHour)+
                ":" + String.format("%02d",tMinute) +
                ":" + String.format("%02d",tSeconds)
        );
    }

    public void stopTimer(){
        updateTimer(0);
        hour = 0;
        muinute = 0;
        second = 0;
        countDownTimer.cancel();
        counterIsActive = false;
        img = (ImageView) findViewById(R.id.start_button);
        img.setImageResource(R.drawable.play_button);
        mplayer.stop();
    }

}

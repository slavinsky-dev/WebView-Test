package com.slavinskydev.trafficdevilstest.screens.game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.slavinskydev.trafficdevilstest.screens.loader.LoaderActivity;
import com.slavinskydev.trafficdevilstest.R;
import com.slavinskydev.trafficdevilstest.screens.loader.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity implements GameView {

    boolean doubleBackToExitPressedOnce = false;

    private TextView textViewButtonStart;
    private TextView textViewButtonClickMe;
    private TextView textViewResult;
    private TextView textViewBestReaction;

    private long startTime, endTime, currentReaction, bestReaction;
    private int countDownTimer = 20000;
    private List<Long> reaction = new ArrayList<>();

    private ProgressBar progressBar;

    private int randomTop;

    private SensorManager sensorManager;
    private float accelerationValue;
    private float accelerationLastValue;
    private float shake;

    private GamePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        textViewButtonStart = findViewById(R.id.textViewButtonStart);
        textViewButtonClickMe = findViewById(R.id.textViewButtonClickMe);
        textViewResult = findViewById(R.id.textViewResult);
        textViewBestReaction = findViewById(R.id.textViewBestReaction);
        progressBar = findViewById(R.id.progressBar);


        presenter = new GamePresenter(this);

        textViewButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startGame();
            }
        });

        textViewButtonClickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clickMeButton();
            }
        });
    }

    public void startClickMe() {
        int random = (int) (Math.random() * countDownTimer / 20 + 500);
        if (countDownTimer > 1500) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    randomTop = (int) (Math.random() * 1400 + 100);
                    textViewButtonClickMe.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                    params.setMargins(0, randomTop, 0, 0);
                    textViewButtonClickMe.setLayoutParams(params);
                    startTime = System.currentTimeMillis();
                }
            }, random);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }
        accelerationValue = SensorManager.GRAVITY_EARTH;
        accelerationLastValue = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;
    }

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            accelerationLastValue = accelerationValue;
            accelerationValue = (float) Math.sqrt(x * x + y * y + z * z);
            float delta = accelerationValue - accelerationLastValue;
            shake = shake * 0.9f + delta;
            if (shake > 25) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                SharedPreferencesManager.activateFirstRun();
                                Intent intent = new Intent(GameActivity.this, LoaderActivity.class);
                                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(GameActivity.this);
                ab.setMessage(getResources().getString(R.string.first_start))
                        .setCancelable(true)
                        .setPositiveButton(getResources().getString(R.string.answer_yes), dialogClickListener)
                        .setNegativeButton(getResources().getString(R.string.answer_no), dialogClickListener)
                        .show();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    public void startGame() {
        textViewResult.setVisibility(View.GONE);
        textViewBestReaction.setVisibility(View.GONE);
        textViewBestReaction.setText("");
        textViewButtonStart.setVisibility(View.GONE);
        reaction.clear();
        progressBar.setProgress(countDownTimer);
        countDownTimer = 20000;

        new CountDownTimer(20000, 20) {
            public void onTick(long millisUntilFinished) {
                countDownTimer = countDownTimer - 20;
                progressBar.setProgress(countDownTimer - 200);
            }

            public void onFinish() {
                textViewButtonClickMe.setVisibility(View.GONE);
                textViewButtonStart.setVisibility(View.VISIBLE);
                textViewBestReaction.setVisibility(View.INVISIBLE);
                textViewButtonStart.setText(R.string.play_again);
                countDownTimer = 0;
                progressBar.setProgress(0);
                if (reaction.size() != 0) {
                    int minIndex = reaction.indexOf(Collections.min(reaction));
                    bestReaction = reaction.get(minIndex);
                    final AlphaAnimation alphaAnimationTitle = new AlphaAnimation(0.0f, 1.0f);
                    alphaAnimationTitle.setStartOffset(0);
                    alphaAnimationTitle.setDuration(900);
                    alphaAnimationTitle.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            textViewBestReaction.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    textViewBestReaction.setAnimation(alphaAnimationTitle);
                    textViewBestReaction.setText("BEST REACTION\n" + bestReaction + " ms");
                    textViewBestReaction.setTextColor(getColor(R.color.colorPrimary));
                } else {
                    final AlphaAnimation alphaAnimationTitle = new AlphaAnimation(0.0f, 1.0f);
                    alphaAnimationTitle.setStartOffset(0);
                    alphaAnimationTitle.setDuration(900);
                    alphaAnimationTitle.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            textViewBestReaction.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    textViewBestReaction.setAnimation(alphaAnimationTitle);
                    textViewBestReaction.setText(R.string.no_reaction);
                    textViewBestReaction.setTextColor(getColor(R.color.gray));
                }
            }
        }.start();
        startClickMe();
    }

    @Override
    public void clickMeButton() {
        endTime = System.currentTimeMillis();
        currentReaction = endTime - startTime;
        reaction.add(currentReaction);
        textViewButtonClickMe.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.setMargins(0, randomTop, 0, 0);
        textViewResult.setLayoutParams(params);
        textViewResult.setText(currentReaction + " ms");
        textViewResult.setVisibility(View.VISIBLE);

        final AlphaAnimation alphaAnimationTitle = new AlphaAnimation(0.8f, 0.0f);
        alphaAnimationTitle.setStartOffset(1);
        alphaAnimationTitle.setDuration(350);
        alphaAnimationTitle.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textViewResult.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        textViewResult.setAnimation(alphaAnimationTitle);
        final MediaPlayer fartLong = MediaPlayer.create(this, R.raw.fartlong);
        final MediaPlayer fart = MediaPlayer.create(this, R.raw.fart);
        final MediaPlayer tap = MediaPlayer.create(this, R.raw.tap);
        final MediaPlayer ding = MediaPlayer.create(this, R.raw.ding);
        final MediaPlayer wow = MediaPlayer.create(this, R.raw.wow);
        if (currentReaction > 1000) {
            fartLong.start();
        } else if (currentReaction > 500 && currentReaction < 1000) {
            fart.start();
        } else if (currentReaction > 350 && currentReaction <= 500) {
            tap.start();
        } else if (currentReaction > 250 && currentReaction <= 350) {
            ding.start();
        } else if (currentReaction <= 250) {
            wow.start();
        }

        startClickMe();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.toast_back_exit), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}


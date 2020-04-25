package com.slavinskydev.trafficdevilstest.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.slavinskydev.trafficdevilstest.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private TextView textViewButtonStart;
    private TextView textViewButtonClickMe;
    private TextView textViewResult;
    private TextView textViewBestReaction;

    private long startTime, endTime, currentReaction, bestReaction;
    private int countDownTimer = 20000;
    private List<Long> reaction = new ArrayList<>();

    private ProgressBar progressBar;

    private int randomTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        textViewButtonStart = findViewById(R.id.textViewButtonStart);
        textViewButtonClickMe = findViewById(R.id.textViewButtonClickMe);
        textViewResult = findViewById(R.id.textViewResult);
        textViewBestReaction = findViewById(R.id.textViewBestReaction);
        progressBar = findViewById(R.id.progressBar);

        textViewButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewResult.setVisibility(View.GONE);
                textViewBestReaction.setVisibility(View.GONE);
                textViewBestReaction.setText("");
                textViewButtonStart.setVisibility(View.GONE);
                countDownTimer = 20000;
                reaction.clear();
                progressBar.setProgress(countDownTimer);

                new CountDownTimer(20000, 20) {
                    public void onTick(long millisUntilFinished) {
                        countDownTimer = countDownTimer - 20;
                        progressBar.setProgress(countDownTimer - 200);
                    }

                    public void onFinish() {
                        textViewButtonClickMe.setVisibility(View.GONE);
                        textViewButtonStart.setVisibility(View.VISIBLE);
                        textViewBestReaction.setVisibility(View.INVISIBLE);
                        textViewButtonStart.setText("PLAY AGAIN");
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
                            textViewBestReaction.setText("NO REACTION");
                            textViewBestReaction.setTextColor(getColor(R.color.gray));
                        }
                    }
                }.start();

                startClickMe();

            }
        });

        textViewButtonClickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


                startClickMe();


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
                    randomTop = (int) (Math.random() * 1500 + 1);
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

}


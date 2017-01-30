package com.prime.awitd.contactbook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.prime.awitd.contactbook.R;
import com.prime.awitd.contactbook.model.Typewriter;

/**
 * Created by SantaClaus on 27/12/2016.
 */

public class GetStartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        final Button btnStart = (Button) findViewById(R.id.button_get_start);
        btnStart.setVisibility(View.INVISIBLE);

        Typewriter tvhi = (Typewriter) findViewById(R.id.tv_hi);
        tvhi.setCharacterDelay(50);
        tvhi.animateText(getString(R.string.first_time_animate_head_text));

        btnStart.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animationFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.jump_from_down);
                btnStart.setAnimation(animationFadeIn);
                btnStart.setVisibility(View.VISIBLE);
            }
        }, 3000);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}

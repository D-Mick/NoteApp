package com.rotimijohnson.notetakingapp.view.walkthrough;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rotimijohnson.notetakingapp.R;
import com.rotimijohnson.notetakingapp.adapters.SliderAdapter;
import com.rotimijohnson.notetakingapp.view.MainActivity;
import com.rotimijohnson.notetakingapp.view.auth.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class WalkthroughActivity extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout dotsLayout;
    SliderAdapter sliderAdapter;
    TextView[] dots;
    Button lets_get_started_btn;
    Animation animation;
    int currentPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewPager = findViewById(R.id.view_Pager);
        dotsLayout = findViewById(R.id.dots);
        lets_get_started_btn = findViewById(R.id.lets_get_started);
        lets_get_started_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WalkthroughActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }

    public void skip(View view){
        Intent intent = new Intent(WalkthroughActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void next(View view){
        viewPager.setCurrentItem(currentPosition + 1);
    }

    private void addDots(int position){
        dots = new TextView[3];
        dotsLayout.removeAllViews();
        for (int i = 0; i< dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);

            currentPosition = position;

            if (position == 0){
                lets_get_started_btn.setVisibility(View.INVISIBLE);
            }else if (position == 1){
                lets_get_started_btn.setVisibility(View.INVISIBLE);
            }else{
                animation = AnimationUtils.loadAnimation(WalkthroughActivity.this,R.anim.side_animation);
                lets_get_started_btn.setAnimation(animation);
                lets_get_started_btn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}

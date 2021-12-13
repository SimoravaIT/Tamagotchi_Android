package com.example.androidapp.ui.home;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import pl.droidsonroids.gif.GifImageView;

import com.example.androidapp.DatabaseController;
import com.example.androidapp.MainActivity;
import com.example.androidapp.Pet;
import com.example.androidapp.R;
import com.example.androidapp.User;
import com.example.androidapp.databinding.FragmentHomeBinding;
import com.example.androidapp.sensors.SensorController;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RelativeLayout chic_walking_area;
    private GifImageView chic, heart;
    private LinearLayout food_linearLayout;
    private ArrayList<ImageView> foods;
    private RelativeLayout.LayoutParams lp_chic, lp_heart;
    private int x_chic, y_chic, next_x_direction, next_y_direction;
    private int animation_frame, animation_frameSet_size;
    private String chic_state, food_selected;


    private TextView happiness_progressbar_cont;
    private static int maxWidth;
  

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if(MainActivity.sensorController==null){
            MainActivity.sensorController = new SensorController(getContext());
        }


        // layout, view, img init
        chic_walking_area = (RelativeLayout) root.findViewById(R.id.relative_screen_layout);
        chic = (GifImageView) root.findViewById(R.id.chic);
        heart = (GifImageView) root.findViewById(R.id.heart);
        food_linearLayout = (LinearLayout) root.findViewById(R.id.food_linearLayout);
        foods = new ArrayList<ImageView>();
        for(int i = 0; i < 5; i++) foods.add((ImageView) food_linearLayout.getChildAt(i));
        lp_chic = new RelativeLayout.LayoutParams(160, 160);
        lp_heart = new RelativeLayout.LayoutParams(120, 120);


        Pet pet =DatabaseController.loadPet(getContext());
        User user= DatabaseController.loadUser(getContext());

        happiness_progressbar_cont = (TextView) root.findViewById(R.id.happiness_progressBar_container);
        happiness_progressbar_cont.post(new Runnable() {
            @Override
            public void run() {
                maxWidth = happiness_progressbar_cont.getMeasuredWidth();
                TextView happinessBar =(TextView) root.findViewById(R.id.happiness_progressBar);
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) happinessBar.getLayoutParams();
                double currentWidth=(maxWidth/100.0)*pet.getHappiness();
                lp.width=(int)currentWidth;
                happinessBar.setLayoutParams(lp);
            }
        });

        TextView homeCoins = (TextView) root.findViewById(R.id.coin_home);
        homeCoins.setText(String.valueOf(user.getMoney()));

        // Screen Relativelayout and Chic GitImageView
        chic_walking_area = (RelativeLayout) root.findViewById(R.id.relative_screen_layout);
        chic = (GifImageView) root.findViewById(R.id.chic);

        // start the animation
        startAnimation();

        return root;
    }

    public void startAnimation() {
        // position init
        x_chic = 50;
        y_chic = 100;
        // animation frame init
        animation_frame = 0;
        animation_frameSet_size = 15;
        // chic state
        chic_state = "idle";

        Timer mTimer = new Timer();
        Activity activity = getActivity();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (activity != null) {
                    activity.runOnUiThread( ()-> {
                        if (chic_state == "idle") {

                            if (animation_frame == 0) {
                                randomDirection(true, true);
                            }
                            updatePosition(true, false);
                            animation_frame++;
                            animation_frame%=animation_frameSet_size;

                        } else if (chic_state == "walking_to_food") {

                            if (food_linearLayout.getVisibility() == View.INVISIBLE) {
                                switch (food_selected){
                                    case "branch":
                                        for (ImageView food: foods) {
                                            food.setImageResource(R.drawable.ic_branch);
                                            food.setColorFilter(getResources().getColor(R.color.wheat));
                                        }
                                        break;
                                    case "worm":
                                        for (ImageView food: foods) {
                                            food.setImageResource(R.drawable.ic_worm);
                                            food.setColorFilter(getResources().getColor(R.color.worm));
                                        }
                                        break;
                                    case "lettuce":
                                        for (ImageView food: foods) {
                                            food.setImageResource(R.drawable.ic_lettuce);
                                            food.setColorFilter(getResources().getColor(R.color.lettuce));
                                        }
                                        break;
                                }
                                food_linearLayout.setVisibility(View.VISIBLE);
                            }

                            checkDirection(170, 85, 40, 10);
                            updatePosition(true, false);

                            if (next_x_direction == 0 && next_y_direction == 0) {
                                chic.setImageResource(R.drawable.chic_eating_animation);
                                chic_state = "eating";
                            }

                        } else if (chic_state == "eating") {

                            // control what to do in specific animation frame
                            if (animation_frame == 0) {
                                randomDirection(true, false);
                                chic.setImageResource(R.drawable.chic_walking_animation);
                            } else if (animation_frame < 3 && next_x_direction != 0) {
                                updatePosition(true, true);
                            } else if (animation_frame == 3) {
                                chic.setImageResource(R.drawable.chic_eating_animation);
                            } else if (animation_frame > 3) {
                                food_linearLayout.setAlpha((float)(food_linearLayout.getAlpha()*0.98));
                            }

                            animation_frame++;
                            animation_frame%=animation_frameSet_size;

                            // if the food is almost finished, then change chic state to idle
                            if (food_linearLayout.getAlpha() < 0.1) {
                                chic.setImageResource(R.drawable.chic_walking_animation);
                                food_linearLayout.setVisibility(View.INVISIBLE);
                                food_linearLayout.setAlpha(1);
                                heart.setVisibility(View.INVISIBLE);
                                chic_state = "idle";
                            }
                        }
                    });
                }
            }
        }, 0, 100);
    }

    public void feedingAnimation(String food){
        chic_state = "walking_to_food";
        food_selected = food;
    }

    public void updatePosition(boolean updateChic, boolean updateHeart) {
        if (updateChic) {
            // change chic x, y based on the direction
            x_chic += next_x_direction*5;
            y_chic += next_y_direction*5;
            lp_chic.leftMargin = x_chic;
            lp_chic.topMargin = y_chic;

            // update the view
            chic.setLayoutParams(lp_chic);
        }
        if (updateHeart) {
            // check visibility
            if (heart.getVisibility() == View.INVISIBLE) heart.setVisibility(View.VISIBLE);

            // change chic x, y based on the direction
            lp_heart.leftMargin = x_chic + 110;
            lp_heart.topMargin = y_chic - 55;

            // update the view
            heart.setLayoutParams(lp_heart);
        }
    }

    public void checkDirection(int goalX, int goalY, int rangeX, int rangeY) {
        // init direction
        next_x_direction = 0;
        next_y_direction = 0;

        if (goalX - x_chic > rangeX)  next_x_direction = 1;
        if (goalX - x_chic < -rangeX)  next_x_direction = -1;
        if (goalY - y_chic > rangeY)  next_y_direction = 1;
        if (goalY - y_chic < -rangeY)  next_y_direction = -1;

        // horizontally flip the img base on the direction
        if (next_x_direction != 0) chic.setScaleX(-next_x_direction);
    }

    public void randomDirection(boolean randX, boolean randY) {
        // init direction
        next_x_direction = 0;
        next_y_direction = 0;

        // constrain chic walking area, if chic is out of range, then change direction
        if (randX) {
            next_x_direction = new Random().nextInt(3) + -1;
            if (x_chic > 300) next_x_direction = -1;
            if (x_chic < 30) next_x_direction = 1;
        }

        if (randY) {
            next_y_direction = new Random().nextInt(3) + -1;
            if (y_chic > 160) next_y_direction = -1;
            if (y_chic < 30) next_y_direction = 1;
        }

        // horizontally flip the img based on direction
        if (next_x_direction != 0) chic.setScaleX(-next_x_direction);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
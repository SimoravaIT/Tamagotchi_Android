package com.example.androidapp.ui.home;

import android.app.Activity;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import pl.droidsonroids.gif.GifImageView;

import com.example.androidapp.MainActivity;
import com.example.androidapp.R;
import com.example.androidapp.databinding.FragmentHomeBinding;
import com.example.androidapp.sensors.SensorController;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RelativeLayout chic_walking_area;
    private GifImageView chic;
    private LinearLayout food_group;
    private ImageView food_1, food_2, food_3, food_4, food_5;
    private RelativeLayout.LayoutParams layoutParams;
    private int x_chic, y_chic;
    private int next_x_direction, next_y_direction;
    private int animation_frame, animation_frameSet_size;
    private String chic_state;
    private String food_selected;



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
        food_group = (LinearLayout) root.findViewById(R.id.food_group);
        food_1 = (ImageView) root.findViewById(R.id.food_selected_1);
        food_2 = (ImageView) root.findViewById(R.id.food_selected_2);
        food_3 = (ImageView) root.findViewById(R.id.food_selected_3);
        food_4 = (ImageView) root.findViewById(R.id.food_selected_4);
        food_5 = (ImageView) root.findViewById(R.id.food_selected_5);
        layoutParams = new RelativeLayout.LayoutParams(160, 160);

        // start the animation
        startAnimation(chic_walking_area, chic);

        return root;
    }

    public void startAnimation(RelativeLayout area, GifImageView chic) {
        // position init
        x_chic = 50;
        y_chic = 200;
        // animation frame init
        animation_frame = 0;
        animation_frameSet_size = 10;
        // chic state and food init
        chic_state = "walking_to_food";
        food_selected = "branch";


        Timer mTimer = new Timer();
        Activity activity = getActivity();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (activity != null) {
                    activity.runOnUiThread( ()-> {
                        if (chic_state == "idle") {
                            if (animation_frame == 0) {
                                // random the direction, -1 -> left, 0 -> remain, 1 -> right
                                next_x_direction = new Random().nextInt(3) + -1;
                                next_y_direction = new Random().nextInt(3) + -1;
                                // constrain chic walking area, if chic is out of range, then change direction
                                if (x_chic > 300) next_x_direction = -1;
                                if (x_chic < 30) next_x_direction = 1;
                                if (y_chic > 200) next_y_direction = -1;
                                if (y_chic < 30) next_y_direction = 1;
                                // horizontally flip the img based on direction
                                if (next_x_direction != 0) chic.setScaleX(-next_x_direction);
                            }

                            // change chic x, y based on the direction
                            x_chic += next_x_direction*5;
                            y_chic += next_y_direction*5;
                            layoutParams.leftMargin = x_chic;
                            layoutParams.topMargin = y_chic;

                            // update the view
                            area.removeAllViews();
                            area.addView(chic, layoutParams);

                            animation_frame++;
                            animation_frame%=animation_frameSet_size;
                        } else if (chic_state == "walking_to_food") {
                            if (food_group.getVisibility() == View.INVISIBLE) {
                                food_group.setVisibility(View.VISIBLE);
                            }
                            if (Math.abs(170 - x_chic) > 40 || Math.abs(95 - y_chic) > 10) {
                                next_x_direction = 0;
                                next_y_direction = 0;
                                if (170 - x_chic > 40)  next_x_direction = 1;
                                if (170 - x_chic < -40)  next_x_direction = -1;
                                if (95 - y_chic > 10)  next_y_direction = 1;
                                if (95 - y_chic < -10)  next_y_direction = -1;
                                if (next_x_direction != 0) chic.setScaleX(-next_x_direction);
                                x_chic += next_x_direction*5;
                                y_chic += next_y_direction*5;
                                layoutParams.leftMargin = x_chic;
                                layoutParams.topMargin = y_chic;
                                area.removeAllViews();
                                area.addView(chic, layoutParams);
                            } else {
                                chic.setImageResource(R.drawable.chic_eating_animation);
                                area.removeAllViews();
                                area.addView(chic, layoutParams);
                                chic_state = "eating";
                            }
                        } else if (chic_state == "eating") {
                            if (animation_frame == 0) {
                                next_x_direction = new Random().nextInt(3) + -1;
                                if (x_chic > 300) next_x_direction = -1;
                                if (x_chic < 30) next_x_direction = 1;
                                if (next_x_direction != 0) chic.setScaleX(-next_x_direction);
                                chic.setImageResource(R.drawable.chic_walking_animation);
                                area.removeAllViews();
                                area.addView(chic, layoutParams);
                            } else if (animation_frame < 3 && next_x_direction != 0) {
                                x_chic += next_x_direction*5;
                                layoutParams.leftMargin = x_chic;
                                area.removeAllViews();
                                area.addView(chic, layoutParams);
                            } else if (animation_frame == 3) {
                                chic.setImageResource(R.drawable.chic_eating_animation);
                                area.removeAllViews();
                                area.addView(chic, layoutParams);
                            }
                            animation_frame++;
                            animation_frame%=animation_frameSet_size;
                        }
                    });
                }
            }
        }, 0, 300);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
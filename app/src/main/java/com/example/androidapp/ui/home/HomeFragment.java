package com.example.androidapp.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RelativeLayout chic_walking_area;
    private GifImageView chic;
    private int x_chic, y_chic;
    private int next_x_direction, next_y_direction;
    private int animation_frame, animation_frameSet_size;
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

        Pet pet =DatabaseController.loadPet(getContext());
        User user= DatabaseController.loadUser(getContext());

        happiness_progressbar_cont = (TextView) root.findViewById(R.id.happiness_progressBar_container);
        happiness_progressbar_cont.post(new Runnable() {
            @Override
            public void run() {
                maxWidth = happiness_progressbar_cont.getMeasuredWidth();
            }});

        TextView happinessBar =(TextView) root.findViewById(R.id.happiness_progressBar);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) happinessBar.getLayoutParams();
        double currentWidth=(maxWidth/100.0)*pet.getHappiness();
        lp.width=(int)currentWidth;
        happinessBar.setLayoutParams(lp);

        TextView homeCoins = (TextView) root.findViewById(R.id.coin_home);
        homeCoins.setText(String.valueOf(user.getMoney()));

        // Screen Relativelayout and Chic GitImageView
        chic_walking_area = (RelativeLayout) root.findViewById(R.id.relative_screen_layout);
        chic = (GifImageView) root.findViewById(R.id.chic);
        // start the animation
        startAnimation(chic_walking_area, chic);

        return root;
    }

    public void startAnimation(RelativeLayout area, GifImageView chic) {
        // position init
        x_chic = 200;
        y_chic = 100;
        // animation frame init
        animation_frame = 0;
        animation_frameSet_size = 10;

        Timer mTimer = new Timer();
        Activity activity = getActivity();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (activity != null) {
                    activity.runOnUiThread( ()-> {
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(120, 120);

                        if (animation_frame == 0) {
                            // random the direction, -1 -> left, 0 -> remain, 1 -> right
                            next_x_direction = new Random().nextInt(3) + -1;
                            next_y_direction = new Random().nextInt(3) + -1;
                            // constrain chic walking area, if chic is out of range, then change direction
                            if (x_chic > 190) next_x_direction = -1;
                            if (x_chic < 50) next_x_direction = 1;
                            if (y_chic > 110) next_y_direction = -1;
                            if (y_chic < 50) next_y_direction = 1;
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
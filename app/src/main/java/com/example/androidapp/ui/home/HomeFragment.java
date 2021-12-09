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
    private RelativeLayout relativeScreenLayout;
    private GifImageView chic;
    private int x_chic, y_chic;
    private int next_x_direction, next_y_direction;
    private int animation_frame, animation_frameSet_size;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if(MainActivity.sensorController==null){
            MainActivity.sensorController = new SensorController(getContext());
        }

        // RelativeLayout. though you can use xml RelativeLayout here too by `findViewById()`
        relativeScreenLayout = (RelativeLayout) root.findViewById(R.id.relative_screen_layout);
        // ImageView
        chic = (GifImageView) root.findViewById(R.id.chic);
        // position init
        x_chic = 200;
        y_chic = 100;
        // frame init
        animation_frame = 0;
        animation_frameSet_size = 10;

        Timer mTimer = new Timer();
        Activity activity = getActivity();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (activity != null) {
                    activity.runOnUiThread( ()-> {
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100);

                        if (animation_frame == 0) {
                            next_x_direction = new Random().nextInt(3) + -1;
                            next_y_direction = new Random().nextInt(3) + -1;
                            if (x_chic > 190) next_x_direction = -1;
                            if (x_chic < 50) next_x_direction = 1;
                            if (y_chic > 110) next_y_direction = -1;
                            if (y_chic < 50) next_y_direction = 1;
                            if (next_x_direction != 0) chic.setScaleX(-next_x_direction);
                        }

                        x_chic += next_x_direction*5;
                        y_chic += next_y_direction*5;

                        layoutParams.leftMargin = x_chic;
                        layoutParams.topMargin = y_chic;

                        relativeScreenLayout.removeAllViews();
                        relativeScreenLayout.addView(chic, layoutParams);

                        animation_frame++;
                        animation_frame%=animation_frameSet_size;
                    });
                }
            }
        }, 0, 300);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
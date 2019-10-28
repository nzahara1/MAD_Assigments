package com.example.homework06;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectAvatar extends Fragment {

    Image image;
    int[] imageDrawable = {R.drawable.avatar_f_1, R.drawable.avatar_f_2, R.drawable.avatar_f_3, R.drawable.avatar_m_1, R.drawable.avatar_m_2, R.drawable.avatar_m_3};
    int[] imageViews = {R.id.f_1, R.id.f_2, R.id.f_3, R.id.m_1, R.id.m_2, R.id.m_3};
    ImageView imageView;

    public SelectAvatar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_avatar, container, false);
    }


    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            image = (SelectAvatar.Image) activity;
        } catch (ClassCastException e) {
            Log.d("exception", activity.toString() + " activity has not implemented interface");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for (int i = 0; i < imageViews.length; i++) {
            final ImageView imageView = getView().findViewById(imageViews[i]);
            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    image.getImage(imageDrawable[finalI]);
                }
            });
        }
    }

    public interface Image {
        public void getImage(int imageView);
    }

}

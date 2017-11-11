package com.example.royal.delicacy_nfu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by user on 2017/11/7.0
 */

public class ImageSlideFragment extends Fragment {
    int imageID;

    static ImageSlideFragment init(int imgID) {
        ImageSlideFragment frag = new ImageSlideFragment();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("imageID", imgID);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            imageID = getArguments().getInt("imageID");
        }else{
            imageID =  R.drawable.shape_scrim;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.detail_image_slide, container, false);

        ImageView img = rootView.findViewById(R.id.slide_image);
        GlideApp.with(rootView)
                .load(imageID)
                .thumbnail(0.4f)
                .fitCenter()
                .into(img);
        return rootView;
    }
}

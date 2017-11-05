package com.example.royal.delicacy_nfu;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by user on 2017/11/3.
 */

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.MyViewHolder> {
    private List<String> titleList;
    private List<Integer> imageList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        MyViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.info_text);
        }
    }

    MainViewAdapter(List<String> titleList,List<Integer> imageList) {
        this.titleList = titleList;
        this.imageList = imageList;
    }

    @Override
    public MainViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_card_view, parent, false);
        MyViewHolder viewHolder  = new MyViewHolder(view);
        return viewHolder ;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mTextView.setText(titleList.get(position));
        holder.mTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,imageList.get(position),0,0);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v.getRootView(),
                        "Onclick:"+ holder.getLayoutPosition(),Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (titleList.size());
    }

    public void update(List<String> tList,List<Integer> imgList){
        titleList.clear();
        titleList.addAll(tList);
        imageList.clear();
        imageList.addAll(imgList);
        notifyDataSetChanged();
        Log.d("debug","adapter update");
    }
}
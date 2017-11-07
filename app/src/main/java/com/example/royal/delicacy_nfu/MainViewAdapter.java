package com.example.royal.delicacy_nfu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 2017/11/3.
 */

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.MyViewHolder> {
    private List<Pair<String,Integer>> dataList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private ImageView mThumbnail;

        MyViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.info_text);
            mThumbnail = itemView.findViewById(R.id.shop_thumbnail);
        }
    }

    MainViewAdapter(List<Pair<String,Integer>> data) {
        this.dataList = data;
    }

    @Override
    public MainViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_card_view, parent, false);
        MyViewHolder viewHolder  = new MyViewHolder(view);
        return viewHolder ;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final String shopName = dataList.get(position).first;
        final Integer imageID = dataList.get(position).second;

        holder.mTextView.setText(shopName);

        holder.mThumbnail.setImageResource(imageID);
        holder.mThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View itemview) {
                Context mContext = itemview.getContext();
                Intent intent = new Intent(mContext,DetailActivity.class);
                intent.putExtra("ShopName",shopName);
                intent.putExtra("ShopImage",imageID);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String trans = mContext.getResources().getString(R.string.picture_transition_name);
                    ActivityOptionsCompat option = ActivityOptionsCompat
                            .makeSceneTransitionAnimation((Activity)mContext,holder.mThumbnail, trans);
                    mContext.startActivity(intent,option.toBundle());
                }
                else {
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList.size());
    }

    void update(List<Pair<String,Integer>> data){
        dataList.clear();
        dataList.addAll(data);
        notifyDataSetChanged();
    }


}
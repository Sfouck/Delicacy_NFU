package com.example.royal.delicacy_nfu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/11/3.
 */

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.MyViewHolder> {
    private List<ArrayList<String>> dataList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private ImageView mThumbnail;

        MyViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.info_text);
            mThumbnail = itemView.findViewById(R.id.shop_thumbnail);
        }
    }

    MainViewAdapter(List<ArrayList<String>> data) {
        this.dataList = data;
    }

    @Override
    public MainViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Integer shopNumber = Integer.valueOf(dataList.get(position).get(0));
        final String shopName = dataList.get(position).get(1);
        final Integer imageID = Integer.valueOf(dataList.get(position).get(2));
        final String mealName = dataList.get(position).get(3);

        holder.mTextView.setText(shopName);

        GlideApp.with(holder.itemView.getContext())
                .load(imageID)
                .error(R.drawable.cat)
                .thumbnail(0.1f)
                .centerCrop()
                .fitCenter()
                .into(holder.mThumbnail);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View itemview) {
                Context mContext = itemview.getContext();
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("ShopNumber", shopNumber);
                intent.putExtra("ShopName", shopName);
                intent.putExtra("ShopImage", imageID);
                intent.putExtra("MealName", mealName);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String trans = mContext.getResources().getString(R.string.picture_transition_name);
                    ActivityOptionsCompat option = ActivityOptionsCompat
                            .makeSceneTransitionAnimation((Activity) mContext, holder.mThumbnail, trans);
                    mContext.startActivity(intent, option.toBundle());
                } else {
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList.size());
    }

    void update(List<ArrayList<String>> data) {
        dataList.clear();
        dataList.addAll(data);
        notifyDataSetChanged();
    }

}
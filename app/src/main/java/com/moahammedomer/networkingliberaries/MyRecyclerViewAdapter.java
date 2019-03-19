package com.moahammedomer.networkingliberaries;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Product> myList;
    private LayoutInflater mInflater;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    MyRecyclerViewAdapter(Context context, ArrayList<Product> data, OnItemClickListener listener){
        this.mInflater = LayoutInflater.from(context);
        this.myList = data;
        this.listener = listener;
    }
    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        Product product = myList.get(i);
        viewHolder.price.setText(String.valueOf(product.getPrice()) + " SDG");
        viewHolder.description.setText(product.getDescription());
        viewHolder.title.setText(product.getName());
        Glide.with(viewHolder.itemView.getContext())
                .load(product.getImageUrl())
//                .override(200, 200)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        viewHolder.progressBar.setVisibility(View.GONE);
                        viewHolder.image.setImageDrawable(resource);
                        return true;
                    }
                })
                .into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }


    public void setOnItemListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title, description, price;
        ImageButton image;
        private OnItemClickListener mListener;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            mListener = listener;
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.item_name);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.item_image);
            progressBar = itemView.findViewById(R.id.loading_indicator);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(v, getAdapterPosition());
        }
    }


}

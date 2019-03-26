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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Product> myList;
    private LayoutInflater mInflater;
    private OnItemClickListener listener;
    private Context context;
    private int lastPosition = 4;
    private RequestManager glide;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    MyRecyclerViewAdapter(RequestManager glide, Context context, ArrayList<Product> data, OnItemClickListener listener){
        this.glide = glide;
        this.mInflater = LayoutInflater.from(context.getApplicationContext());
        this.myList = data;
        this.listener = listener;
        this.context = context;
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
        if (product != null){
            viewHolder.price.setText(String.valueOf(Math.round(product.getPrice())) + " " + context.getString(R.string.SDG_unit));
            viewHolder.description.setText(product.getDescription());
            viewHolder.title.setText(product.getName());
            // this context.getApplicationContext fixed the crash after restarting the app after clicking the back button
            Picasso.get().load(product.getImageUrl())
                .resize(200, 200)
                    .placeholder(R.drawable.progress_animated)
//                    .listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public synchronized boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            viewHolder.image.setImageDrawable(resource);
//                            viewHolder.image.setImageDrawable(resource);
//                            Log.e("main", "loading into " + product.getName());
//                            return true;
//                        }
//                    })
                    .into(viewHolder.image);
            if (i > lastPosition){
                Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.up_from_bottom);
                viewHolder.itemView.startAnimation(animation);
                lastPosition = i;
            }
        }
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
        ImageView image;
        private OnItemClickListener mListener;

        ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            mListener = listener;
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.item_name);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.item_image);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(v, getAdapterPosition());
        }
    }



}

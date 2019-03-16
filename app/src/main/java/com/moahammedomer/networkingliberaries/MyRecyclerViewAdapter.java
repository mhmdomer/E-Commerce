package com.moahammedomer.networkingliberaries;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Product> myList;
    private LayoutInflater mInflater;
    private String server_images_url = "http://095c6044.ngrok.io/imageUpload/";
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
        viewHolder.price.setText(String.valueOf(product.getPrice()));
        viewHolder.description.setText(product.getDescription());
        viewHolder.title.setText(product.getName());
        Picasso.get()
                .load(server_images_url + product.getId() + ".jpg")
                .resize(100, 100)
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
        ImageView image;
        private OnItemClickListener mListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
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

package com.example.essaouiraapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    ViewPager2 viewPager;
    ArrayList<String> imgs;
    private Context mContext;


    public SliderAdapter(ViewPager2 viewPager, ArrayList<String> imgs,Context mContext) {
        this.viewPager = viewPager;
        this.imgs = imgs;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       return new SliderViewHolder(
               LayoutInflater.from(parent.getContext())
                       .inflate(R.layout.slide_prod,
                               parent,
                               false
               )
       );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImageView(imgs.get(position));
        if (position==imgs.size()-2){
            viewPager.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return imgs.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder{
        ArrayList<String> list;
        private RoundedImageView imageView;

         SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageSlide);
            this.list=new ArrayList<String>();
        }



        void setImageView(String img) {
            Picasso.with(mContext)
                    .load(img)
                    .fit()
                    .centerCrop()
                    .into(imageView);
        }
    }

    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            imgs.addAll(imgs);
            notifyDataSetChanged();
        }
    };
}

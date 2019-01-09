package com.example.ijasahamed.lezyv2.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ijasahamed.lezyv2.ItemInfoActivity;
import com.example.ijasahamed.lezyv2.R;
import com.example.ijasahamed.lezyv2.models.items;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewholder>{
    private Context mContext;
    private List<items> mData;
    RequestOptions option;

    public RecyclerViewAdapter(Context mContext, List<items> mData) {
        this.mContext = mContext;
        this.mData = mData;

        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View view;
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.items_row,parent,false);
        final MyViewholder myViewholder = new MyViewholder(view);

        myViewholder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ItemInfoActivity.class);
                intent.putExtra("item_id",mData.get(myViewholder.getAdapterPosition()).getItem_id());
                intent.putExtra("item_name",mData.get(myViewholder.getAdapterPosition()).getItem_name());
                intent.putExtra("item_img",mData.get(myViewholder.getAdapterPosition()).getItem_img());
                intent.putExtra("item_price",mData.get(myViewholder.getAdapterPosition()).getItem_price());
                intent.putExtra("item_availability",mData.get(myViewholder.getAdapterPosition()).getItem_availability());
                intent.putExtra("item_type",mData.get(myViewholder.getAdapterPosition()).getItem_type());
                intent.putExtra("item_category",mData.get(myViewholder.getAdapterPosition()).getItem_category());
                intent.putExtra("shop_id",mData.get(myViewholder.getAdapterPosition()).getShop_id());
                intent.putExtra("shop_name",mData.get(myViewholder.getAdapterPosition()).getShop_name());
                intent.putExtra("item_descriptions",mData.get(myViewholder.getAdapterPosition()).getItem_descriptions());
                intent.putExtra("Avg_rating",mData.get(myViewholder.getAdapterPosition()).getAvg_rating());

                    mContext.startActivity(intent);


            }
        });

        return myViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {

        holder.tv_itemName.setText(mData.get(position).getItem_name());
        holder.tv_shopName.setText(mData.get(position).getShop_name());
        holder.tv_price.setText(String.valueOf(mData.get(position).getItem_price()));
        holder.tv_rating.setText(String.valueOf(mData.get(position).getAvg_rating()));

        Glide.with(mContext).load(mData.get(position).getItem_img()).apply(option).into(holder.iv_thumbnail);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewholder extends RecyclerView.ViewHolder{

        TextView tv_itemName;
        TextView tv_shopName;
        TextView tv_price;
        TextView tv_rating;
        ImageView iv_thumbnail;
        LinearLayout view_container;



        public MyViewholder(View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.container);
            tv_itemName = itemView.findViewById(R.id.item_name);
            tv_shopName = itemView.findViewById(R.id.shop_name);
            tv_price = itemView.findViewById(R.id.price);
            tv_rating = itemView.findViewById(R.id.rating);
            iv_thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }
}

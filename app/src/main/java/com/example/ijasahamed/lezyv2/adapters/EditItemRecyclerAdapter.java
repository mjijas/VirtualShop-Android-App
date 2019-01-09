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
import com.example.ijasahamed.lezyv2.EditItemDetails;
import com.example.ijasahamed.lezyv2.R;
import com.example.ijasahamed.lezyv2.models.items;

import java.util.List;

public class EditItemRecyclerAdapter  extends RecyclerView.Adapter<EditItemRecyclerAdapter.EditViewholder>{

    private Context editContext;
    private List<items> editData;
    RequestOptions option;

    public EditItemRecyclerAdapter(Context editContext, List<items> editData) {
        this.editContext = editContext;
        this.editData = editData;
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }


    @NonNull
    @Override
    public EditViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(editContext);
        view = inflater.inflate(R.layout.items_row,parent,false);
        final EditViewholder editViewholder = new EditViewholder(view);

        editViewholder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(editContext, EditItemDetails.class);
                intent.putExtra("item_id",editData.get(editViewholder.getAdapterPosition()).getItem_id());
                intent.putExtra("item_name",editData.get(editViewholder.getAdapterPosition()).getItem_name());
                intent.putExtra("item_img",editData.get(editViewholder.getAdapterPosition()).getItem_img());
                intent.putExtra("item_price",editData.get(editViewholder.getAdapterPosition()).getItem_price());
                intent.putExtra("item_availability",editData.get(editViewholder.getAdapterPosition()).getItem_availability());
                intent.putExtra("item_type",editData.get(editViewholder.getAdapterPosition()).getItem_type());
                intent.putExtra("item_category",editData.get(editViewholder.getAdapterPosition()).getItem_category());
                intent.putExtra("shop_name",editData.get(editViewholder.getAdapterPosition()).getShop_name());
                intent.putExtra("item_descriptions",editData.get(editViewholder.getAdapterPosition()).getItem_descriptions());
                intent.putExtra("Avg_rating",editData.get(editViewholder.getAdapterPosition()).getAvg_rating());

                editContext.startActivity(intent);

            }
        });





        return editViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull EditViewholder holder, int position) {
        holder.tv_itemName.setText(editData.get(position).getItem_name());
        holder.tv_shopName.setText(editData.get(position).getShop_name());
        holder.tv_price.setText(String.valueOf(editData.get(position).getItem_price()));
        holder.tv_rating.setText(String.valueOf(editData.get(position).getAvg_rating()));

        Glide.with(editContext).load(editData.get(position).getItem_img()).apply(option).into(holder.iv_thumbnail);
    }

    @Override
    public int getItemCount() {
        return editData.size();
    }

    public static class EditViewholder extends RecyclerView.ViewHolder {

        TextView tv_itemName;
        TextView tv_shopName;
        TextView tv_price;
        TextView tv_rating;
        ImageView iv_thumbnail;
        LinearLayout view_container;


        public EditViewholder(View itemView) {
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




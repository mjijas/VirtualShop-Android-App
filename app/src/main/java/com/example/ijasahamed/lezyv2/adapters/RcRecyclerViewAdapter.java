package com.example.ijasahamed.lezyv2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ijasahamed.lezyv2.R;
import com.example.ijasahamed.lezyv2.models.ratingsComments;

import java.util.List;

public class RcRecyclerViewAdapter extends RecyclerView.Adapter<RcRecyclerViewAdapter.RcViewholder>{


    private Context context;
    private List<ratingsComments> data;

    public RcRecyclerViewAdapter(Context context, List<ratingsComments> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RcViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.row_comments,parent,false);
        return new RcViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RcViewholder holder, int position) {

        holder.tv_UserName.setText(data.get(position).getUser_name());
        holder.tv_ratings.setText(String.valueOf(data.get(position).getRatings()));
        holder.tv_comments.setText(data.get(position).getComments());
        holder.tv_dateTime.setText(data.get(position).getDate_time());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class RcViewholder extends RecyclerView.ViewHolder{

        TextView tv_UserName;
        TextView tv_ratings;
        TextView tv_comments;
        TextView tv_dateTime;


        public RcViewholder(View itemView) {
            super(itemView);

            tv_UserName = itemView.findViewById(R.id.rc_user_name);
            tv_ratings = itemView.findViewById(R.id.rc_rating);
            tv_comments = itemView.findViewById(R.id.rc_comments);
            tv_dateTime = itemView.findViewById(R.id.rc_date);
        }
    }

}

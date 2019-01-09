package com.example.ijasahamed.lezyv2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogRateComments extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {

    private EditText commentsEditText;
//    private Spinner rateSpinner;
//    ArrayAdapter<CharSequence> spinnerAdapter;
    float ratings;
    private RateCommentsListener rateCommentsListener;
    private RatingBar ratingBar;

    private String names;

    private TextView itemNameTextView,shopNameTextView;
    private CircleImageView itemImageView;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_rate_comment_dialog,null);

        commentsEditText = view.findViewById(R.id.commentsEditTxt);
//        rateSpinner = view.findViewById(R.id.rateSpinner);
        ratingBar = view.findViewById(R.id.ratingBar);

//        spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.ratting,android.R.layout.simple_spinner_item);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        rateSpinner.setAdapter(spinnerAdapter);
//        rateSpinner.setOnItemSelectedListener(this);


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratings = ratingBar.getRating();
            }
        });

        builder.setView(view)
                .setTitle("Rattings & Comments")
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String rating = String.valueOf(Math.round(ratings));
                String comments = commentsEditText.getText().toString();
                rateCommentsListener.saveRatingsComments(rating,comments);
            }
        }).setIcon(R.drawable.virtual_shop_logo_original);


        itemNameTextView = view.findViewById(R.id.itemNameTextView);
        shopNameTextView = view.findViewById(R.id.shopNameTextView);
        itemImageView = view.findViewById(R.id.itemImageView);

        itemNameTextView.setText(getArguments().getString("itemName"));
        shopNameTextView.setText(getArguments().getString("shopName"));


        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
        Glide.with(this).load(getArguments().getString("itemImage")).apply(requestOptions).into(itemImageView);


        return builder.create();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            rateCommentsListener = (RateCommentsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement RateCommentsListener");
        }
    }



    public interface RateCommentsListener{  //interface
        void saveRatingsComments(String rating,String comments);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        ratings = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}

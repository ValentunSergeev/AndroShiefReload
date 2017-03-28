package com.valentun.androshief.Adapters;

import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.valentun.androshief.DTOs.Category;
import com.valentun.androshief.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> contents;
    private Category category;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CELL = 1;

    public RecyclerViewAdapter(List<Object> contents, Category category) {
        this.contents = contents;
        this.category = category;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext())
                                     .inflate(R.layout.list_item_header, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext())
                                     .inflate(R.layout.list_item_cell, parent, false);
        }

        return new RecyclerView.ViewHolder(view) {
        };
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                CardView cardView = (CardView) holder.itemView.findViewById(R.id.recipes_card_header);
                cardView.setCardBackgroundColor(Color.parseColor(category.getColor()));

                TextView titleView = (AppCompatTextView) holder.itemView.findViewById(R.id.recipes_card_header_title);
                titleView.setText(category.getName());

                TextView descriptionView = (AppCompatTextView) holder.itemView.findViewById(R.id.recipes_card_header_body);
                descriptionView.setText(category.getDescription());
                break;
            default:
                ImageView image = (ImageView) holder.itemView.findViewById(R.id.recipes_card_item_image);
                image.post(image::requestLayout);
        }
    }
}
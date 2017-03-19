package com.valentun.androshief.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valentun.androshief.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> contents;
    private int color = 0;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CELL = 1;

    public RecyclerViewAdapter(List<Object> contents, int color) {
        this.contents = contents;
        this.color = color;
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
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_header, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_cell, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                CardView cardView = (CardView) holder.itemView.findViewById(R.id.recipes_card_header);
                cardView.setCardBackgroundColor(color);
                break;
            case TYPE_CELL:
                break;
        }
    }
}
package com.mangu.crossingactors.Views;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mangu.crossingactors.Model.Result;
import com.mangu.crossingactors.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ResultHolder> {
    public ArrayList<Result> getDataSet() {
        return dataSet;
    }

    private ArrayList<Result> dataSet = new ArrayList<>();

    public SearchAdapter(ArrayList<Result> data) {
        this.dataSet = data;
    }

    @Override
    public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForItem = R.layout.searcher;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForItem, parent, false);
        return new ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultHolder holder, int position) {
        holder.name.setText(dataSet.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if(dataSet == null) {
            return 0;
        }else {
            return dataSet.size();
        }
    }

    public class ResultHolder extends RecyclerView.ViewHolder {
        TextView name;
        String id;
        ImageView searchIcon;

       public ResultHolder(View item) {
            super(item);
            name = (TextView) item.findViewById(R.id.tv_name);
        }
    }

    public void swapResults(ArrayList<Result> results) {
        this.dataSet = results;

        notifyDataSetChanged();
    }
}

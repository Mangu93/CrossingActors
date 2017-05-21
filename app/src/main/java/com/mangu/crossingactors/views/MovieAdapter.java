package com.mangu.crossingactors.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mangu.crossingactors.R;

import java.util.ArrayList;


public class MovieAdapter extends ArrayAdapter<String> {
    public String name;
    private Context mContext;
    private ArrayList<String> mDataSet;

    public MovieAdapter(Context context, ArrayList<String> dataSet) {
        super(context, R.layout.movie_item, dataSet);
        this.mContext = context;
        this.mDataSet = dataSet;
    }

    public void swapItems(ArrayList<String> newTasks) {
        this.mDataSet = newTasks;
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public int getCount() {
        return mDataSet.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final String movie = getItem(position);
        assert movie != null;
        ViewHolder viewHolder;


        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.movie_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_movie_item);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(movie);

        return convertView;
    }

    private static class ViewHolder {
        protected TextView name;
    }

}

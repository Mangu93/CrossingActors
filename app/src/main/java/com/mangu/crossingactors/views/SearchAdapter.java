package com.mangu.crossingactors.views;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mangu.crossingactors.models.Result;
import com.mangu.crossingactors.R;

import java.util.ArrayList;

import static com.mangu.crossingactors.utils.ImageFactory.formUrlPic;

@SuppressWarnings("CanBeFinal")
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ResultHolder> {
    private Context mContext;
    private ArrayList<Result> mDataSet = new ArrayList<>();

    public SearchAdapter(Context context, ArrayList<Result> data) {
        this.mDataSet = data;
        this.mContext = context;
    }

    public ArrayList<Result> getDataSet() {
        return mDataSet;
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
        final Result mResultItem = mDataSet.get(position);
        holder.name.setText(mResultItem.getName());
        String imgUrl = formUrlPic(mResultItem.getProfilePath());
        Glide.with(mContext)
                .load(imgUrl)
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.ic_perm_contact_calendar_black_24dp)
                .into(new BitmapImageViewTarget(holder.searchIcon) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circular =
                                RoundedBitmapDrawableFactory.create(
                                        mContext.getResources(), resource);
                        circular.setCircular(true);
                        holder.searchIcon.setImageDrawable(circular);
                    }
                });
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null) {
            return 0;
        } else {
            return mDataSet.size();
        }
    }

    private void delete(int position) {
        mDataSet.remove(position);
        notifyItemRemoved(position);
    }

    public void swapResults(ArrayList<Result> results) {
        this.mDataSet = results;

        notifyDataSetChanged();
    }

    public class ResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        @SuppressWarnings("unused")
        String id;
        ImageView searchIcon;
        ImageView delete;

        public ResultHolder(View item) {
            super(item);
            searchIcon = (ImageView) item.findViewById(R.id.iv_icon_search);
            name = (TextView) item.findViewById(R.id.tv_name);
            delete = (ImageView) item.findViewById(R.id.iv_icon_delete);
            delete.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            delete(getAdapterPosition());
        }
    }
}

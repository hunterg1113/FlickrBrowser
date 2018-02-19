package com.example.huntergreer.flickrbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "FlickrRecyclerViewAdapt";

    private List<Photo> mPhotosList;
    private Context mContext;

    FlickrRecyclerViewAdapter(List<Photo> photosList, Context context) {
        mPhotosList = photosList;
        mContext = context;
    }

    @Override
    public FlickrImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FlickrImageViewHolder holder, int position) {
        if (mPhotosList == null || mPhotosList.size() == 0) {
            holder.thumbnail.setImageResource(R.drawable.placeholder);
            holder.title.setText(R.string.empty_photo);
        } else {
            Photo photo = mPhotosList.get(position);
            Picasso.with(mContext).load(photo.getImage()).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(holder.thumbnail);
            holder.title.setText(photo.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return (mPhotosList != null && mPhotosList.size() != 0) ? mPhotosList.size() : 1;
    }

    void loadNewData(List<Photo> newPhotos) {
        mPhotosList = newPhotos;
        notifyDataSetChanged();
    }

    Photo getPhoto(int position) {
        return (mPhotosList != null && mPhotosList.size() != 0) ? mPhotosList.get(position) : null;
    }

    static class FlickrImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "FlickrImageViewHolder";

        ImageView thumbnail = null;
        TextView title = null;

        FlickrImageViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "FlickrImageViewHolder: starts");
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}

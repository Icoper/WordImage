package com.apps_x.word_image.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps_x.word_image.R;
import com.apps_x.word_image.model.RealmImageModel;
import com.bumptech.glide.Glide;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 *  Just custom images list adapter
 */

public class ImgListAdapter extends RecyclerView.Adapter<ImgListAdapter.ViewHolder> implements RealmChangeListener{

    private final RealmResults<RealmImageModel> images;
    private View view;

    public ImgListAdapter(RealmResults<RealmImageModel> imgs) {
        images = imgs;
        // This is necessary in order to automatically update the list after receiving a new image.
        images.addChangeListener(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        TextView title = (TextView)view.findViewById(R.id.hi_search_text);
        ImageView img = (ImageView)view.findViewById(R.id.hi_search_img);

        return new ViewHolder(title,img);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.title.setText(images.get(position).getTitle());
        Glide.with(view.getContext()).
                load(images.get(position).getUrl()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    @Override
    public void onChange(Object o) {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView img;

        public ViewHolder(final TextView textView, final ImageView imageView) {
            super(view);
            title = textView;
            img = imageView;
        }
    }
}

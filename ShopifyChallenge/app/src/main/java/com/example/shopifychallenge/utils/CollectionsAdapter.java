package com.example.shopifychallenge.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shopifychallenge.R;
import com.example.shopifychallenge.model.CustomCollection;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter.CollectionsAdapterViewHolder> {


    //Data store
    List<CustomCollection> customCollectionList;

    Context context;

    //Handling Clicks
    public interface ListItemClickListener {

        void onListItemClick(CustomCollection customCollection);

    }

    private ListItemClickListener mOnclickListener;

    public CollectionsAdapter(ListItemClickListener listener, Context context) {

        mOnclickListener = listener;
        this.context = context;


    }

    //ViewHolder Class
    public class CollectionsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Views
        public @BindView(R.id.collection_title)
        TextView titleTextView;
        public @BindView(R.id.collection_date)
        TextView dateTextView;
        public @BindView(R.id.coverImage)
        ImageView coverImage;


        public CustomCollection customCollection;

        public CollectionsAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            mOnclickListener.onListItemClick(customCollectionList.get(position));

        }


    }


    @NonNull
    @Override
    public CollectionsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.collection_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new CollectionsAdapterViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull CollectionsAdapterViewHolder holder, int position) {

        holder.customCollection = customCollectionList.get(position);
        holder.titleTextView.setText(customCollectionList.get(position).getTitle());
        holder.dateTextView.setText(customCollectionList.get(position).getPublishedAt());

        //Image View Logic
        if (!TextUtils.isEmpty(customCollectionList.get(position).getImage().getSrc())) {

            Picasso picasso = new Picasso.Builder(context).
                    downloader(new OkHttp3Downloader(context))
                    .build();
            picasso.load(customCollectionList.get(position).getImage().getSrc())
                    .placeholder((R.drawable.ic_launcher_background))
                    .fit()
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.coverImage);
        }


    }


    @Override
    public int getItemCount() {

        if (customCollectionList == null)
            return 0;
        else
            return customCollectionList.size();
    }


    public void setCustomCollectionData(List<CustomCollection> customCollectionList) {
        this.customCollectionList = customCollectionList;
        notifyDataSetChanged();
    }

}

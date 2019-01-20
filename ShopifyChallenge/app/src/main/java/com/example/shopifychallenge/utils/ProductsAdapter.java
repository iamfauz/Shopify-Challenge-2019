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
import com.example.shopifychallenge.model.Product;
import com.example.shopifychallenge.model.Product;
import com.example.shopifychallenge.model.Variant;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsAdapterViewHolder> {


    //Data store
    List<Product> productList;

    Context context;

    //Handling Clicks
    public interface ListItemClickListener {

        void onListItemClick(Product Product);

    }

    private ListItemClickListener mOnclickListener;

    public ProductsAdapter(ListItemClickListener listener, Context context) {

        mOnclickListener = listener;
        this.context = context;


    }

    //ViewHolder Class
    public class ProductsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Views
        public @BindView(R.id.product_title)
        TextView titleTextView;
        public @BindView(R.id.inventory)
        TextView inventoryTextView;
        public @BindView(R.id.coverImage)
        ImageView coverImage;


        public Product product;

        public ProductsAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            mOnclickListener.onListItemClick(productList.get(position));

        }


    }


    @NonNull
    @Override
    public ProductsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.product_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ProductsAdapterViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ProductsAdapterViewHolder holder, int position) {

        holder.product = productList.get(position);
        holder.titleTextView.setText(productList.get(position).getTitle());
        holder.inventoryTextView.setText(String.format ("%.0f", calculateTotalInventory(productList.get(position))));
        //Image View Logic
        if (!TextUtils.isEmpty(productList.get(position).getImage().getSrc())) {

            Picasso picasso = new Picasso.Builder(context).
                    downloader(new OkHttp3Downloader(context))
                    .build();
            picasso.load(productList.get(position).getImage().getSrc())
                    .placeholder((R.drawable.ic_launcher_background))
                    .fit()
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.coverImage);
        }


    }


    @Override
    public int getItemCount() {

        if (productList == null)
            return 0;
        else
            return productList.size();
    }


    public void setProductData(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }


    /**
     * Method to calculate the total inventory of variants  of a given product
     *
     * @param product
     * @return inventory
     */
    private double calculateTotalInventory(Product product) {

        double inventory = 0;
        List<Variant> variants = product.getVariants();

        for (Variant variant : variants) {
            inventory += variant.getInventoryQuantity();
        }

        return inventory;
    }
}
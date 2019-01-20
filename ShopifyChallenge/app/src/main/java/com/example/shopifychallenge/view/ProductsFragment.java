package com.example.shopifychallenge.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopifychallenge.R;
import com.example.shopifychallenge.model.Collect;
import com.example.shopifychallenge.model.CollectionsResponse;
import com.example.shopifychallenge.model.CollectsResponse;
import com.example.shopifychallenge.model.CustomCollection;
import com.example.shopifychallenge.model.Product;
import com.example.shopifychallenge.model.ProductsResponse;
import com.example.shopifychallenge.retrofit.ApiService;
import com.example.shopifychallenge.retrofit.RetrofitClientInstance;
import com.example.shopifychallenge.utils.CollectionsAdapter;
import com.example.shopifychallenge.utils.Constants;
import com.example.shopifychallenge.utils.ProductsAdapter;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductsFragment extends Fragment implements ProductsAdapter.ListItemClickListener, SwipeRefreshLayout.OnRefreshListener {


    private static final String COLLECTION_ID = "COLLECTION_ID";
    private static final String TITLE = "TITLE";
    private static final String BODY = "BODY";
    private static final String IMG_SRC = "IMG_SRC";

    private double collectionId;
    private String title;
    private String body;
    private String imgSrc;


    @BindView(R.id.recycler_view_product)
    public RecyclerView mRecyclerView;

    @BindView(R.id.swipe_to_refresh)
    public SwipeRefreshLayout mSwipeRefreshLayout;

    public @BindView(R.id.collection_title)
    TextView collectionTitleTextView;

    public @BindView(R.id.body)
    TextView bodyTextView;

    public @BindView(R.id.coverImage)
    ImageView coverImage;

    private ProductsAdapter mProductsAdapter;


    public ProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param collectionId collection ID.
     * @return A new instance of fragment ProductsFragment.
     */
    public static ProductsFragment newInstance(double collectionId, String title, String body, String imgSrc) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putDouble(COLLECTION_ID, collectionId);
        args.putString(TITLE, title);
        args.putString(BODY, body);
        args.putString(IMG_SRC, imgSrc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle("Products");

        if (getArguments() != null) {
            collectionId = getArguments().getDouble(COLLECTION_ID);
            title = getArguments().getString(TITLE);
            body = getArguments().getString(BODY);
            imgSrc = getArguments().getString(IMG_SRC);

        }

    }

    @Override
    public void onResume(){
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle("Products");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        ButterKnife.bind(this, view);

        initCollectionCardView();
        initRecyclerView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);
                // Fetching data from server
                loadDataToRecyclerView();
            }
        });
    }


    /**
     * Method to initialize recyclerView
     */
    public void initRecyclerView() {

        //RecyclerViewDefinition
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        //Improving performance
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mProductsAdapter = new ProductsAdapter(this, getActivity());
        mRecyclerView.setAdapter(mProductsAdapter);

    }

    void initCollectionCardView() {

        collectionTitleTextView.setText(title);
        bodyTextView.setText(body);

        //Image View Logic
        if (!TextUtils.isEmpty(imgSrc)) {

            Picasso picasso = new Picasso.Builder(getActivity()).
                    downloader(new OkHttp3Downloader(getActivity()))
                    .build();
            picasso.load(imgSrc)
                    .placeholder((R.drawable.ic_launcher_background))
                    .fit()
                    .error(R.drawable.ic_launcher_background)
                    .into(coverImage);
        }


    }

    public void loadDataToRecyclerView() {

        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);

        /*Create handle for the RetrofitInstance interface*/
        ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);

        loadCollectsFromNetwork(service);

    }

    /**
     * Method that makes a API call get all collects and then makes API call to get all products
     *
     * @param service
     * @return List<Collect> - lists of collects
     * null - if network fails
     */

    private void loadCollectsFromNetwork(final ApiService service) {

        Call<CollectsResponse> call = service.getCollects(String.format("%.0f", collectionId), 1, Constants.ACCESS_TOKEN);

        call.enqueue(new Callback<CollectsResponse>() {
            @Override
            public void onResponse(Call<CollectsResponse> call, Response<CollectsResponse> response) {

                Log.d("DEBUG", response.code() + "");
                Log.d("DEBUG", response.message() + "");
                Log.d("DEBUG", response.raw() + "");
                Log.d("DEBUG", response.headers() + "");
                Log.d("DEBUG", response.body() + "");
                Log.d("DEBUG", response.errorBody() + "");

                if (response.isSuccessful()) {

                    loadProductsFromNetworkAndPopulateUI(service, response.body().getCollects());

                } else {

                    Toast.makeText(getActivity(), "Failed to get collects", Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);


                }

            }

            @Override
            public void onFailure(Call<CollectsResponse> call, Throwable t) {
                Log.d("DEBUG", t.getLocalizedMessage() + "");
                Log.d("DEBUG", t.getMessage() + "");
                t.printStackTrace();

                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });


    }

    /**
     * Method that creates a the productId list from a given list of collects ,
     * makes an API call using the list to get all products,
     * poulates UI i,e recycler view
     *
     * @param service
     * @param collects
     */
    private void loadProductsFromNetworkAndPopulateUI(ApiService service, List<Collect> collects) {

        //Making products query Parameters
        String productIds = "";
        for (int i = 0; i < (collects.size() - 1); i++) {

            productIds = productIds + String.format("%.0f", collects.get(i).getProductId()) + ",";

        }
        productIds = productIds + String.format("%.0f", (collects.get(collects.size() - 1).getProductId()));


        //Making network call and populating Recycler View
        Call<ProductsResponse> call = service.getProducts(productIds, 1, Constants.ACCESS_TOKEN);
        call.enqueue(new Callback<ProductsResponse>() {
            @Override
            public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {

                Log.d("DEBUG", response.code() + "");
                Log.d("DEBUG", response.message() + "");
                Log.d("DEBUG", response.raw() + "");
                Log.d("DEBUG", response.headers() + "");
                Log.d("DEBUG", response.body() + "");
                Log.d("DEBUG", response.errorBody() + "");

                if (response.isSuccessful()) {

                    mSwipeRefreshLayout.setRefreshing(false);
                    mProductsAdapter.setProductData(response.body().getProducts());

                } else {

                    Toast.makeText(getActivity(), "Failed to get products", Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;

                }

            }

            @Override
            public void onFailure(Call<ProductsResponse> call, Throwable t) {
                Log.d("DEBUG", t.getLocalizedMessage() + "");
                Log.d("DEBUG", t.getMessage() + "");
                t.printStackTrace();

                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
                mSwipeRefreshLayout.setRefreshing(false);
                return;
            }
        });

    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {

        // Fetching data from server
        loadDataToRecyclerView();
    }

    @Override
    public void onListItemClick(Product product) {

        //Do Nothing as per challenge


    }

}

package com.example.shopifychallenge.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shopifychallenge.R;
import com.example.shopifychallenge.model.CollectionsResponse;
import com.example.shopifychallenge.model.CustomCollection;
import com.example.shopifychallenge.retrofit.ApiService;
import com.example.shopifychallenge.retrofit.RetrofitClientInstance;
import com.example.shopifychallenge.utils.CollectionsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CollectionListFragment extends Fragment implements CollectionsAdapter.ListItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycler_view_collection)
    public RecyclerView mRecyclerView;

    @BindView(R.id.swipe_to_refresh)
    public SwipeRefreshLayout mSwipeRefreshLayout;

    private CollectionsAdapter mCollectionsAdapter;


    public CollectionListFragment() {
        // Required empty public constructor
    }


    public static CollectionListFragment newInstance() {
        CollectionListFragment fragment = new CollectionListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collection_list, container, false);
        ButterKnife.bind(this, view);

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

        mCollectionsAdapter = new CollectionsAdapter(this, getActivity());
        mRecyclerView.setAdapter(mCollectionsAdapter);

    }

    public void loadDataToRecyclerView() {

        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);

        /*Create handle for the RetrofitInstance interface*/
        ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);

        Call<CollectionsResponse> call = service.getCollections(1, "c32313df0d0ef512ca64d5b336a0d7c6");

        call.enqueue(new Callback<CollectionsResponse>() {
            @Override
            public void onResponse(Call<CollectionsResponse> call, Response<CollectionsResponse> response) {

                Log.d("DEBUG", response.code() + "");
                Log.d("DEBUG", response.message() + "");
                Log.d("DEBUG", response.raw() + "");
                Log.d("DEBUG", response.headers() + "");
                Log.d("DEBUG", response.body() + "");
                if (response.isSuccessful()) {

                    mSwipeRefreshLayout.setRefreshing(false);
                    mCollectionsAdapter.setCustomCollectionData(response.body().getCustomCollections());

                } else {

                    Toast.makeText(getActivity(), "Failed to get collections", Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);

                }

            }

            @Override
            public void onFailure(Call<CollectionsResponse> call, Throwable t) {

                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
                mSwipeRefreshLayout.setRefreshing(false);
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
    public void onListItemClick(CustomCollection customCollection) {

        //TODO replace fragment


    }


}

